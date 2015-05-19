package net.gahfy.muslimcompanion.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
    /**
     * Converts a Gregorian Date into julian day
     * @param yearInt the year of the gregorian date
     * @param monthInt the month of the gregorian date
     * @param dayInt the day of the gregorian date
     * @return the julian day
     */
    public static long dateToJulian(int yearInt, int monthInt, int dayInt) {
        long year = monthInt  < 3 ? ((long) yearInt)-1l : (long) yearInt;
        long month = monthInt < 3 ? ((long) monthInt)+12l : (long) monthInt;
        long day = (long) dayInt;
        long s = year/100;
        long b = 2-s+(s/4);
        return (long)(365.25*(year+4716)) + (long)(30.6001*(month+1)) + day + b - 1524;
    }

    /**
     * Returns the Hijri date from a julian day
     * @param julianDay the julian day to convert into hijri
     * @return the hijri date with an integer array (year, month, day)
     */
    public static int[] getHijriFromJulianDay(long julianDay){
        long year = (30l*julianDay - 58442554l)/(10631l);
        long r2 = julianDay-((10631l*year+58442583l)/30l);
        long month = (11l*r2+330l)/325l;
        long r1 = r2-((325l*month-320l)/11l);
        long day = r1+1l;
        return new int[]{(int) year, (int) month, (int) day};
    }

    /**
     * Converts a UTC time in a long timestamp that can then easily be used
     * @param year The year of the date
     * @param month the month of the date
     * @param day the day of the date
     * @param hour the hour expressed in decimal
     * @return the timestamp of this UTC date
     */
    public static long utcTimeToTimestamp(int year, int month, int day, double hour){
        int hourInt = (int) hour;
        int minute = (int) ((hour - (double) hourInt)*60);
        int second = (int) ((hour - (double) hourInt - ((double) minute / 60.0))*3600.0);

        return utcTimeToTimestamp(year, month, day, hourInt, minute, second);
    }

    /**
     * Converts a UTC time in a long timestamp that can then easily be used
     * @param year The year of the date
     * @param month the month of the date
     * @param day the day of the date
     * @param hour the hour of the date
     * @param minute the minute of the date
     * @param second the second of the date
     * @return the timestamp of this UTC date
     */
    public static long utcTimeToTimestamp(int year, int month, int day, int hour, int minute, int second){
        try {
            String dateFormatted = String.format("%04d/%02d/%02d %02d:%02d:%02d +0000", year, month, day, hour, minute, second);

            String dateFormat = "yyyy/MM/dd HH:mm:ss z";
            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.getDefault());

            return format.parse(dateFormatted).getTime();
        }
        catch(ParseException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Returns the year (index 0), month (index 1) and day (index 2) of the given timestamp.
     * @param timestamp the timestamp
     * @return the year (index 0), month (index 1) and day (index 2) of the given timestamp
     */
    public static int[] getDayMonthYear(long timestamp){
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(timestamp);

        int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
        int month = gregorianCalendar.get(Calendar.MONTH)+1;
        int year = gregorianCalendar.get(Calendar.YEAR);

        return new int[]{year, month, day};
    }
}
