package oceanebelle.parser.engine.nmea.parsers;

import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.nmea.model.Coordinates;
import oceanebelle.parser.engine.nmea.model.DateTimeData;
import oceanebelle.parser.engine.nmea.model.NmeaDataMapBuilder;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;

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

    private static final int TIM = 1;
    private static final int STATUS = 3;
    private static final int LAT = 4;
    private static final int LATD = 5;
    private static final int LON = 6;
    private static final int LOND = 7;
    private static final int SPEED = 8;
    private static final int DATE = 10;

    private final static Pattern RMC_PATTERN = Pattern.compile(
            "^\\$GPRMC," +
                    "(\\d+)?(\\.\\d+)?," +           // tim 1 & 2
                    "([AV])," +                     // status 3
                    "(\\d+\\.\\d+)?," +              // lat 4
                    "([NS])?," +                     // latd 5
                    "(\\d+\\.\\d+)?," +              // lon 6
                    "([EW])?," +                     // lond 7
                    "(\\d+\\.\\d+)?," +              // speed 8
                    "(\\d+\\.\\d+)?," +              // track 9
                    "(\\d+)?," +                     // date 10
                    "(\\d+\\.\\d+)?," +           // mag 11
                    "([^,]*,)?" +
                    "[^,]*\\*([A-Za-z\\d]+)$");     // chk 12

    @Override
    public NmeaEvent getHandledEvent() {
        return NmeaEvent.GPRMC;
    }

    @Override
    public boolean isValid(Matcher matcher) {
        return !matcher.group(STATUS).equals("V");
    }

    @Override
    protected Map<NmeaProperty, Object> populatePayload(String rawSentence, Matcher matcher) {
        NmeaDataMapBuilder builder = new NmeaDataMapBuilder();

        builder.setCoordinates(Coordinates.of(
                matcher.group(LAT),
                matcher.group(LATD),
                matcher.group(LON),
                matcher.group(LOND)));
        builder.setSpeed(Float.valueOf(getValueOrDefault(matcher, SPEED, "0")));
        builder.setDateTimeData(DateTimeData.forDateAndTime(matcher.group(DATE), matcher.group(TIM)));

        return builder.toMap();
    }

    private String getValueOrDefault(Matcher matcher, int group, String defaultValue) {
        String value = matcher.group(group);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    @Override
    protected Pattern getPattern() {
        return RMC_PATTERN;
    }
}
