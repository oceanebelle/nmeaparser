package oceanebelle.parser.engine;

import java.io.InputStream;

/**
 * <p>
 * An {@link ParserEngine} is composed of one or more {@link Parser} implementations.
 * The {@link ParserEngine} reads one sentence (line) at a time from the input and
 * a {@link Translator} translates that sentence into an event.
 * The engine uses the event to identity the correct {@link Parser} and delegates
 * parsing to that parser.
 * </p>
 * <p>
 * A {@link Parser} processes an NMEA sentence and notifies {@link ParserHandler}
 * </p>
 * <p>
 * If an error occurs in {@link Parser} during processing an {@link ErrorHandler} is notified when set.
 * </p>
 * <p>
 * A {@link Parser} is associated to a {@link ParserHandler}
 * via an event such as {@link oceanebelle.parser.engine.nmea.NmeaEvent}
 * </p>
 * <p>
 *     A {@link ParserEngineBuilder} is required to build an {@link ParserEngine}.
 *     see {@link oceanebelle.parser.engine.nmea.NmeaParserEngineFactory} for NMEA support
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
