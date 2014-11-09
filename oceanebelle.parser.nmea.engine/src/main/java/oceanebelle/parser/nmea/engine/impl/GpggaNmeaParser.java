package oceanebelle.parser.nmea.engine.impl;

import oceanebelle.parser.nmea.engine.NmeaEvent;
import oceanebelle.parser.nmea.engine.model.Coordinates;
import oceanebelle.parser.nmea.engine.model.FixQuality;
import oceanebelle.parser.nmea.engine.model.NmeaDataMapBuilder;
import oceanebelle.parser.nmea.engine.model.NmeaProperty;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * <p>
 * GGA - essential fix data which provide 3D location and accuracy data.
 *
 * </p>
 * $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
 * <pre>
 * Where:
 * GGA          Global Positioning System Fix Data
 * 123519       Fix taken at 12:35:19 UTC
 * 4807.038,N   Latitude 48 deg 07.038' N
 * 01131.000,E  Longitude 11 deg 31.000' E
 * 1            Fix quality: 0 = invalid
 *                             1 = GPS fix (SPS)
 *                             2 = DGPS fix
 *                             3 = PPS fix
 *                             4 = Real Time Kinematic
 *                             5 = Float RTK
 *                             6 = estimated (dead reckoning) (2.3 feature)
 *                             7 = Manual input mode
 *                             8 = Simulation mode
 * 08           Number of satellites being tracked
 * 0.9          Horizontal dilution of position
 * 545.4,M      Altitude, Meters, above mean sea level
 * 46.9,M       Height of geoid (mean sea level) above WGS84
 * ellipsoid
 * (empty field) time in seconds since last DGPS update
 * (empty field) DGPS station ID number
 * *47          the checksum data, always begins with
 * </pre>
 */
public class GpggaNmeaParser extends AbstractNmeaRegexParser {

    private final static Pattern GGA_PATTERN = Pattern.compile(
            "^\\$GPGGA," +
            "(?<tim>\\d+(\\.\\d+)?)," +
            "(?<lat>\\d+\\.\\d+)," +
            "(?<latd>[NS])," +
            "(?<lon>\\d+\\.\\d+)," +
            "(?<lond>[EW])," +
            "(?<fix>[0-8])," +
            "(?<sat>[0-8]+)," +
            "(?<hdil>\\d+\\.\\d+)," +
            "(?<alt>\\d+\\.\\d+)," +
            "(?<altunit>[M])," +
            "(?<geo>\\d+\\.\\d+)," +
            "(?<geounit>[M]),[^,]*," +
            "[^,]*\\*(?<chk>[A-Za-z\\d]+)$");

    @Override
    public NmeaEvent getHandledEvent() {
        return NmeaEvent.GPGGA;
    }

    @Override
    protected Map<NmeaProperty, Object> populatePayload(String rawSentence, Matcher matcher) {
        NmeaDataMapBuilder builder = new NmeaDataMapBuilder();

        builder.setCoordinates(Coordinates.of(
                matcher.group("lat"),
                matcher.group("latd"),
                matcher.group("lon"),
                matcher.group("lond")));
        builder.setFixQuality(FixQuality.of(matcher.group("fix")));
        builder.setSatellites(Integer.valueOf(matcher.group("sat")));
        builder.setAltitude(Float.valueOf(matcher.group("alt")));

        return builder.toMap();
    }

    @Override
    protected Pattern getPattern() {
        return GGA_PATTERN;
    }
}
