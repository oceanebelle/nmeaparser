package oceanebelle.parser.nmea.engine;

import oceanebelle.parser.nmea.engine.impl.GpggaNmeaParser;
import oceanebelle.parser.nmea.engine.impl.GprmcNmeaParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Main entry point for this library. An engine is a specialised instance
 * that will only hold parsers for which it was configured.
 * The engine takes into account unsupported events if any.
 * Defines which parsers are supported/implemented
 *
 * <pre>
 * How to use:
 * 1. create a builder newBuilder().
 * 2. setup event handlers and error handler for use by builder to create the engine
 * 3. build the engine builder.build().
 * 3. call engine.parse()
 * </pre>
 */
public final class NmeaParserEngineFactory {

    private final static Map<NmeaEvent, Parser<NmeaEvent>> parsers;

    static {
        // TODO: Add all supported parsers here. One for every ParseEvent/type
        parsers = new HashMap<NmeaEvent, Parser<NmeaEvent>>();

        Parser<NmeaEvent> ggaParser = new GpggaNmeaParser();
        parsers.put(ggaParser.getHandledEvent(), ggaParser);

        Parser<NmeaEvent> rmcParser = new GprmcNmeaParser();
        parsers.put(rmcParser.getHandledEvent(), rmcParser);
    }

    private NmeaParserEngineFactory() {}

    /**
     * Creates an instance of a builder from which an ParseEngine can be obtained.
     * @return builder
     */
    public static NmeaParserEngineBuilder newBuilder() {
        return new NmeaParserEngineBuilder(parsers, new NmeaEventTranslator());
    }
}
