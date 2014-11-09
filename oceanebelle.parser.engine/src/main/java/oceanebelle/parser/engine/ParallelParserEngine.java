package oceanebelle.parser.engine;

import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * One thread reads and another thread processes and sends events.
 * @param <T>
 * @param <P>
 */
public class ParallelParserEngine<T, P> implements ParserEngine {

    private final BlockingQueue<ParserTask> taskQueue = new ArrayBlockingQueue<ParserTask>(100);

    private final Map<T, Parser<T, P>> engineParsers;
    private final Map<T, ParserHandler<T, P>> engineHandlers;
    private final Translator<T> translator;
    private final ErrorHandler errorHandler;

    private transient boolean done = false;

    public ParallelParserEngine(Map<T, Parser<T, P>> engineParsers, Map<T, ParserHandler<T, P>> engineHandlers, Translator<T> translator, ErrorHandler errorHandler) {
        this.engineParsers = engineParsers;
        this.engineHandlers = engineHandlers;
        this.translator = translator;
        this.errorHandler = errorHandler;
    }

    @Override
    public int parse(InputStream input) {
        // Calling thread reads the input, processing thread to parse a sentence and pass to delegate handler.
        Scanner scanner = null;
        Thread processor = new Thread(new TaskProcessor());
        processor.setDaemon(true);

        done = false;
        int events_processed = 0;

        try {
            processor.start();

            scanner = new Scanner(input, "UTF-8");
            while(scanner.hasNextLine()) {
                String sentence = scanner.nextLine();

                T event = translator.translate(sentence);
                if (event != null && engineParsers.containsKey(event)) {
                    events_processed++;
                    ParserTask<T, P> task = new ParserTask<T, P>(sentence, engineParsers.get(event), engineHandlers.get(event), errorHandler);
                    // sometimes the processor can take some time so wait patiently...
                    while (!taskQueue.offer(task, 1l, TimeUnit.MILLISECONDS));
                }

            }
            done = true;

            processor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }

        return events_processed;
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
