package oceanebelle.parser.nmea.engine;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple event translator that takes a sentence a does a startsWith NmeaEvent.
 */
public class NmeaEventTranslator implements Translator<NmeaEvent> {

    private final static Set<NmeaEvent> Supported;

    static {
        Supported = new HashSet<NmeaEvent>();
        for (NmeaEvent event : NmeaEvent.values()) {
            if (event.isSupported()) {
                Supported.add(event);
            }
        }
    }

    public NmeaEvent translate(String sentence) {

        for (NmeaEvent event : Supported) {
            if (sentence.startsWith(event.getStartsWith())) {
                return event;
            }
        }

        return null;
    }
}
