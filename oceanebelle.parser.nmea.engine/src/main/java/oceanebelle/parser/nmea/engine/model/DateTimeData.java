package oceanebelle.parser.nmea.engine.model;

import java.util.Calendar;

/**
 * Raw UTC Nmea date data.
 */
public class DateTimeData {

    private final RawDate rawDate;
    private final RawTime rawTime;

    public DateTimeData(RawDate rawDate, RawTime rawTime) {
        this.rawDate = rawDate;
        this.rawTime = rawTime;
    }

    public RawDate getRawDate() {
        return rawDate;
    }

    public RawTime getRawTime() {
        return rawTime;
    }

    public static class RawDate {
        int year;
        int month;
        int day;

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }
    }

    public static class RawTime {
        int hour;
        int min;
        int sec;

        public int getHour() {
            return hour;
        }

        public int getMin() {
            return min;
        }

        public int getSec() {
            return sec;
        }
    }

    public static DateTimeData forTime(String time) {
        return new DateTimeData(null, getTime(time));
    }



    public static DateTimeData forDateAndTime(String date, String time) {
        return new DateTimeData(getDate(date), getTime(time));
    }

    // * 230394       Date - 23rd of March 1994
    private static RawDate getDate(final String date) {
        Calendar now = Calendar.getInstance();
        final int currentCentury = (now.get(Calendar.YEAR) / 100) * 100;
        final int currentYear = now.get(Calendar.YEAR) % 100;

        final int dataYear = Integer.valueOf(date.substring(4));

        return new RawDate(){{
            this.day = Integer.valueOf(date.substring(0, 2));
            this.month = Integer.valueOf(date.substring(2, 4));
            this.year = ((dataYear - currentYear) > 0) ? currentCentury - 100 + dataYear : currentCentury + dataYear;
        }};
    }

    // 123519       Fix taken at 12:35:19 UTC
    private static RawTime getTime(final String time) {
        return new RawTime(){{
            this.hour = Integer.valueOf(time.substring(0, 2));
            this.min = Integer.valueOf(time.substring(2, 4));
            this.sec = Integer.valueOf(time.substring(4, 6));
            // millis is optional in some.
        }};
    }

}
