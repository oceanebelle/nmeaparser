package oceanebelle.parser.engine.nmea.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateTimeDataTest {
    @Test
    public void whenPreviousCentury() {
        // * 230394       Date - 23rd of March 1994
        // 123519       Fix taken at 12:35:19 UTC
        DateTimeData data = DateTimeData.forDateAndTime("230394", "123519");
        assertEquals(23, data.getRawDate().getDay());
        assertEquals(3, data.getRawDate().getMonth());
        assertEquals(1994, data.getRawDate().getYear());

        assertEquals(12, data.getRawTime().getHour());
        assertEquals(35, data.getRawTime().getMin());
        assertEquals(19, data.getRawTime().getSec());
    }

    @Test
    public void whenCuurentCentury() {
        // * 230394       Date - 23rd of March 1994
        // 123519       Fix taken at 12:35:19 UTC
        DateTimeData data = DateTimeData.forDateAndTime("230314", "123519");
        assertEquals(23, data.getRawDate().getDay());
        assertEquals(3, data.getRawDate().getMonth());
        assertEquals(2014, data.getRawDate().getYear());

        assertEquals(12, data.getRawTime().getHour());
        assertEquals(35, data.getRawTime().getMin());
        assertEquals(19, data.getRawTime().getSec());
    }
}
