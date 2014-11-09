package oceanebelle.parser.engine.nmea.model;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Helper for consuming data from the underlying property-value dictionary
 */
public class NmeaDataAdapter {
    private final Map<NmeaProperty, Object> payload;

    public NmeaDataAdapter(Map<NmeaProperty, Object> payload) {
        this.payload = payload;
    }

    public Coordinates getCoordinates() {
        return getProperty(NmeaProperty.Coordinates, Coordinates.class);
    }

    public DateTimeData getDateTimeData() {
        return getProperty(NmeaProperty.DateTimeData, DateTimeData.class);
    }

    public Boolean isChecksumValid() {
        return getProperty(NmeaProperty.IsValidChecksum, Boolean.class);
    }

    public Integer getSatellites() {
        return getProperty(NmeaProperty.Satellites, Integer.class);
    }

    public Float getAltitude() {
        return getProperty(NmeaProperty.Altitude, Float.class);
    }

    public FixQuality getFixQuality() {
        return getProperty(NmeaProperty.FixQuality, FixQuality.class);
    }

    public Float getSpeed() {
        return getProperty(NmeaProperty.Speed, Float.class);
    }

    public boolean hasProperties(NmeaProperty... properties) {
        Set<NmeaProperty> props = new HashSet<NmeaProperty>(properties.length);
        Collections.addAll(props, properties);

        return EnumSet.copyOf(payload.keySet()).containsAll(props);
    }

    public Object getProperty(NmeaProperty property) {
        return payload.get(property);
    }

    public <N> N getProperty(NmeaProperty property, Class<N> type) {
        if (payload.containsKey(property) ) {
            Object data = payload.get(property);
            if (data != null && type.isAssignableFrom(data.getClass())) {
                return type.cast(data);
            }
        }

        return null;
    }
}
