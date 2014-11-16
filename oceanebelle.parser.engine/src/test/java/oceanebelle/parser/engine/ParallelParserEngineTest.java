package oceanebelle.parser.engine;

import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;
import java.util.Map;

public class ParallelParserEngineTest extends NmeaParserEngineTest {

    @Override
    public ParserEngine getEngine(
            Map<NmeaEvent, Parser<NmeaEvent, NmeaProperty>> parsers,
            Map<NmeaEvent, ParserHandler<NmeaEvent, NmeaProperty>> handlers,
            Translator<NmeaEvent> mockTranslator,
            ErrorHandler errorHandler) {
        return new ParallelParserEngine<NmeaEvent, NmeaProperty>(1, parsers, handlers, mockTranslator, errorHandler);
    }
}
