package oceanebelle.parser.engine.nmea.parsers;

import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.nmea.model.*;

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


    private static final int TIME = 1;
    private static final int LAT = 3;
    private static final int LATD = 4;
    private static final int LON = 5;
    private static final int LOND = 6;
    private static final int FIX = 7;
    private static final int SAT = 8;
    private static final int ALT = 10;

    private final static Pattern GGA_PATTERN = Pattern.compile(
            "^\\$GPGGA," +
            "(\\d+)?(\\.\\d+)?," +         // tim      1 & 2
            "(\\d+\\.\\d+)?," +          // lat      3
            "([NS])?," +                 // latd     4
            "(\\d+\\.\\d+)?," +          // lon      5
            "([EW])?," +                 // lond     6
            "([0-8])," +                // fix      7
            "([\\d]+)?," +               // sat      8
            "(\\d+\\.\\d+)?," +          // hdil     9
            "(\\d+\\.\\d+)?," +          // alt      10
            "([M])?," +                  // altunit  11
            "(\\d+\\.\\d+)?," +          // geo      12
            "([M])?," +                 // geounit  13
            "[^,]*," +
            "[^,]*\\*([A-Za-z\\d]+)$"); // checksum 14

    @Override
    public NmeaEvent getHandledEvent() {
        return NmeaEvent.GPGGA;
    }

    @Override
    public boolean isValid(Matcher matcher) {
        return !FixQuality.of(matcher.group(FIX)).equals(FixQuality.INVALID);
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
        builder.setDateTimeData(DateTimeData.forTime(matcher.group(TIME)));

        return builder.toMap();
    }

    @Override
    protected Pattern getPattern() {
        return GGA_PATTERN;
    }
}
