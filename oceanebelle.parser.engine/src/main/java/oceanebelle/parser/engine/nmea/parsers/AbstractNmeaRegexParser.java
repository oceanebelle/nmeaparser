package oceanebelle.parser.engine.nmea.parsers;


import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.ParseException;
import oceanebelle.parser.engine.Parser;
import oceanebelle.parser.engine.nmea.model.NmeaDataAdapter;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;
import oceanebelle.parser.engine.ParserHandler;

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

        // Ignore $
        String[] parts = rawSentence.substring(1).split("\\*");

        boolean validChecksum = validateChecksum(parts[0], parts[1]);

        Matcher matcher = getPattern().matcher(rawSentence);

        if (matcher.matches()) {

            Map<NmeaProperty, Object> payload =  populatePayload(rawSentence, matcher);
            payload.put(NmeaProperty.Type, getHandledEvent().name());
            payload.put(NmeaProperty.IsValidChecksum, validChecksum);
            handler.handle(new NmeaDataAdapter(payload));
        } else {
            throw new ParseException(String.format("%s parser failed to match: %s", getClass(), rawSentence));
        }
    }

    private boolean validateChecksum(String sentencePart, String checksumPart) {
        int calculatedChecksum = 0;
        for (byte charByte : sentencePart.getBytes()) {
            calculatedChecksum ^= charByte;
        }

        return Integer.toHexString(calculatedChecksum).equals(checksumPart);
    }

    protected abstract Map<NmeaProperty, Object> populatePayload(String rawSentence, Matcher matcher);

    protected abstract Pattern getPattern();
}
