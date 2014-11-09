package oceanebelle.parser.engine.nmea.model;

/**
 * Lat Long coordinates created from NMEA format
 */
public class Coordinates {

    private final float latitude;
    private final float longitude;

    public Coordinates(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (Float.compare(that.latitude, latitude) != 0) return false;
        if (Float.compare(that.longitude, longitude) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (latitude != +0.0f ? Float.floatToIntBits(latitude) : 0);
        result = 31 * result + (longitude != +0.0f ? Float.floatToIntBits(longitude) : 0);
        return result;
    }

    /**
     * 4807.038,N   Latitude 48 deg 07.038' N
     * 01131.000,E  Longitude 11 deg 31.000' E
     * @param lat latitude
     * @param latd N/S
     * @param lon longitude
     * @param lond E/W
     * @return coordinates in decimal
     */
    public static Coordinates of(String lat, String latd, String lon, String lond){
        String latDegrees = lat.substring(0, 2);
        String latMinutes = lat.substring(2);

        String lonDegrees = lon.substring(0, 3);
        String lonMinutes = lon.substring(3);

        float latitude = convert(latDegrees, latMinutes, (latd.equalsIgnoreCase("N") ? 1 : -1));
        float longitude = convert(lonDegrees, lonMinutes, (lond.equalsIgnoreCase("E") ? 1 : -1));

        return new Coordinates(latitude, longitude);
    }

    public static float convert(String sDegrees, String sMinutes, int direction) {
        float degrees = Float.valueOf(sDegrees);
        float minutes = Float.valueOf(sMinutes) / 60;

        return (degrees + minutes) * (direction);
    }
}
