package oceanebelle.parser.engine;

import java.util.regex.Matcher;

public interface Parser<T, P> {
    T getHandledEvent();
    boolean isValid(Matcher matcher);
    void parse(String rawSentence, ParserHandler<T, P> handler) throws ParseException;
}
