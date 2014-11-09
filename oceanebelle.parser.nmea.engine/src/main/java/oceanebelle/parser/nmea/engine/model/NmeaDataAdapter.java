package oceanebelle.parser.nmea.engine.model;

import java.util.Map;

/**
 * Helper for consuming data from the underlying property-value dictionary
 */
public class NmeaDataAdapter {
    private final Map<NmeaProperty, Object> payload;

    public NmeaDataAdapter(Map<NmeaProperty, Object> payload) {
        this.payload = payload;
    }

    public Coordinates getCoordinates() {
        return (Coordinates) payload.get(NmeaProperty.Coordinates);
    }

    public DateTimeData getDateTimeData() {
        return (DateTimeData) payload.get(NmeaProperty.DateTimeData);
    }

    public Integer getSatellites() {
        return get(NmeaProperty.Satellites, Integer.class);
    }

    public Float getAltitude() {
        return get(NmeaProperty.Altitude, Float.class);
    }

    public FixQuality getFixQuality() {
        return (FixQuality) payload.get(NmeaProperty.FixQuality);
    }

    public Float getSpeed() {
        return get(NmeaProperty.Speed, Float.class);
    }

    public <N extends Number> N get(NmeaProperty property, Class<N> type) {
        if (payload.containsKey(property) ) {
            Object data = payload.get(property);
            if (data != null && type.isAssignableFrom(data.getClass())) {
                return type.cast(data);
            }
        }

        return null;
    }


}
