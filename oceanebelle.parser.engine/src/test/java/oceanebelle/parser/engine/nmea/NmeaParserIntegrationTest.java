package oceanebelle.parser.engine.nmea;

import oceanebelle.parser.engine.ErrorHandler;
import oceanebelle.parser.engine.ParseException;
import oceanebelle.parser.engine.ParserEngine;
import oceanebelle.parser.engine.ParserHandler;

import oceanebelle.parser.engine.nmea.model.NmeaProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NmeaParserIntegrationTest {
    @Mock
    private ErrorHandler errHandler;
    @Mock
    private ParserHandler<NmeaEvent, NmeaProperty> ggaHandler;
    @Mock
    private ParserHandler<NmeaEvent, NmeaProperty> rmcHandler;

    @Before
    public void setup() {
        when(ggaHandler.getHandledEvent()).thenReturn(NmeaEvent.GPGGA);
        when(rmcHandler.getHandledEvent()).thenReturn(NmeaEvent.GPRMC);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                ((ParseException)invocationOnMock.getArguments()[0]).printStackTrace();
                return null;
            }
        }).when(errHandler).handle(any(ParseException.class));
    }

    @Test
    public void testReadStockholm() {
        // 1-2. Get a builder from the factory and configure a handler for each event
        NmeaParserEngineBuilder builder = NmeaParserEngineFactory.newBuilder()
                .addErrorHandler(errHandler)
                .addEventHandler(rmcHandler)
                .addEventHandler(ggaHandler)
                .setBufferSize(32);

        // 3. Build the engine
        ParserEngine engine = builder.build();

        // 4. Parse a stream of data using the built engine.
        // the registered event handlers will receive the data for each line.
        int events_processed = engine.parse(getClass().getResourceAsStream("/stockholm_walk.nmea"));

        verify(errHandler, times(0)).handle(any(ParseException.class));

        assertEquals(1349, events_processed);

        verify(ggaHandler, atLeast(1)).handle(anyMapOf(NmeaProperty.class, Object.class));
        verify(rmcHandler, atLeast(1)).handle(anyMapOf(NmeaProperty.class, Object.class));

    }
}
