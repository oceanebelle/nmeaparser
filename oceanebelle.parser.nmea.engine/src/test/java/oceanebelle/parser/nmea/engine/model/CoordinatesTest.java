package oceanebelle.parser.nmea.engine.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class CoordinatesTest {
    @Test
    public void testCoordinates() {
        /**
         * 4807.038,N   Latitude 48 deg 07.038' N
         * 01131.000,E  Longitude 11 deg 31.000' E */
        Coordinates coords = Coordinates.of("4807.038", "N", "01131.000", "E");
        assertEquals(48.1173f, coords.getLatitude(), 0f);
        assertEquals(11.516666f, coords.getLongitude(), 0f);


        Coordinates negativeCoords = Coordinates.of("4807.038", "S", "01131.000", "W");
        assertEquals(-48.1173f, negativeCoords.getLatitude(), 0f);
        assertEquals(-11.516666f, negativeCoords.getLongitude(), 0f);

    }
}
