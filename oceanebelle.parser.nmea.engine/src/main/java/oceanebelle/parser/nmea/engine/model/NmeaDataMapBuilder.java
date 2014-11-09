package oceanebelle.parser.nmea.engine.model;

import java.util.HashMap;
import java.util.Map;

public class NmeaDataMapBuilder {

    private final HashMap<NmeaProperty, Object> mapData;

    public NmeaDataMapBuilder() {
        this.mapData = new HashMap<NmeaProperty, Object>();
    }

    public Map<NmeaProperty, Object> toMap() {
        return mapData;
    }

    public NmeaDataMapBuilder setCoordinates(Coordinates coords) {
        mapData.put(NmeaProperty.Coordinates, coords);
        return this;
    }

    public NmeaDataMapBuilder setFixQuality(FixQuality fix) {
        mapData.put(NmeaProperty.FixQuality, fix);
        return this;
    }

    public NmeaDataMapBuilder setSatellites(Integer sat) {
        mapData.put(NmeaProperty.Satellites, sat);
        return this;
    }

    public NmeaDataMapBuilder setAltitude(Float alt) {
        mapData.put(NmeaProperty.Altitude, alt);
        return this;
    }

    public NmeaDataMapBuilder setSpeed(Float speed) {
        mapData.put(NmeaProperty.Speed, speed);
        return this;
    }

    public NmeaDataMapBuilder setDateTimeData(DateTimeData dateTimeData) {
        mapData.put(NmeaProperty.DateTimeData, dateTimeData);
        return this;
    }
}
