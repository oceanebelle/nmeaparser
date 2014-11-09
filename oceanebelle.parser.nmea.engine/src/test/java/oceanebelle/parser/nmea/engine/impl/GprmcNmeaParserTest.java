package oceanebelle.parser.nmea.engine.impl;

import oceanebelle.parser.nmea.engine.NmeaEvent;
import oceanebelle.parser.nmea.engine.ParseException;
import oceanebelle.parser.nmea.engine.ParserHandler;
import oceanebelle.parser.nmea.engine.model.NmeaDataAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class GprmcNmeaParserTest {

    @Captor
    private ArgumentCaptor<NmeaDataAdapter> adapterCaptor;

    @Mock
    private ParserHandler<NmeaEvent> handler;

    private GprmcNmeaParser parser;


    @Before
    public void setup() {
        when(handler.getHandledEvent()).thenReturn(NmeaEvent.GPRMC);
        parser = new GprmcNmeaParser();
    }

    @Test
    public void whenParsingValidReturnDataAdapter() throws ParseException {
        String sentence = "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = adapterCaptor.getValue();
        // set
        assertNotNull(adapter.getCoordinates());
        assertNotNull(adapter.getDateTimeData());
        assertNotNull(adapter.getSpeed());

        // not set
        assertNull(adapter.getAltitude());
        assertNull(adapter.getSatellites());

    }

    @Test
    public void whenParsing() throws ParseException {
        String sentence = "$GPRMC,105540.542,A,5920.5499,N,01803.4380,E,3.081178,41.53,141204,,*3A";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = adapterCaptor.getValue();
        // set
        assertNotNull(adapter.getCoordinates());
        assertNotNull(adapter.getDateTimeData());
        assertNotNull(adapter.getSpeed());

        // not set
        assertNull(adapter.getAltitude());
        assertNull(adapter.getSatellites());
    }
}
