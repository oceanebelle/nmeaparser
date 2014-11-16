package oceanebelle.parser.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public abstract class StreamingParserEngine<T, P> implements ParserEngine {
    private final static int BYTE = 1024;
    private final int bufferSize;

    StreamingParserEngine(int bufferSizeBytes) {
        bufferSize = bufferSizeBytes;
    }


    protected void processStream(InputStream stream, SentenceHandler handler) throws ParseException {
        BufferedReader bis = null;

        try {

            bis = new BufferedReader(new InputStreamReader(stream, "UTF-8"), bufferSize * BYTE);

            String sentence = bis.readLine();
            while (sentence != null) {
                handler.handle(sentence);
                sentence = bis.readLine();
            }

        } catch (UnsupportedEncodingException e) {
            throw new ParseException(e.getMessage(), e);
        } catch (IOException e) {
            throw new ParseException(e.getMessage(), e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    throw new ParseException("Failed to close stream", e);
                }
            }
        }

    }

    interface SentenceHandler {
        public void handle(String sentence) throws ParseException;
    }

    static class StatsData {
        int events_processed;
    }

}
