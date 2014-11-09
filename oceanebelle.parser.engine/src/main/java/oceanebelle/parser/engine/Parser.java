package oceanebelle.parser.engine;

public interface Parser<T, P> {
    T getHandledEvent();
    void parse(String rawSentence, ParserHandler<T, P> handler) throws ParseException;
}
