package oceanebelle.parser.nmea.engine.model;

import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class NmeaDataAdapterTest {
    @Test
    public void whenPropertiesAreSetThenReturnObjects() {
        NmeaDataMapBuilder builder = new NmeaDataMapBuilder();

        builder.setAltitude(4f);
        builder.setSpeed(4f);
        builder.setSatellites(2);
        builder.setCoordinates(Coordinates.of("4807.038", "N", "01131.000", "E"));
        builder.setDateTimeData(DateTimeData.forDateAndTime("230394", "123519"));

        NmeaDataAdapter adapter = new NmeaDataAdapter(builder.toMap());

        assertNotNull(adapter.getAltitude());
        assertNotNull(adapter.getSpeed());
        assertNotNull(adapter.getSatellites());
        assertNotNull(adapter.getCoordinates());
        assertNotNull(adapter.getDateTimeData());
    }

    @Test
    public void whenPropertiesNotSetThenReturnObjects() {
        NmeaDataMapBuilder builder = new NmeaDataMapBuilder();

        NmeaDataAdapter adapter = new NmeaDataAdapter(builder.toMap());

        assertNull(adapter.getAltitude());
        assertNull(adapter.getSpeed());
        assertNull(adapter.getSatellites());
        assertNull(adapter.getCoordinates());
        assertNull(adapter.getDateTimeData());
    }


}
