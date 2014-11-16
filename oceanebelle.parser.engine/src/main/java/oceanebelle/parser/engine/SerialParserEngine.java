package oceanebelle.parser.engine;

import java.io.InputStream;
import java.util.Map;

/**
 * Serial read the sentence and then process.
 * @param <T>
 * @param <P>
 */
public class SerialParserEngine<T, P> extends StreamingParserEngine {

    private final Map<T, Parser<T, P>> engineParsers;
    private final Map<T, ParserHandler<T, P>> engineHandlers;
    private final Translator<T> translator;
    private final ErrorHandler errorHandler;

    public SerialParserEngine(int bufferSize, Map<T, Parser<T, P>> engineParsers, Map<T, ParserHandler<T, P>> engineHandlers, Translator<T> translator, ErrorHandler errorHandler) {
        super(bufferSize);
        this.engineParsers = engineParsers;
        this.engineHandlers = engineHandlers;
        this.translator = translator;
        this.errorHandler = errorHandler;
    }

    @Override
    public int parse(InputStream input) {

        final StatsData stats = new StatsData();
        stats.events_processed = 0;

        try {
            processStream(input, new SentenceHandler() {
                @Override
                public void handle(String sentence) throws ParseException {

                    T event = translator.translate(sentence);
                    if (event != null && engineParsers.containsKey(event)) {
                        stats.events_processed++;
                        Parser<T, P> parser = engineParsers.get(event);
                        ParserHandler<T, P> handler = engineHandlers.get(event);
                        try {
                            parser.parse(sentence, handler);
                        } catch (ParseException e) {
                            if (errorHandler != null) {
                                errorHandler.handle(e);
                            }
                        }
                    }

                }
            });
        } catch (ParseException e) {
            if (errorHandler != null) {
                errorHandler.handle(e);
            }
        }

        return stats.events_processed;
    }

}
