package oceanebelle.parser.engine;

import java.util.Map;

/**
 * handles a sentence
 *
 * @param <T> event type
 * @param <P> property type
 */
public interface ParserHandler<T, P> {
    /**
     * Event to be consumed by this handler
     * @return the event T
     */
    public T getHandledEvent();

    /**
     * Callback method when an event is successfully processed into data.
     * @param payload sentence data in easily consumable interface
     */
    public void handle(Map<P, Object> payload);
}
