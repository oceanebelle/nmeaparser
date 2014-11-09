package oceanebelle.parser.engine;

public interface ParserEngineBuilder<T> {
    /**
     * Hook to allow processing of errors during parsing.
     * If not set, then the error will be ignored and processing continues.
     * @param errorHandler delegate for handling ParseException
     * @return fluent builder
     */
    ParserEngineBuilder<T> addErrorHandler(ErrorHandler errorHandler);

    /**
     * For every event T, register a handler that will receive the data
     * @param handler is invoked when an event T is successfully processed
     * @return fluent builder
     */
    ParserEngineBuilder<T> addEventHandler(ParserHandler<T> handler);

    /**
     * Gathers all parameters specified, validates those arguments and instantiates
     * a correct instance of a parser engine.
     * @return the engine
     * @throws java.lang.IllegalStateException
     */
    ParserEngine build();
}
