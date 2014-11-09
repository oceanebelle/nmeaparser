package oceanebelle.parser.engine;

public interface Parser<T> {
    T getHandledEvent();
    void parse(String rawSentence, ParserHandler<T> handler) throws ParseException;
}
