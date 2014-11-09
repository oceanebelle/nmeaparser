package oceanebelle.parser.engine.nmea.parsers;

import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.ParseException;
import oceanebelle.parser.engine.ParserHandler;
import oceanebelle.parser.engine.nmea.model.Coordinates;
import oceanebelle.parser.engine.nmea.model.NmeaDataAdapter;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GprmcNmeaParserTest {

    @Captor
    private ArgumentCaptor<Map<NmeaProperty, Object>> adapterCaptor;

    @Mock
    private ParserHandler<NmeaEvent, NmeaProperty> handler;

    private GprmcNmeaParser parser;


    @Before
    public void setup() {
        when(handler.getHandledEvent()).thenReturn(NmeaEvent.GPRMC);
        parser = new GprmcNmeaParser();
    }

    @Test(expected = ParseException.class)
    public void whenParsingInvalidPattenThenThrowException() throws ParseException {
        String sentence = "$GPRMC,123519,A,4807.038,N,0,022.4,084.4,230394,003.1,W*6A";

        parser.parse(sentence, handler);
    }

    @Test
    public void whenParsingInvalidChecksum() throws ParseException {
        String sentence = "$GPRMC,220516,A,5133.82,N,00042.24,W,173.8,231.8,130694,004.2,W*71";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = new NmeaDataAdapter(adapterCaptor.getValue());

        // TODO: The data will still be read, should processing be skipped?
        assertFalse(adapter.isChecksumValid());

    }

    @Test
    public void whenParsingValidPattern1() throws ParseException {
        String sentence = "$GPRMC,220516,A,5133.82,N,00042.24,W,173.8,231.8,130694,004.2,W*70";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = new NmeaDataAdapter(adapterCaptor.getValue());

        assertCommonNmeaData(adapter);

        // Assert values
        assertEquals(Coordinates.of("5133.82","N","00042.24","W"), adapter.getCoordinates());
        assertEquals(173.8f, adapter.getSpeed());
        assertEquals(getCalendar(1994, 6, 13, 22, 5, 16), adapter.getDateTimeData().getCalendar());

    }

    @Test
    public void whenParsingValidPattern2() throws ParseException {
        String sentence = "$GPRMC,081836,A,3751.65,S,14507.36,E,000.0,360.0,130998,011.3,E*62";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = new NmeaDataAdapter(adapterCaptor.getValue());

        assertCommonNmeaData(adapter);

        // assert values
        // Assert values
        assertEquals(Coordinates.of("3751.65","S","14507.36","E"), adapter.getCoordinates());
        assertEquals(0f, adapter.getSpeed());
        assertEquals(getCalendar(1998, 9, 13, 8, 18, 36), adapter.getDateTimeData().getCalendar());
    }


    public Calendar getCalendar(int year, int month, int day, int hour, int min, int second) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);

        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0); // millis is not fetched
        return cal;
    }


    private void assertCommonNmeaData(NmeaDataAdapter adapter) {
        assertTrue(adapter.isChecksumValid());
        assertTrue(adapter.hasProperties(
                NmeaProperty.Type,
                NmeaProperty.IsValidChecksum,
                NmeaProperty.Coordinates,
                NmeaProperty.DateTimeData,
                NmeaProperty.Speed));

        assertEquals(NmeaEvent.GPRMC.name(), adapter.getProperty(NmeaProperty.Type, String.class));

        // not set
        assertFalse(adapter.hasProperties(NmeaProperty.Altitude));
        assertFalse(adapter.hasProperties(NmeaProperty.Satellites));

        assertNull(adapter.getAltitude());
        assertNull(adapter.getSatellites());

    }
}
