package net.gahfy.muslimcompanion.utils;

import android.content.Context;
import android.text.format.DateFormat;

import net.gahfy.muslimcompanion.R;

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
        long year = monthInt  < 3 ? ((long) yearInt)-1L : (long) yearInt;
        long month = monthInt < 3 ? ((long) monthInt)+12L : (long) monthInt;
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
        long year = (30L*julianDay - 58442554L)/(10631L);
        long r2 = julianDay-((10631L*year+58442583L)/30L);
        long month = (11L*r2+330L)/325L;
        long r1 = r2-((325L*month-320L)/11L);
        long day = r1+1L;
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
            String dateFormatted = String.format(Locale.US, "%04d/%02d/%02d %02d:%02d:%02d +0000", year, month, day, hour, minute, second);

            String dateFormat = "yyyy/MM/dd HH:mm:ss z";
            SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

            return format.parse(dateFormatted).getTime();
        }
        catch(ParseException e){
            //TODO: Handle error
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

    /**
     * Return an array of integer for the next day.
     * @param year The year of the previous day
     * @param month The month of the previous day
     * @param day The day of the previous day
     * @return An array of integer for the next day
     */
    public static int[] getNextDay(int year, int month, int day){
        if(((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && day<31)
                || (month == 2 && day<29 && year%4==0)
                || (month == 2 && day<28 && year%4!=0)
                || ((month == 4 || month == 6 || month == 9 || month == 11) && day<30))
            return new int[]{year, month, day+1};
        if(month < 12)
            return new int[]{year, month+1, day};
        return new int[]{year+1, 1, 1};
    }

    /**
     * Return an array of integer for the previous day.
     * @param year The year of the next day
     * @param month The month of the next day
     * @param day The day of the next day
     * @return An array of integer for the previous day
     */
    public static int[] getPreviousDay(int year, int month, int day){
        if(day > 1)
            return new int[]{year, month, day-1};
        if(month==2 || month==4 || month==6 || month==8 || month==9 || month==11)
            return new int[]{year, month-1, 31};
        if(month==5 || month==7 || month==10 || month==12)
            return new int[]{year, month-1, 30};
        if(month==3)
            return new int[]{year, month-1, (year%4==0) ? 29 : 28};
        return new int[]{year-1, 12, 31};
    }

    public static String getShortTime(Context context, long timeStamp){
        SimpleDateFormat timeFormat = new SimpleDateFormat(context.getString(DateFormat.is24HourFormat(context) ? R.string.hour_format_24hours : R.string.hour_format_12hours), Locale.getDefault());
        return timeFormat.format(timeStamp);
    }
}
