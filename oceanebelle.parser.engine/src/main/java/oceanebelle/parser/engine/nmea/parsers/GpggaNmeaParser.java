package oceanebelle.parser.engine.nmea.parsers;

import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.nmea.model.Coordinates;
import oceanebelle.parser.engine.nmea.model.FixQuality;
import oceanebelle.parser.engine.nmea.model.NmeaDataMapBuilder;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;

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


    private static final int LAT = 2;
    private static final int LATD = 3;
    private static final int LON = 4;
    private static final int LOND = 5;
    private static final int FIX = 6;
    private static final int SAT = 7;
    private static final int ALT = 9;

    private final static Pattern GGA_PATTERN = Pattern.compile(
            "^\\$GPGGA," +
            "\\d+(\\.\\d+)?," +         // tim      1
            "(\\d+\\.\\d+)," +          // lat      2
            "([NS])," +                 // latd     3
            "(\\d+\\.\\d+)," +          // lon      4
            "([EW])," +                 // lond     5
            "([0-8])," +                // fix      6
            "([0-8]+)," +               // sat      7
            "(\\d+\\.\\d+)," +          // hdil     8
            "(\\d+\\.\\d+)," +          // alt      9
            "([M])," +                  // altunit  10
            "(\\d+\\.\\d+)," +          // geo      11
            "([M]),[^,]*," +            // geounit  12
            "[^,]*\\*([A-Za-z\\d]+)$"); // checksum 13

    @Override
    public NmeaEvent getHandledEvent() {
        return NmeaEvent.GPGGA;
    }

    @Override
    protected Map<NmeaProperty, Object> populatePayload(String rawSentence, Matcher matcher) {
        NmeaDataMapBuilder builder = new NmeaDataMapBuilder();

        builder.setCoordinates(Coordinates.of(
                matcher.group(LAT),
                matcher.group(LATD),
                matcher.group(LON),
                matcher.group(LOND)));
        builder.setFixQuality(FixQuality.of(matcher.group(FIX)));
        builder.setSatellites(Integer.valueOf(matcher.group(SAT)));
        builder.setAltitude(Float.valueOf(matcher.group(ALT)));

        return builder.toMap();
    }

    @Override
    protected Pattern getPattern() {
        return GGA_PATTERN;
    }
}
