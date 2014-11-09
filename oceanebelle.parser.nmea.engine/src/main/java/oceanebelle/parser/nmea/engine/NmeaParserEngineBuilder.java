package oceanebelle.parser.nmea.engine;

import java.util.*;

public class NmeaParserEngineBuilder implements ParserEngineBuilder<NmeaEvent> {

    private final Map<NmeaEvent, Parser<NmeaEvent>> allParsers;
    private final Translator<NmeaEvent> translator;
    private final Map<NmeaEvent, ParserHandler<NmeaEvent>> handlers = new HashMap<NmeaEvent, ParserHandler<NmeaEvent>>();
    private EnumSet<NmeaEvent> events;
    private ErrorHandler errorHandler;

    public NmeaParserEngineBuilder(Map<NmeaEvent, Parser<NmeaEvent>> parsers, Translator<NmeaEvent> translator) {
        this.allParsers = Collections.unmodifiableMap(parsers);
        this.translator = translator;
    }

    @Override
    public NmeaParserEngineBuilder addErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public NmeaParserEngineBuilder addEventHandler(ParserHandler<NmeaEvent> handler) {
        handlers.put(handler.getHandledEvent(), handler);
        return this;
    }


    private NmeaParserEngineBuilder setEvents(Collection<NmeaEvent> eventHints) {

        if (eventHints == null ||  eventHints.isEmpty()) {
            throw new IllegalStateException("Events are not set.");
        }

        EnumSet<NmeaEvent> neededEvents = EnumSet.copyOf(eventHints);

        neededEvents.retainAll(allParsers.keySet());

        this.events = neededEvents;

        return this;
    }

    @Override
    public ParserEngine build() {
        setEvents(handlers.keySet());

        if (!isValid()) {
            throw new IllegalStateException("Required events are not supported or missing or no parser/handler found");
        }

        Map<NmeaEvent, Parser<NmeaEvent>> engineParsers = new HashMap<NmeaEvent, Parser<NmeaEvent>>(events.size());
        Map<NmeaEvent, ParserHandler<NmeaEvent>> engineHandlers = new HashMap<NmeaEvent, ParserHandler<NmeaEvent>>(events.size());

        for (NmeaEvent event : events) {
            engineParsers.put(event, allParsers.get(event));
            engineHandlers.put(event, handlers.get(event));
        }

        return new ParallelParserEngine<NmeaEvent>(engineParsers, engineHandlers, translator, errorHandler);
    }

    private boolean isValid() {
        return events != null && !events.isEmpty();
    }
}
