package oceanebelle.parser.nmea.engine;

import oceanebelle.parser.nmea.engine.model.NmeaDataAdapter;

public interface ParserHandler<T> {
    /**
     * Event to be consumed by this handler
     * @return the event T
     */
    public T getHandledEvent();

    /**
     * Callback method when an event is successfully processed into data.
     * @param payload sentence data in easily consumable interface
     */
    public void handle(NmeaDataAdapter payload);
}
