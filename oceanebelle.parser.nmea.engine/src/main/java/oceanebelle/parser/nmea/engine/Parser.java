package oceanebelle.parser.nmea.engine;

public interface Parser<T> {
    T getHandledEvent();
    void parse(String rawSentence, ParserHandler<T> handler) throws ParseException;
}
