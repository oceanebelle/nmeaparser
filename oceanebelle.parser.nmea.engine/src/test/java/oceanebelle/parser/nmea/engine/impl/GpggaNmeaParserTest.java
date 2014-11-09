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
public class GpggaNmeaParserTest {

    @Captor
    private ArgumentCaptor<NmeaDataAdapter> adapterCaptor;

    @Mock
    private ParserHandler<NmeaEvent> handler;

    private GpggaNmeaParser parser;


    @Before
    public void setup() {
        when(handler.getHandledEvent()).thenReturn(NmeaEvent.GPGGA);
        parser = new GpggaNmeaParser();
    }

    @Test
    public void whenParsingValidReturnDataAdapter() throws ParseException {
        String sentence = "$GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = adapterCaptor.getValue();
        // set
        assertNotNull(adapter.getCoordinates());
        assertNotNull(adapter.getAltitude());
        assertNotNull(adapter.getSatellites());

        // not set
        assertNull(adapter.getDateTimeData());
        assertNull(adapter.getSpeed());

    }

    @Test
    public void whenParsing() throws ParseException {
        String sentence = "$GPGGA,104427.591,5920.7009,N,01803.2938,E,1,05,3.3,78.2,M,23.2,M,0.0,0000*4A";

        parser.parse(sentence, handler);

        verify(handler).handle(adapterCaptor.capture());

        NmeaDataAdapter adapter = adapterCaptor.getValue();
        // set
        assertNotNull(adapter.getCoordinates());
        assertNotNull(adapter.getAltitude());
        assertNotNull(adapter.getSatellites());

        // not set
        assertNull(adapter.getDateTimeData());
        assertNull(adapter.getSpeed());

    }
}
