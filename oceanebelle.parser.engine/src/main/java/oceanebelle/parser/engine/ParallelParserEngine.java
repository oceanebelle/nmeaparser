package oceanebelle.parser.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * One thread reads and another thread processes and sends events.
 * @param <T>
 * @param <P>
 */
public class ParallelParserEngine<T, P> extends StreamingParserEngine {

    private final BlockingQueue<ParserTask> taskQueue = new ArrayBlockingQueue<ParserTask>(100);

    private final Map<T, Parser<T, P>> engineParsers;
    private final Map<T, ParserHandler<T, P>> engineHandlers;
    private final Translator<T> translator;
    private final ErrorHandler errorHandler;

    private transient boolean done = false;

    public ParallelParserEngine(int bufferSize, Map<T, Parser<T, P>> engineParsers, Map<T, ParserHandler<T, P>> engineHandlers, Translator<T> translator, ErrorHandler errorHandler) {
        super(bufferSize);
        this.engineParsers = engineParsers;
        this.engineHandlers = engineHandlers;
        this.translator = translator;
        this.errorHandler = errorHandler;
    }

    @Override
    public int parse(InputStream input) {
        // Calling thread reads the input, processing thread to parse a sentence and pass to delegate handler.

        Thread processor = new Thread(new TaskProcessor());
        processor.setDaemon(true);

        done = false;
        final StatsData stats = new StatsData();
        stats.events_processed = 0;

        try {
            processor.start();

            processStream(input, new SentenceHandler() {
                @Override
                public void handle(String sentence) throws ParseException {
                    T event = translator.translate(sentence);
                    if (event != null && engineParsers.containsKey(event)) {
                        stats.events_processed++;
                        ParserTask<T, P> task = new ParserTask<T, P>(sentence, engineParsers.get(event), engineHandlers.get(event), errorHandler);
                        // sometimes the processor can take some time so wait patiently...
                        try {
                            while (!taskQueue.offer(task, 1l, TimeUnit.MILLISECONDS)) ;
                        } catch (InterruptedException e) {
                            throw new ParseException(e.getMessage(), e);
                        }
                    }
                }
            });

            done = true;

            processor.join();
        } catch (ParseException pe) {
            raiseError(pe);
        } catch (InterruptedException e) {
            raiseError(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                raiseError(e);
            }
        }

        return stats.events_processed;
    }

    private void raiseError(Exception error) {
        if (errorHandler != null) {
            errorHandler.handle(new ParseException(String.format("Parse exception: %s", error.getMessage()), error));
        }
    }

    private class TaskProcessor implements Runnable {

        @Override
        public void run() {
            try {
                while(true) {
                    ParserTask task = taskQueue.poll(1l, TimeUnit.MILLISECONDS);

                    if (task != null) {
                        task.executeTask();
                    }

                    if (done && taskQueue.isEmpty()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                // Either the processor or scanner can complete (due to normal execution or an exception)
                done = true;
            }
        }
    }

    private static class ParserTask<HT, HP> {
        private final Parser<HT, HP> parser;
        private final ParserHandler<HT, HP> handler;
        private final String sentence;
        private final ErrorHandler errHandler;

        ParserTask(String sentence, Parser<HT, HP> parser, ParserHandler<HT, HP> handler, ErrorHandler errHandler) {
            this.parser = parser;
            this.handler = handler;
            this.sentence = sentence;
            this.errHandler = errHandler;
        }

        void executeTask() {
            try {
                parser.parse(sentence, handler);
            } catch (ParseException e) {
                if (errHandler != null) {
                    errHandler.handle(e);
                }
            }
        }
    }

}
