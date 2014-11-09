package oceanebelle.parser.nmea.engine.impl;

import oceanebelle.parser.nmea.engine.NmeaEvent;
import oceanebelle.parser.nmea.engine.model.Coordinates;
import oceanebelle.parser.nmea.engine.model.DateTimeData;
import oceanebelle.parser.nmea.engine.model.NmeaDataMapBuilder;
import oceanebelle.parser.nmea.engine.model.NmeaProperty;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RMC - NMEA has its own version of essential gps pvt (position, velocity, time) data.
 * It is called RMC, The Recommended Minimum, which will look similar to:
 * <p/>
 * $GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A
 * <p/>
 * <pre>
 * Where:
 * RMC          Recommended Minimum sentence C
 * 123519       Fix taken at 12:35:19 UTC
 * A            Status A=active or V=Void.
 * 4807.038,N   Latitude 48 deg 07.038' N
 * 01131.000,E  Longitude 11 deg 31.000' E
 * 022.4        Speed over the ground in knots
 * 084.4        Track angle in degrees True
 * 230394       Date - 23rd of March 1994
 * 003.1,W      Magnetic Variation
 * 6A          The checksum data, always begins with *
 * </pre>
 * Note that, as of the 2.3 release of NMEA, there is a new field in the RMC sentence at the end just prior to the checksum. For more information on this field see here.
 * <p/>
 */
public class GprmcNmeaParser extends AbstractNmeaRegexParser {

    private final static Pattern GGA_PATTERN = Pattern.compile(
            "^\\$GPRMC," +
                    "(?<tim>\\d+(\\.\\d+)?)," +
                    "(?<status>[AV])," +
                    "(?<lat>\\d+\\.\\d+)," +
                    "(?<latd>[NS])," +
                    "(?<lon>\\d+\\.\\d+)," +
                    "(?<lond>[EW])," +
                    "(?<speed>\\d+\\.\\d+)," +
                    "(?<trackAngle>\\d+\\.\\d+)," +
                    "(?<date>\\d+)," +
                    "(?<mag>(\\d+\\.\\d+)?)," +
                    "[^,]*\\*(?<chk>[A-Za-z\\d]+)$");

    @Override
    public NmeaEvent getHandledEvent() {
        return NmeaEvent.GPRMC;
    }

    @Override
    protected Map<NmeaProperty, Object> populatePayload(String rawSentence, Matcher matcher) {
        NmeaDataMapBuilder builder = new NmeaDataMapBuilder();

        builder.setCoordinates(Coordinates.of(
                matcher.group("lat"),
                matcher.group("latd"),
                matcher.group("lon"),
                matcher.group("lond")));
        builder.setSpeed(Float.valueOf(matcher.group("speed")));
        builder.setDateTimeData(DateTimeData.forDateAndTime(matcher.group("date"), matcher.group("tim")));

        return builder.toMap();
    }

    @Override
    protected Pattern getPattern() {
        return GGA_PATTERN;
    }
}
