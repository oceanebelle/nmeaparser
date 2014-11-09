package oceanebelle.parser.engine;

import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class SerialParserEngineTest extends NmeaParserEngineTest {

    @Override
    public ParserEngine getEngine(
            Map<NmeaEvent, Parser<NmeaEvent, NmeaProperty>> parsers,
            Map<NmeaEvent, ParserHandler<NmeaEvent, NmeaProperty>> handlers,
            Translator<NmeaEvent> mockTranslator,
            ErrorHandler errorHandler) {
        return new SerialParserEngine<NmeaEvent, NmeaProperty>(parsers, handlers, mockTranslator, errorHandler);
    }
}
