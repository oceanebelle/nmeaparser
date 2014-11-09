package oceanebelle.parser.engine.nmea;

import oceanebelle.parser.engine.Parser;
import oceanebelle.parser.engine.ParserEngine;
import oceanebelle.parser.engine.ParserHandler;
import oceanebelle.parser.engine.Translator;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NmeaParserEngineBuilderTest {

    @Mock
    private Translator<NmeaEvent> mockTranslator;
    @Mock
    private Parser<NmeaEvent, NmeaProperty> mockParser;
    @Mock
    private ParserHandler<NmeaEvent, NmeaProperty> mockHandler;
    @Mock
    private ParserHandler<NmeaEvent, NmeaProperty> mockMissingEventHandler;

    private Map<NmeaEvent, Parser<NmeaEvent, NmeaProperty>> parsers;
    private NmeaParserEngineBuilder builder;

    @Before
    public void setup() {

        when(mockMissingEventHandler.getHandledEvent()).thenReturn(NmeaEvent.GPAAM);

        when(mockHandler.getHandledEvent()).thenReturn(NmeaEvent.GPAPB);
        when(mockParser.getHandledEvent()).thenReturn(NmeaEvent.GPAPB);

        parsers = new HashMap<NmeaEvent, Parser<NmeaEvent, NmeaProperty>>();
        parsers.put(NmeaEvent.GPAPB, mockParser);

        builder = new NmeaParserEngineBuilder(parsers, mockTranslator);
    }

    @Test(expected = IllegalStateException.class)
    public void whenNoHandlerSetThrowException() {
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void whenNoParserSetThrowException() {
        parsers.clear();
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void whenGpapbEventUnsupportedThenThrowException() {
        builder.addEventHandler(mockMissingEventHandler);
        builder.build();
    }

    @Test
    public void whenGpapbEventThenReturnEngine() {
        builder.addEventHandler(mockHandler);
        ParserEngine engine = builder.build();

        assertNotNull(engine);
    }

}
