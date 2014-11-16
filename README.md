oceanebelle.parser.nmea
=======================

A framework for parsing line by line input sources.
Each line is distinct and unrelated from other lines in the same source.

This project is composed of two modules.

1. oceanebelle.parser.engine - parser library framework
2. oceanebelle.parser.nmea.client - sample usage

## Implemented input sources:

1. NMEA - sentences currently supported
  * GGA
  * RMC


## How to use for parsing NMEA input:
The module oceanebelle.parser.engine produces a **oceanebelle.parser.engine.jar** which you will need to
reference in your classpath

Imports required by this library

```java
        import oceanebelle.parser.engine.ErrorHandler;
        import oceanebelle.parser.engine.ParseException;
        import oceanebelle.parser.engine.ParserEngine;
        import oceanebelle.parser.engine.nmea.NmeaParserEngineBuilder;
        import oceanebelle.parser.engine.nmea.NmeaParserEngineFactory;
        import oceanebelle.parser.engine.nmea.helper.NmeaHandlers;
        import oceanebelle.parser.engine.nmea.model.NmeaDataAdapter;
        import oceanebelle.parser.engine.nmea.model.NmeaProperty;
```

Code Fragment as found in **oceanebelle.parser.nmea.client** module.

Note that call to engine.parse() is _synchronous_ ie the call will not complete until all
lines are read and registered event payload are processed.

A handler is called once for every line associated with that event.

```java
        // 1. Create a builder from NMEA factory
                NmeaParserEngineBuilder builder = NmeaParserEngineFactory.newBuilder();

                // use parallel engine for processing, when processing events may take some time.
                // builder.useParallelEngine(true);

                // uses a serial engine for processing, a line is read and processed before moving on to next
                builder.useParallelEngine(false);

                // set the size of the memory buffer in kB used to store read data from the stream, defaults 1kb
                builder.setBufferSize(32);


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
                        // payload will have properties applicable to RMC
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

                // 4. Parse input
                engine.parse(CLI.class.getResourceAsStream("/stockholm_walk.nmea"));

```