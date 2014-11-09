package oceanebelle.parser.engine;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Serial read the sentence and then process.
 * @param <T>
 * @param <P>
 */
public class SerialParserEngine<T, P> implements ParserEngine {

    private final Map<T, Parser<T, P>> engineParsers;
    private final Map<T, ParserHandler<T, P>> engineHandlers;
    private final Translator<T> translator;
    private final ErrorHandler errorHandler;

    public SerialParserEngine(Map<T, Parser<T, P>> engineParsers, Map<T, ParserHandler<T, P>> engineHandlers, Translator<T> translator, ErrorHandler errorHandler) {
        this.engineParsers = engineParsers;
        this.engineHandlers = engineHandlers;
        this.translator = translator;
        this.errorHandler = errorHandler;
    }

    @Override
    public int parse(InputStream input) {
        Scanner scanner = null;

        int events_processed = 0;

        try {

            scanner = new Scanner(input, "UTF-8");
            while(scanner.hasNextLine()) {
                String sentence = scanner.nextLine();

                T event = translator.translate(sentence);
                if (event != null && engineParsers.containsKey(event)) {
                    events_processed++;
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

        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return events_processed;
    }

}
