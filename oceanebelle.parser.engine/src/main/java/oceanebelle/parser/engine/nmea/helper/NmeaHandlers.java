package oceanebelle.parser.engine.nmea.helper;

import oceanebelle.parser.engine.ParserHandler;
import oceanebelle.parser.engine.nmea.NmeaEvent;
import oceanebelle.parser.engine.nmea.model.NmeaDataAdapter;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;

import java.util.Map;

public final class NmeaHandlers {
    private NmeaHandlers() {}

    public static ParserHandler<NmeaEvent, NmeaProperty> forGGA(HandlerAdapter adapter) {
        return new GenericNmeaEventAdapter(NmeaEvent.GPGGA, adapter);
    }

    public static ParserHandler<NmeaEvent, NmeaProperty> forRMC(HandlerAdapter adapter) {
        return new GenericNmeaEventAdapter(NmeaEvent.GPRMC, adapter);
    }


    private static class GenericNmeaEventAdapter implements ParserHandler<NmeaEvent, NmeaProperty> {
        private final NmeaEvent event;
        private final HandlerAdapter adapter;

        public GenericNmeaEventAdapter(NmeaEvent event, HandlerAdapter adapter) {
            this.event = event;
            this.adapter = adapter;
        }


        @Override
        public NmeaEvent getHandledEvent() {
            return event;
        }

        @Override
        public void handle(Map<NmeaProperty, Object> payload) {
            adapter.handle(new NmeaDataAdapter(payload));
        }
    }

    public interface HandlerAdapter {
        void handle(NmeaDataAdapter adapter);
    }
}
