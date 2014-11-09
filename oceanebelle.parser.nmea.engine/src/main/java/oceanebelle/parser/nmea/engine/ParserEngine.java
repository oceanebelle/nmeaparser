package oceanebelle.parser.nmea.engine;

import java.io.InputStream;

/**
 * <p>
 * An {@link oceanebelle.parser.nmea.engine.ParserEngine} is composed of one or more {@link Parser} implementations.
 * The {@link oceanebelle.parser.nmea.engine.ParserEngine} reads one sentence (line) at a time from the input and
 * a {@link oceanebelle.parser.nmea.engine.Translator} translates that sentence into an event.
 * The engine uses the event to identity the correct {@link oceanebelle.parser.nmea.engine.Parser} and delegates
 * parsing to that parser.
 * </p>
 * <p>
 * A {@link Parser} processes an NMEA sentence and notifies {@link oceanebelle.parser.nmea.engine.ParserHandler}
 * </p>
 * <p>
 * If an error occurs in {@link Parser} during processing an {@link oceanebelle.parser.nmea.engine.ErrorHandler} is notified when set.
 * </p>
 * <p>
 * A {@link oceanebelle.parser.nmea.engine.Parser} is associated to a {@link oceanebelle.parser.nmea.engine.ParserHandler}
 * via an event such as {@link oceanebelle.parser.nmea.engine.NmeaEvent}
 * </p>
 * <p>
 *     A {@link oceanebelle.parser.nmea.engine.ParserEngineBuilder} is required to build an {@link oceanebelle.parser.nmea.engine.ParserEngine}.
 *     see {@link oceanebelle.parser.nmea.engine.NmeaParserEngineFactory} for NMEA support
 * </p>
 */
public interface ParserEngine {

    /**
     * Engine starts parsing the input.
     *
     * @param input source of all sentences
     * @return the number of events processed.
     */
    int parse(InputStream input);
}
