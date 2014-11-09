package oceanebelle.parser.nmea.engine.impl;


import oceanebelle.parser.nmea.engine.NmeaEvent;
import oceanebelle.parser.nmea.engine.ParseException;
import oceanebelle.parser.nmea.engine.Parser;
import oceanebelle.parser.nmea.engine.ParserHandler;
import oceanebelle.parser.nmea.engine.model.NmeaDataAdapter;
import oceanebelle.parser.nmea.engine.model.NmeaProperty;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractNmeaRegexParser implements Parser<NmeaEvent> {

    @Override
    public void parse(String rawSentence, ParserHandler<NmeaEvent> handler) throws ParseException {
        NmeaEvent event = getHandledEvent();

        if (!event.isSupported()) {
            throw new IllegalStateException(String.format("%s is not supported. see %s", event, NmeaEvent.class));
        }

        if (!event.equals(handler.getHandledEvent())) {
            throw new IllegalStateException(String.format("Parser(%s-%s) and Handler(%s-%s) event type do not match.", getClass(), event, handler.getClass(), handler.getHandledEvent()));
        }

        Matcher matcher = getPattern().matcher(rawSentence);

        if (matcher.matches()) {

            Map<NmeaProperty, Object> payload =  populatePayload(rawSentence, matcher);
            payload.put(NmeaProperty.Type, getHandledEvent());
            handler.handle(new NmeaDataAdapter(payload));
        } else {
            throw new ParseException(String.format("%s parser failed to match: %s", getClass(), rawSentence));
        }
    }

    protected abstract Map<NmeaProperty, Object> populatePayload(String rawSentence, Matcher matcher);

    protected abstract Pattern getPattern();
}
