package oceanebelle.parser.nmea.engine;

/**
 * All the list of NmeaEvents that may or may not be supported.
 *
 * N.B. Support requires: adding a Parser implementation and registering that to the Factory ({@link NmeaParserEngineFactory})
 *
 * This enum ties the handlers and parsers.
 *
 * http://www.gpsinformation.org/dale/nmea.htm#nmea
 */
public enum NmeaEvent {
    GPAAM, // - Waypoint Arrival Alarm
    GPALM, // - Almanac data
    GPAPA, // - Auto Pilot A sentence
    GPAPB, // - Auto Pilot B sentence
    GPBOD, // -  Bearing Origin to Destination
    GPBWC, // - Bearing using Great Circle route
    GPDTM, // - Datum being used.
    GPGGA(true,
        "$GPGGA,"
    ), // - Fix information
    GPGLL(), // - Lat/Lon data
    GPGRS, // - GPS Range Residuals
    GPGSA, // - Overall Satellite data
    GPGST, // - GPS Pseudorange Noise Statistics
    GPGSV, // - Detailed Satellite data
    GPMSK, // - send control for a beacon receiver
    GPMSS, // - Beacon receiver status information.
    GPRMA, // - recommended Loran data
    GPRMB, // - recommended navigation data for gps
    GPRMC(true,
        "$GPRMC,"), // - recommended minimum data for gps
    GPRTE, // - route message
    GPTRF, // - Transit Fix Data
    GPSTN, // - Multiple Data ID
    GPVBW, // - dual Ground / Water Spped
    GPVTG, // - Vector track an Speed over the Ground
    GPWCV, // - Waypoint closure velocity (Velocity Made Good)
    GPWPL, // - Waypoint Location information
    GPXTC, // - cross track error
    GPXTE, // - measured cross track error
    GPZTG, // - Zulu (UTC) time and time to go (to destination)
    GPZDA; // - Date and Time

    private final String startsWith;
    private final boolean supported;

    NmeaEvent() {
        this(false, null);
    }

    /**
     *
     * @param startsWith - for use in translation
     */
    NmeaEvent(boolean supported, String startsWith) {
        this.startsWith = startsWith;
        this.supported = supported;

    }

    public boolean isSupported() {
        return supported;
    }

    public String getStartsWith() {
        return startsWith;
    }
}
