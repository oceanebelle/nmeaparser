package oceanebelle.parser.nmea.client;

import oceanebelle.parser.engine.ErrorHandler;
import oceanebelle.parser.engine.ParseException;
import oceanebelle.parser.engine.ParserEngine;
import oceanebelle.parser.engine.nmea.NmeaParserEngineBuilder;
import oceanebelle.parser.engine.nmea.NmeaParserEngineFactory;
import oceanebelle.parser.engine.nmea.helper.NmeaHandlers;
import oceanebelle.parser.engine.nmea.model.NmeaDataAdapter;
import oceanebelle.parser.engine.nmea.model.NmeaProperty;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 *
 */
public class CLI
{
    public static void main( String[] args ) throws FileNotFoundException {

        // 1. Create a builder from NMEA factory
        NmeaParserEngineBuilder builder = NmeaParserEngineFactory.newBuilder();

        // 2. Configure builders by adding handlers
        builder.addErrorHandler(new ErrorHandler() {
            @Override
            public void handle(ParseException error) {
                // Do something with the error here
                error.printStackTrace();
            }
        });

        // Handler for GGA
        builder.addEventHandler(NmeaHandlers.forGGA(new NmeaHandlers.HandlerAdapter() {
            @Override
            public void handle(NmeaDataAdapter payload) {
                // payload will have properties applicable to GGA
                // Note the payload has helper methods to retrieve properties
                System.out.println(toPrettyPrint(
                        payload,
                        NmeaProperty.Type,
                        NmeaProperty.IsValidChecksum,
                        NmeaProperty.Coordinates,
                        NmeaProperty.Altitude,
                        NmeaProperty.Satellites
                ));
            }
        }));

        // Handler for RMC
        builder.addEventHandler(NmeaHandlers.forRMC(new NmeaHandlers.HandlerAdapter() {
            @Override
            public void handle(NmeaDataAdapter payload) {
                // payload will have properties applicable to GGA
                System.out.println(toPrettyPrint(
                        payload,
                        NmeaProperty.Type,
                        NmeaProperty.IsValidChecksum,
                        NmeaProperty.Coordinates,
                        NmeaProperty.Speed,
                        NmeaProperty.DateTimeData
                ));
            }
        }));

        // 3. Build the engine
        ParserEngine engine = builder.build();

        // 4. Call parse() on engine
        if (args.length == 0) {
            engine.parse(CLI.class.getResourceAsStream("/stockholm_walk.nmea"));
        } else {
            engine.parse(new FileInputStream(args[0]));
        }

    }

    private static String toPrettyPrint(NmeaDataAdapter payload, NmeaProperty... properties) {
        StringBuilder sb = new StringBuilder();

        for(NmeaProperty property : properties) {
            sb.append(property)
                    .append("=").
                    append(payload.getProperty(property))
                    .append("; ");
        }

        return sb.toString();
    }
}
