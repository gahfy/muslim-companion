package net.gahfy.muslimcompanion.utils;

import java.util.Calendar;

public class DateUtils {
    public static double dateToJulian(int yearInt, int monthInt, int dayInt) {
        double year = (double) yearInt;
        double month = (double) monthInt;
        double day = (double) dayInt;

        double a = month < 3 ? 1 : 0;
        double y = year+4800-a;
        double m = month + 12*a -3;

        return day +
                ((153.0*m+2.0)/(5.0))+
                365*y+
                y/4-
                y/100+
                y/400-
                32045;
    }
}
