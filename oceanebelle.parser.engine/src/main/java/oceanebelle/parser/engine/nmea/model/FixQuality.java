package oceanebelle.parser.engine.nmea.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 1            Fix qualityValue: 0 = invalid
 1 = GPS fix (SPS)
 2 = DGPS fix
 3 = PPS fix
 4 = Real Time Kinematic
 5 = Float RTK
 6 = estimated (dead reckoning) (2.3 feature)
 7 = Manual input mode
 8 = Simulation mode
 */
public enum FixQuality {
    INVALID("0"),
    GPS_SPS("1"),
    DGPS("2"),
    PPS("3"),
    RTK("4"),
    FLOAT_RTK("5"),
    ESTIMATED("6"),
    MANUAL("7"),
    SIMULATED("8")
    ;

    private static final Map<String, FixQuality> QUALITY_MAP;

    static {
        QUALITY_MAP = new HashMap<String, FixQuality>();
        for(FixQuality fixQuality : FixQuality.values()) {
            QUALITY_MAP.put(fixQuality.qualityValue, fixQuality);
        }
    }

    private final String qualityValue;

    FixQuality(String quality) {
        this.qualityValue = quality;
    }

    public static FixQuality of(String value) {
        if (QUALITY_MAP.containsKey(value)) {
            return QUALITY_MAP.get(value);
        }
        return INVALID;
    }
}
