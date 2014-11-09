package oceanebelle.parser.engine.nmea;

import oceanebelle.parser.engine.ErrorHandler;
import oceanebelle.parser.engine.ParallelParserEngine;
import oceanebelle.parser.engine.Parser;
import oceanebelle.parser.engine.ParserEngine;
import oceanebelle.parser.engine.ParserEngineBuilder;
import oceanebelle.parser.engine.ParserHandler;
import oceanebelle.parser.engine.Translator;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;

import java.util.*;

public class NmeaParserEngineBuilder implements ParserEngineBuilder<NmeaEvent, NmeaProperty> {

    private final Map<NmeaEvent, Parser<NmeaEvent, NmeaProperty>> allParsers;
    private final Translator<NmeaEvent> translator;
    private final Map<NmeaEvent, ParserHandler<NmeaEvent, NmeaProperty>> handlers = new HashMap<NmeaEvent, ParserHandler<NmeaEvent, NmeaProperty>>();
    private EnumSet<NmeaEvent> events;
    private ErrorHandler errorHandler;

    public NmeaParserEngineBuilder(Map<NmeaEvent, Parser<NmeaEvent, NmeaProperty>> parsers, Translator<NmeaEvent> translator) {
        this.allParsers = Collections.unmodifiableMap(parsers);
        this.translator = translator;
    }

    @Override
    public NmeaParserEngineBuilder addErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    @Override
    public NmeaParserEngineBuilder addEventHandler(ParserHandler<NmeaEvent, NmeaProperty> handler) {
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

        Map<NmeaEvent, Parser<NmeaEvent, NmeaProperty>> engineParsers = new HashMap<NmeaEvent, Parser<NmeaEvent, NmeaProperty>>(events.size());
        Map<NmeaEvent, ParserHandler<NmeaEvent, NmeaProperty>> engineHandlers = new HashMap<NmeaEvent, ParserHandler<NmeaEvent, NmeaProperty>>(events.size());

        for (NmeaEvent event : events) {
            engineParsers.put(event, allParsers.get(event));
            engineHandlers.put(event, handlers.get(event));
        }

        return new ParallelParserEngine<NmeaEvent, NmeaProperty>(engineParsers, engineHandlers, translator, errorHandler);
    }

    private boolean isValid() {
        return events != null && !events.isEmpty();
    }
}
