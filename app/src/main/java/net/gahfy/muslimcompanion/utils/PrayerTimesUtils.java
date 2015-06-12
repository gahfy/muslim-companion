package net.gahfy.muslimcompanion.utils;

import android.content.Context;

import net.gahfy.muslimcompanion.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class PrayerTimesUtils {
    /**
     * Number of milliseconds in a day.
     */
    private static final int DAY_MILLIS = 24*60*60*1000;

    /**
     * All the possible schools for Asr calculation
     */
    public enum School{
        /** Everything except Hanafi */
        NOT_HANAFI,
        /** Hanafi school */
        HANAFI
    }

    /**
     * List of possible mode of calculation in abnormal periods at higher latitudes for Fajr and
     * Isha
     */
    public enum HigherLatitudeMode{
        /** Middle of the night (Fajr and Isha are at the middle of the night) */
        MIDDLE_OF_THE_NIGHT,
        /** One seventh (Isha is at one seventh of the night, Fajr at six seventh of the night) */
        SEVENTH,
        /** Night is divided into (fajrAngle/60) parts and fajr starts at the last period.
         * Similar calculation for Isha
         */
        ANGLE_BASED
    }

    /**
     * All the possible conventions for prayer time calculation
     */
    public enum Convention{
        /** Muslim World League */
        MUSLIM_WORLD_LEAGUE,
        /** Islamic Society of North America (ISNA) */
        ISLAMIC_SOCIETY_OF_NORTH_AMERICA,
        /** Egyptian General Authority of Survey */
        EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY,
        /** Umm al-Qura University, Makkah */
        UMM_AL_QURA_UNIVERSITY_MAKKAH,
        /** University of Islamic Sciences, Karachi */
        UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI,
        /** Institute of Geophysics, University of Tehran */
        INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN,
        /** Shia Ithna Ashari, Leva Research Institute, Qum */
        SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM,
        /** Ministry of religious affairs and Wakfs, Algeria */
        MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA,
        /** Ministry of Habous and Islamic Affairs, Morocco */
        MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO,
        /** Ministry of Religious affairs, Tunisia*/
        MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA,
        /** Calculation rules in South East Asia */
        SOUTH_EAST_ASIA,
        /** Union of Islamic Organisations of France */
        UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE,
        /** Grand Mosque of Paris */
        GRAND_MOSQUE_OF_PARIS,
        /** Presidency of Religious Affairs, Turkey */
        PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY,
        /** Ministry of Endowments and Religious Affairs, Oman */
        MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN,
        /** General Authority of Islamic Affairs & Endowments, U.A.E. */
        GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE,
        /** Department of Islamic Affairs and charitable activities, Dubai */
        DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI,
        /** Ministry of Awqaf Islamic Affairs and Holy Places, Jordan */
        MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN,
        /** Ministry of Awqaf and Islamic Affairs, Kuwait */
        MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT,
        /** Qatar calendar house */
        QATAR_CALENDAR_HOUSE,
        /** Ministry of Endowments and Islamic Affairs, Lybia*/
        MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA,
        /** Ministry of Islamic Affairs, Maldives*/
        MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES,
        /** Birmingham Central Mosque */
        BIRMINGHAM_CENTRAL_MOSQUE,
        /** London Central Mosque */
        LONDON_CENTRAL_MOSQUE,
        /** Islamic Centre of Quebec */
        ISLAMIC_CENTRE_OF_QUEBEC,
        /** Munich, Germany */
        MUNICH_GERMANY
    }

    public static final int MUSLIM_WORLD_LEAGUE_VALUE = 0;
    public static final int ISLAMIC_SOCIETY_OF_NORTH_AMERICA_VALUE = 1;
    public static final int EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY_VALUE = 2;
    public static final int UMM_AL_QURA_UNIVERSITY_MAKKAH_VALUE = 3;
    public static final int UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI_VALUE = 4;
    public static final int INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN_VALUE = 5;
    public static final int SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM_VALUE = 6;
    public static final int MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA_VALUE = 7;
    public static final int MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO_VALUE = 8;
    public static final int MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA_VALUE = 9;
    public static final int SOUTH_EAST_ASIA_VALUE = 10;
    public static final int UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE_VALUE = 11;
    public static final int PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY_VALUE = 12;
    public static final int MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN_VALUE = 13;
    public static final int GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE_VALUE = 14;
    public static final int DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI_VALUE = 15;
    public static final int MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN_VALUE = 16;
    public static final int MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT_VALUE = 17;
    public static final int QATAR_CALENDAR_HOUSE_VALUE = 18;
    public static final int MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA_VALUE = 19;
    public static final int MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES_VALUE = 20;
    public static final int BIRMINGHAM_CENTRAL_MOSQUE_VALUE = 21;
    public static final int LONDON_CENTRAL_MOSQUE_VALUE = 22;
    public static final int MUNICH_GERMANY_VALUE = 23;
    public static final int GRAND_MOSQUE_OF_PARIS_VALUE = 24;
    public static final int ISLAMIC_CENTRE_OF_QUEBEC_VALUE = 25;

    public static final int HANAFI_VALUE = 0;
    public static final int NOT_HANAFI_VALUE = 1;

    public static final int MIDDLE_OF_THE_NIGHT_VALUE = 0;
    public static final int SEVENTH_VALUE = 1;
    public static final int ANGLE_BASED_VALUE = 2;

    private Context context;
    /** The year that should be used for the calculation */
    private int year;
    /** The month that should be used for the calculation */
    private int month;
    /** The day that should be used for the calculation */
    private int day;
    /** Time as the centuries ellapsed since January 1, 2000 at 12:00 GMT. */
    private double time;
    /** The latitude for which the prayer times should be calculated */
    private double latitude;
    /** The longitude for which the prayer times should be calculated */
    private double longitude;
    /** The angle for calculation of Fajr */
    private double fajrAngle;
    /** The delay to add for calculation of Fajr */
    private double fajrDelay = 0.0;
    /** The angle for calculation of Isha */
    private double ishaAngle;
    /** The delay to add for calculation of Isha */
    private double ishaDelay = 0.0;
    /** The delay to add to Maghrib */
    private double maghribDelay = 0.0;
    /** The delay to add for calculation of Isha */
    private double ishaDelayNormal = 0.0;
    /** The delay to add for calculation of Isha on Ramadhan */
    private double ishaDelayOnRamadhan = 0.0;
    /** The delay to add for calculation of asr */
    private double asrDelay = 0.0;
    /** The number of times the shadow should be bigger than an object for calculation of Asr */
    private double asrTime = 1.0;
    /** The delay to add for calculation of Sunrise */
    private double sunriseDelay = 0.0;
    /** The angle for calculation of Sunrise */
    private double sunriseAngle = 5.0/6.0;
    /** The angle for calculation of Maghrib */
    private double maghribAngle = 5.0/6.0;
    /** The delay to add for calculation of Dhuhr */
    private double dhuhrDelay = 0.0;
    /** The calculation mode for Fajr and Isha at abnormal periods*/
    private HigherLatitudeMode higherLatitudeMode = HigherLatitudeMode.ANGLE_BASED;

    /**
     * Instantiates a new Prayer Time Utils.
     * @param year the year of the date to calculate prayer times
     * @param month the month of the date to calculate prayer times
     * @param day the day of the date to calculate prayer times
     * @param latitude the latitude to calculate prayer times
     * @param longitude the longitude to calculate prayer times
     * @param convention the convention to use to calculate prayer times
     */
    public PrayerTimesUtils(Context context, int year, int month, int day, double latitude, double longitude, Convention convention, School school){
        super();
        initMembers(context, year, month, day, latitude, longitude, convention, school);
    }

    /**
     * Initializes the properties of the object.
     * @param year the year of the date to calculate prayer times
     * @param month the month of the date to calculate prayer times
     * @param day the day of the date to calculate prayer times
     * @param latitude the latitude to calculate prayer times
     * @param longitude the longitude to calculate prayer times
     * @param convention the convention to use to calculate prayer times
     */
    public void initMembers(Context context, int year, int month, int day, double latitude, double longitude, Convention convention, School school){
        double julianDay = DateUtils.dateToJulian(year, month, day);
        this.context     = context;
        this.year        = year;
        this.month       = month;
        this.day         = day;
        this.latitude    = latitude;
        this.longitude = longitude;
        time             = (julianDay-2451545)/36525;
        changeConvention(convention, school, HigherLatitudeMode.ANGLE_BASED);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public void changeCountry(String countryIso){
        Pair pair = COUNTRY_DEFAULT_CONVENTION.get(countryIso);
        if(pair != null){
            changeConvention(pair.getConvention(), pair.getSchool(), HigherLatitudeMode.ANGLE_BASED);
        }
    }

    public void changeConvention(Convention convention, School school, HigherLatitudeMode mode){
        if(!SharedPreferencesUtils.getConventionIsAutomatic(context)){
            convention = getConventionFromPreferenceValue(SharedPreferencesUtils.getConventionValue(context));
        }
        else{
            SharedPreferencesUtils.putConvention(context, getConventionPreferenceValue(convention));
        }

        if(!SharedPreferencesUtils.getSchoolIsAutomatic(context)){
            school = getSchoolFromPreferenceValue(SharedPreferencesUtils.getSchoolValue(context));
        }
        else{
            SharedPreferencesUtils.putSchool(context, getSchoolPreferenceValue(school));
        }

        if(!SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(context)){
            mode = getHigherLatitudeModeFromPreferenceValue(SharedPreferencesUtils.getHigherLatitudeModeValue(context));
        }
        else{
            SharedPreferencesUtils.putHigherLatitudeMode(context, getHigherLatitudeModePreferenceValue(mode));
        }

        higherLatitudeMode = mode;

        switch (school){
            case HANAFI:
                asrTime = 2.0;
                break;
            case NOT_HANAFI:
                asrTime = 1.0;
                break;
        }

        switch(convention){
            case MUSLIM_WORLD_LEAGUE:
                fajrAngle = 18.0;
                ishaAngle = 17.0;
                break;
            case ISLAMIC_SOCIETY_OF_NORTH_AMERICA:
                fajrAngle = 15.0;
                ishaAngle = 15.0;
                break;
            case EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY:
                fajrAngle = 19.5;
                ishaAngle = 17.5;
                break;
            case UMM_AL_QURA_UNIVERSITY_MAKKAH:
                fajrAngle = 18.5;
                ishaAngle = maghribAngle;
                ishaDelayNormal = 1.5;
                ishaDelayOnRamadhan = 2.0;
                break;
            case UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI:
                fajrAngle = 18.0;
                ishaAngle = 18.0;
                break;
            case INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN:
                fajrAngle = 17.7;
                ishaAngle = 14.0;
                break;
            case SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM:
                fajrAngle = 16.0;
                ishaAngle = 14.0;
                maghribAngle = 4.0;
                break;
            case DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI:
                fajrAngle = 18.5;
                ishaAngle = maghribAngle;
                ishaDelayNormal = 1.5;
                ishaDelayOnRamadhan = 2.0;
                dhuhrDelay = 4.0/60.0;
                asrDelay = 5.0/60.0;
                maghribDelay = 2.0/60.0;
                break;
            case MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA:
                fajrAngle = 18.0;
                ishaAngle = 17.0;
                maghribDelay = 3.0/60.0;
                break;
            case MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO:
                fajrAngle = 18.0;
                ishaAngle = 17.0;
                fajrDelay = -1.0/60.0;
                sunriseDelay = -2.0/60.0;
                dhuhrDelay = 5.0/60.0;
                maghribDelay = 3.0/60.0;
                break;
            case MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA:
                fajrAngle = 18.0;
                ishaAngle = 18.0;
                fajrDelay = -1.0/60.0;
                dhuhrDelay = 7.0/60.0;
                maghribDelay = 1.0/60.0;
                ishaDelay = 1.0/60.0;
                break;
            case SOUTH_EAST_ASIA:
                fajrAngle = 20.0;
                ishaAngle = 18.0;
                fajrDelay = 3.0/60.0;
                sunriseDelay = 2.0/60.0;
                dhuhrDelay = 1.0/60.0;
                asrDelay = 2.0/60.0;
                maghribDelay = 1.0/60.0;
                ishaDelay = -1.0/60.0;
                break;
            case UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE:
                fajrAngle = 12.0;
                ishaAngle = 12.0;
                fajrDelay = -5.0/60.0;
                dhuhrDelay = 5.0/60.0;
                maghribDelay = 4.0/60.0;
                ishaDelay = 5.0/60.0;
                break;
            case PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY:
                fajrAngle = 18.0;
                ishaAngle = 17.0;
                fajrDelay = -2.0/60.0;
                sunriseDelay = -7.0/60.0;
                dhuhrDelay = 6.0/60.0;
                asrDelay = 4.0/60.0;
                maghribDelay = 9.0/60.0;
                ishaDelay = 2.0/60.0;
                break;
            case MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN:
                fajrAngle = 18.0;
                ishaAngle = 18.0;
                dhuhrDelay = 5.0/60.0;
                asrDelay = 5.0/60.0;
                maghribDelay = 5.0/60.0;
                ishaDelay = 1.0/60.0;
                break;
            case GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE:
                fajrAngle = 18.5;
                ishaAngle = maghribAngle;
                asrDelay = 4.0/60.0;
                maghribDelay = 2.0/60.0;
                ishaDelay = 2.0/60.0;
                ishaDelayNormal = 1.5;
                ishaDelayOnRamadhan = 2.0;
                break;
            case MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN:
                fajrAngle = 18.0;
                ishaAngle = 18.0;
                break;
            case MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT:
                fajrAngle = 18.0;
                ishaAngle = 17.5;
                break;
            case QATAR_CALENDAR_HOUSE:
                fajrAngle = 18.0;
                ishaAngle = maghribAngle;
                dhuhrDelay = 4.0/60.0;
                maghribDelay = 4.0/60.0;
                ishaDelay = 4.0/60.0;
                ishaDelayNormal = 1.5;
                ishaDelayOnRamadhan = 2.0;
                break;
            case MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA:
                fajrAngle = 18.5;
                ishaAngle = 18.5;
                dhuhrDelay = 4.0/60.0;
                maghribDelay = 4.0/60.0;
                break;
            case MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES:
                fajrAngle = 19.0;
                ishaAngle = 19.0;
                sunriseDelay = -1.0/60.0;
                dhuhrDelay = 4.0/60.0;
                asrDelay = 1.0/60.0;
                maghribDelay = 1.0/60.0;
                ishaDelay = 1.0/60.0;
                break;
            case BIRMINGHAM_CENTRAL_MOSQUE:
                fajrAngle = 5.0;
                ishaAngle = maghribAngle;
                fajrDelay = -1.0;
                ishaDelay = 1.0;
                break;
            case LONDON_CENTRAL_MOSQUE:
                fajrAngle = 5.0;
                ishaAngle = maghribAngle;
                fajrDelay = -1.0;
                ishaDelay = 1.0;
                break;
            case MUNICH_GERMANY:
                fajrAngle = 18.0;
                ishaAngle = 17.0;
                break;
            case GRAND_MOSQUE_OF_PARIS:
                fajrAngle = 18.0;
                ishaAngle = 17.0;
                break;
            case ISLAMIC_CENTRE_OF_QUEBEC:
                fajrAngle = 17.79;
                ishaAngle = 17.6;
                asrDelay = 5.0/60.0;
                maghribDelay = 5.0/60.0;
                break;
        }
    }

    /**
     * Returns the time of Dhuhr (UTC time in decimal hour).
     * @return the time of Dhuhr (UTC time in decimal hour)
     */
    public double getDhuhr(){
        return 12.0 - (longitude/15.0) - (equationOfTime(time)/60.0);
    }

    public double getDhuhrOfPreviousDay(){
        return 12.0 - (longitude/15.0) - (equationOfTime(time-(1.0/36525.0))/60.0);
    }

    public double getDhuhrOfNextDay(){
        return 12.0 - (longitude/15.0) - (equationOfTime(time+(1.0/36525.0))/60.0);
    }

    public boolean isFriday(){
        long dhuhrTimestamp = getDhuhrTimestamp();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(dhuhrTimestamp);

        return calendar.get(Calendar.DAY_OF_WEEK) == 6;
    }

    /**
     * Returns the timestamp of Dhuhr.
     * @return the timestamp of Dhuhr
     */
    public long getDhuhrTimestamp(){
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, getDhuhr() + dhuhrDelay);
        return realTime - (realTime%60000l) + 60000l;
    }

    public long getDhuhrOfNextDayTimestamp(){
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, getDhuhrOfNextDay() + dhuhrDelay)+24l*3600l*1000l;
        return realTime - (realTime%60000l) + 60000l;
    }

    /**
     * Returns the timestamp of Sunrise.
     * @return the timestamp of Sunrise
     */
    public long getSunriseTimestamp(){
        double hoursInTheDay = getDhuhr() - getTimeBelowHorizonDifference(sunriseAngle) + sunriseDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay);
        return realTime - (realTime%60000l) + 60000l;
    }

    public long getSunriseOfNextDayTimestamp(){
        double hoursInTheDay = getDhuhrOfNextDay() - getTimeBelowHorizonDifference(sunriseAngle, time + (1.0/36525.0)) + sunriseDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay) + 24l*3600l*1000l;
        return realTime - (realTime%60000l) + 60000l;
    }

    /**
     * Returns the timestamp of Maghrib.
     * @return the timestamp of Maghrib
     */
    public long getMaghribTimestamp(){
        double hoursInTheDay = getDhuhr() + getTimeBelowHorizonDifference(maghribAngle) + maghribDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay);
        return realTime - (realTime%60000l) + 60000l;
    }

    public long getMaghribOfPreviousDayTimestamp(){
        double hoursInTheDay = getDhuhrOfPreviousDay() + getTimeBelowHorizonDifference(maghribAngle, time - (1.0/36525.0)) + maghribDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay) - 24l*3600l*1000l;
        return realTime - (realTime%60000l) + 60000l;
    }

    /**
     * Returns the timestamp of Fajr.
     * @return the timestamp of Fajr
     */
    public long getFajrTimestamp(){
        double timeBelowHorizonDifference = getTimeBelowHorizonDifference(fajrAngle);
        if(Double.isNaN(timeBelowHorizonDifference)){
            switch (higherLatitudeMode){
                case MIDDLE_OF_THE_NIGHT:
                    return getSunriseTimestamp() - (long)(((double)(getSunriseTimestamp() - getMaghribOfPreviousDayTimestamp()))/2.0) + (long)(fajrDelay*3600.0*1000.0);
                case SEVENTH:
                    return getSunriseTimestamp() - (long)(((double)(getSunriseTimestamp() - getMaghribOfPreviousDayTimestamp()))/7.0) + (long)(fajrDelay*3600.0*1000.0);
                case ANGLE_BASED:
                    double t = 60.0/fajrAngle;
                    return getSunriseTimestamp() - (long)(((double)(getSunriseTimestamp() - getMaghribOfPreviousDayTimestamp()))/t) + (long)(fajrDelay*3600.0*1000.0);
            }
        }
        double hoursInTheDay = getDhuhr() - timeBelowHorizonDifference + fajrDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay);
        return realTime - (realTime%60000l) + 60000l;
    }

    public long getFajrTimestampOfNextDay(){
        double timeBelowHorizonDifference = getTimeBelowHorizonDifference(fajrAngle, time + (1.0/36525.0));
        if(Double.isNaN(timeBelowHorizonDifference)){
            switch (higherLatitudeMode){
                case MIDDLE_OF_THE_NIGHT:
                    return getSunriseOfNextDayTimestamp() - (long)(((double)(getSunriseOfNextDayTimestamp() - getMaghribTimestamp()))/2.0) + (long)(fajrDelay*3600.0*1000.0);
                case SEVENTH:
                    return getSunriseOfNextDayTimestamp() - (long)(((double)(getSunriseOfNextDayTimestamp() - getMaghribTimestamp()))/7.0) + (long)(fajrDelay*3600.0*1000.0);
                case ANGLE_BASED:
                    double t = 60.0/fajrAngle;
                    return getSunriseOfNextDayTimestamp() - (long)(((double)(getSunriseOfNextDayTimestamp() - getMaghribTimestamp()))/t) + (long)(fajrDelay*3600.0*1000.0);
            }
        }
        double hoursInTheDay = getDhuhrOfNextDay() - timeBelowHorizonDifference + fajrDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay) + 24l*3600l*1000l;
        return realTime - (realTime%60000l) + 60000l;
    }

    /**
     * Returns the timestamp of Isha.
     * @return the timestamp of Isha
     */
    public long getIshaTimestamp() {
        double timeBelowHorizonDifference = getTimeBelowHorizonDifference(ishaAngle);

        int[] hijri = DateUtils.getHijriFromJulianDay(DateUtils.dateToJulian(year, month, day));
        double delayToAdd = hijri[1] == 9 ? ishaDelayOnRamadhan : ishaDelayNormal;

        if (Double.isNaN(timeBelowHorizonDifference)) {
            switch (higherLatitudeMode) {
                case MIDDLE_OF_THE_NIGHT:
                    return getMaghribTimestamp() + (long) (((double) (getSunriseOfNextDayTimestamp() - getMaghribTimestamp())) / 2.0) + (long) ((ishaDelay + delayToAdd) * 3600.0 * 1000.0);
                case SEVENTH:
                    return getMaghribTimestamp() + (long) (((double) (getSunriseOfNextDayTimestamp() - getMaghribTimestamp())) / 7.0) + (long) ((ishaDelay + delayToAdd) * 3600.0 * 1000.0);
                case ANGLE_BASED:
                    double t = 60.0 / ishaAngle;
                    return getMaghribTimestamp() + (long) (((double) (getSunriseOfNextDayTimestamp() - getMaghribTimestamp())) / t) + (long) ((ishaDelay + delayToAdd) * 3600.0 * 1000.0);
            }
        }
        double hoursInTheDay = getDhuhr() + timeBelowHorizonDifference + delayToAdd + ishaDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay);
        return realTime - (realTime % 60000l) + 60000l;
    }

    public long getIshaTimestampOfPreviousDay(){
        double timeBelowHorizonDifference = getTimeBelowHorizonDifference(ishaAngle, time - (1.0/36525.0));

        int[] hijri = DateUtils.getHijriFromJulianDay(DateUtils.dateToJulian(year, month, day));
        double delayToAdd = hijri[1] == 9 ? ishaDelayOnRamadhan : ishaDelayNormal;

        if(Double.isNaN(timeBelowHorizonDifference)) {
            switch (higherLatitudeMode){
                case MIDDLE_OF_THE_NIGHT:
                    return getMaghribOfPreviousDayTimestamp() + (long)(((double)(getSunriseTimestamp() - getMaghribOfPreviousDayTimestamp()))/2.0) + (long)((ishaDelay + delayToAdd)*3600.0*1000.0);
                case SEVENTH:
                    return getMaghribOfPreviousDayTimestamp() + (long)(((double)(getSunriseTimestamp() - getMaghribOfPreviousDayTimestamp())) / 7.0) + (long)((ishaDelay + delayToAdd)*3600.0*1000.0);
                case ANGLE_BASED:
                    double t = 60.0/ishaAngle;
                    return getMaghribOfPreviousDayTimestamp() + (long)(((double)(getSunriseTimestamp() - getMaghribOfPreviousDayTimestamp())) / t) + (long)((ishaDelay + delayToAdd)*3600.0*1000.0);
            }
        }
        double hoursInTheDay = getDhuhrOfPreviousDay() + timeBelowHorizonDifference + delayToAdd + ishaDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay) - 24l*3600l*1000l;
        return realTime - (realTime%60000l) + 60000l;
    }

    /**
     * Returns the timestamp of Asr
     * @return the timestamp of Asr
     */
    public long getAsrTimestamp(){
        double hoursInTheDay = getDhuhr() + getTimeShadowSizeDifference(asrTime) + asrDelay;
        long realTime = DateUtils.utcTimeToTimestamp(year, month, day, hoursInTheDay);
        return realTime - (realTime%60000l) + 60000l;
    }

    /**
     * Returns the time difference between dhuhr and the time when the sun is at the given angle
     * @param angle the angle for which to calculate the time
     * @return the time difference between dhuhr and the time when the sun is at the given angle
     */
    public double getTimeBelowHorizonDifference(double angle){
        return getTimeBelowHorizonDifference(angle, time);
    }

    /**
     * Returns the time difference between dhuhr and the time when the sun is at the given angle
     * @param angle the angle for which to calculate the time
     * @param time the time for which to calculate the value
     * @return the time difference between dhuhr and the time when the sun is at the given angle
     */
    public double getTimeBelowHorizonDifference(double angle, double time){
        double topOperand = (-Math.sin(Math.toRadians(angle)))-Math.sin(Math.toRadians(latitude))*Math.sin(Math.toRadians(sunDeclination(time)));
        double bottomOperand = Math.cos(Math.toRadians(latitude))*Math.cos(Math.toRadians(sunDeclination(time)));
        double acos = Math.acos(topOperand / bottomOperand);
        if (Double.isNaN(acos))
            return acos;
        return (1.0/15.0)*Math.toDegrees(acos);
    }

    /**
     * Returns the time difference between dhuhr when the shadow is the given times bigger than the object
     * @param times the number of times the size of the shadow should be the multiple of the size of an object
     * @return the time difference between dhuhr when the shadow is the given times bigger than the object
     */
    public double getTimeShadowSizeDifference(double times) {
        double sunAngleForAsr = -1.0 * Math.toDegrees(MathUtils.acot(times + Math.tan(Math.toRadians(Math.abs(latitude - sunDeclination(time))))));
        return getTimeBelowHorizonDifference(sunAngleForAsr);
    }

    public static HigherLatitudeMode getHigherLatitudeModeFromPreferenceValue(int preferenceValue){
        switch(preferenceValue){
            case ANGLE_BASED_VALUE:
                return HigherLatitudeMode.ANGLE_BASED;
            case SEVENTH_VALUE:
                return HigherLatitudeMode.SEVENTH;
            case MIDDLE_OF_THE_NIGHT_VALUE:
                return HigherLatitudeMode.MIDDLE_OF_THE_NIGHT;
        }
        return null;
    }

    public static School getSchoolFromPreferenceValue(int preferenceValue){
        switch(preferenceValue){
            case HANAFI_VALUE:
                return School.HANAFI;
            case NOT_HANAFI_VALUE:
                return School.NOT_HANAFI;
        }
        return null;
    }

    public static Convention getConventionFromPreferenceValue(int preferenceValue){
        switch(preferenceValue){
            case MUSLIM_WORLD_LEAGUE_VALUE:
                return Convention.MUSLIM_WORLD_LEAGUE;
            case ISLAMIC_SOCIETY_OF_NORTH_AMERICA_VALUE:
                return Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA;
            case EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY_VALUE:
                return Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY;
            case UMM_AL_QURA_UNIVERSITY_MAKKAH_VALUE:
                return Convention.UMM_AL_QURA_UNIVERSITY_MAKKAH;
            case UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI_VALUE:
                return Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI;
            case INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN_VALUE:
                return Convention.INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN;
            case SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM_VALUE:
                return Convention.SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM;
            case MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA_VALUE:
                return Convention.MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA;
            case MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO_VALUE:
                return Convention.MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO;
            case MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA_VALUE:
                return Convention.MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA;
            case SOUTH_EAST_ASIA_VALUE:
                return Convention.SOUTH_EAST_ASIA;
            case UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE_VALUE:
                return Convention.UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE;
            case PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY_VALUE:
                return Convention.PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY;
            case MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN_VALUE:
                return Convention.MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN;
            case GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE_VALUE:
                return Convention.GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE;
            case DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI_VALUE:
                return Convention.DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI;
            case MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN_VALUE:
                return Convention.MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN;
            case MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT_VALUE:
                return Convention.MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT;
            case QATAR_CALENDAR_HOUSE_VALUE:
                return Convention.QATAR_CALENDAR_HOUSE;
            case MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA_VALUE:
                return Convention.MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA;
            case MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES_VALUE:
                return Convention.MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES;
            case BIRMINGHAM_CENTRAL_MOSQUE_VALUE:
                return Convention.BIRMINGHAM_CENTRAL_MOSQUE;
            case LONDON_CENTRAL_MOSQUE_VALUE:
                return Convention.LONDON_CENTRAL_MOSQUE;
            case MUNICH_GERMANY_VALUE:
                return Convention.MUNICH_GERMANY;
            case GRAND_MOSQUE_OF_PARIS_VALUE:
                return Convention.GRAND_MOSQUE_OF_PARIS;
            case ISLAMIC_CENTRE_OF_QUEBEC_VALUE:
                return Convention.ISLAMIC_CENTRE_OF_QUEBEC;
        }
        return null;
    }

    public static int getHigherLatitudeModePreferenceValue(HigherLatitudeMode higherLatitudeMode){
        if(higherLatitudeMode == null)
            return -1;
        switch(higherLatitudeMode){
            case ANGLE_BASED:
                return ANGLE_BASED_VALUE;
            case SEVENTH:
                return SEVENTH_VALUE;
            case MIDDLE_OF_THE_NIGHT:
                return MIDDLE_OF_THE_NIGHT_VALUE;
        }
        return -1;
    }

    public static int getSchoolPreferenceValue(School school){
        if(school == null)
            return -1;
        switch(school){
            case HANAFI:
                return HANAFI_VALUE;
            case NOT_HANAFI:
                return NOT_HANAFI_VALUE;
        }
        return -1;
    }

    public static int getConventionPreferenceValue(Convention convention){
        if(convention == null)
            return -1;
        switch(convention){
            case MUSLIM_WORLD_LEAGUE:
                return MUSLIM_WORLD_LEAGUE_VALUE;
            case ISLAMIC_SOCIETY_OF_NORTH_AMERICA:
                return ISLAMIC_SOCIETY_OF_NORTH_AMERICA_VALUE;
            case EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY:
                return EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY_VALUE;
            case UMM_AL_QURA_UNIVERSITY_MAKKAH:
                return UMM_AL_QURA_UNIVERSITY_MAKKAH_VALUE;
            case UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI:
                return UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI_VALUE;
            case INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN:
                return INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN_VALUE;
            case SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM:
                return SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM_VALUE;
            case MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA:
                return MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA_VALUE;
            case MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO:
                return MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO_VALUE;
            case MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA:
                return MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA_VALUE;
            case SOUTH_EAST_ASIA:
                return SOUTH_EAST_ASIA_VALUE;
            case UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE:
                return UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE_VALUE;
            case PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY:
                return PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY_VALUE;
            case MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN:
                return MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN_VALUE;
            case GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE:
                return GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE_VALUE;
            case DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI:
                return DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI_VALUE;
            case MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN:
                return MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN_VALUE;
            case MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT:
                return MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT_VALUE;
            case QATAR_CALENDAR_HOUSE:
                return QATAR_CALENDAR_HOUSE_VALUE;
            case MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA:
                return MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA_VALUE;
            case MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES:
                return MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES_VALUE;
            case BIRMINGHAM_CENTRAL_MOSQUE:
                return BIRMINGHAM_CENTRAL_MOSQUE_VALUE;
            case LONDON_CENTRAL_MOSQUE:
                return LONDON_CENTRAL_MOSQUE_VALUE;
            case MUNICH_GERMANY:
                return MUNICH_GERMANY_VALUE;
            case GRAND_MOSQUE_OF_PARIS:
                return GRAND_MOSQUE_OF_PARIS_VALUE;
            case ISLAMIC_CENTRE_OF_QUEBEC:
                return ISLAMIC_CENTRE_OF_QUEBEC_VALUE;
        }
        return -1;
    }

    public static int getHigherLatitudeModeResId(HigherLatitudeMode higherLatitudeMode){
        if(higherLatitudeMode == null)
            return -1;
        switch(higherLatitudeMode){
            case ANGLE_BASED:
                return R.string.method_angle_based;
            case SEVENTH:
                return R.string.method_seventh;
            case MIDDLE_OF_THE_NIGHT:
                return R.string.method_middle_of_night;
        }
        return -1;
    }

    public static int getSchoolResId(School school){
        if(school == null)
            return -1;
        switch(school){
            case HANAFI:
                return R.string.asr_hanafi;
            case NOT_HANAFI:
                return R.string.asr_not_hanafi;
        }
        return -1;
    }

    /**
     * Returns the res id of the name of a convention.
     * @param convention the convention from which to get the name
     * @return the res id of the name of a convention
     */
    public static int getConventionNameResId(Convention convention){
        if(convention == null)
            return 0;
        switch (convention){
            case MUSLIM_WORLD_LEAGUE:
                return R.string.convention_name_mwl;
            case ISLAMIC_SOCIETY_OF_NORTH_AMERICA:
                return R.string.convention_name_isna;
            case EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY:
                return R.string.convention_name_egypt;
            case UMM_AL_QURA_UNIVERSITY_MAKKAH:
                return R.string.convention_name_makkah;
            case UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI:
                return R.string.convention_name_karachi;
            case INSTITUTE_OF_GEOPHYSICS_UNIVERSITY_OF_TEHRAN:
                return R.string.convention_name_tehran;
            case SHIA_ITHNA_ASHARI_LEVA_RESEARCH_INSTITUTE_QUM:
                return R.string.convention_name_jafari;
            case DEPARTMENT_OF_ISLAMIC_AFFAIRS_AND_CHARITABLE_ACTIVITIES_DUBAI:
                return R.string.convention_name_dubai;
            case MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA:
                return R.string.convention_name_algeria;
            case MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO:
                return R.string.convention_name_morocco;
            case MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA:
                return R.string.convention_name_tunisia;
            case SOUTH_EAST_ASIA:
                return R.string.convention_name_south_east_asia;
            case UNION_OF_ISLAMIC_ORGANISATIONS_OF_FRANCE:
                return R.string.convention_name_uoif;
            case PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY:
                return R.string.convention_name_turkey;
            case MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN:
                return R.string.convention_name_oman;
            case GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE:
                return R.string.convention_name_emirates;
            case MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN:
                return R.string.convention_name_jordan;
            case MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT:
                return R.string.convention_name_kuwait;
            case QATAR_CALENDAR_HOUSE:
                return R.string.convention_name_qatar;
            case MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA:
                return R.string.convention_name_lybia;
            case MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES:
                return R.string.convention_name_maldives;
            case BIRMINGHAM_CENTRAL_MOSQUE:
                return R.string.convention_name_birmingham;
            case LONDON_CENTRAL_MOSQUE:
                return R.string.convention_name_london;
            case MUNICH_GERMANY:
                return R.string.convention_name_munich;
            case GRAND_MOSQUE_OF_PARIS:
                return R.string.convention_name_paris;
            case ISLAMIC_CENTRE_OF_QUEBEC:
                return R.string.convention_name_quebec;
        }
        return -1;
    }

    /**
     * Calculate the difference between true solar time and mean. The "equation
     * of time" is a term accounting for changes in the time of solar noon for
     * a given location over the course of a year. Earth's elliptical orbit and
     * Kepler's law of equal areas in equal times are the culprits behind this
     * phenomenon. See the
     * <A HREF="http://www.analemma.com/Pages/framesPage.html">Analemma page</A>.
     * Below is a plot of the equation of time versus the day of the year.
     *
     * <P align="center"><img src="doc-files/EquationOfTime.png"></P>
     *
     * @param  t number of Julian centuries since J2000.
     * @return Equation of time in minutes of time.
     */
    private static double equationOfTime(final double t) {
        double eps = Math.toRadians(obliquityCorrected(t));
        double l0  = Math.toRadians(sunGeometricMeanLongitude(t));
        double m   = Math.toRadians(sunGeometricMeanAnomaly(t));
        double e   = eccentricityEarthOrbit(t);
        double y   = Math.tan(eps/2);
        y *= y;

        double sin2l0 = Math.sin(2 * l0);
        double cos2l0 = Math.cos(2 * l0);
        double sin4l0 = Math.sin(4 * l0);
        double sin1m  = Math.sin(m);
        double sin2m  = Math.sin(2 * m);

        double etime = y*sin2l0 - 2*e*sin1m + 4*e*y*sin1m*cos2l0
                - 0.5*y*y*sin4l0 - 1.25*e*e*sin2m;

        return Math.toDegrees(etime)*4.0;
    }

    /**
     * Calculate the Geometric Mean Longitude of the Sun.
     * This value is close to 0 degree at the spring equinox,
     * 90 degree at the summer solstice, 180 degree at the automne equinox
     * and 270 degree at the winter solstice.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Geometric Mean Longitude of the Sun in degrees,
     *         in the range 0 degree (inclusive) to 360 degree (exclusive).
     */
    private static double sunGeometricMeanLongitude(final double t) {
        double L0 = 280.46646 + t*(36000.76983 + 0.0003032*t);
        L0 = L0 - 360*Math.floor(L0/360);
        return L0;
    }

    /**
     * Calculate the mean obliquity of the ecliptic.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Mean obliquity in degrees.
     */
    private static double meanObliquityOfEcliptic(final double t) {
        final double seconds = 21.448 - t*(46.8150 + t*(0.00059 - t*(0.001813)));
        return 23.0 + (26.0 + (seconds/60.0))/60.0;
    }

    /**
     * Calculate the corrected obliquity of the ecliptic.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Corrected obliquity in degrees.
     */
    private static double obliquityCorrected(final double t) {
        final double e0 = meanObliquityOfEcliptic(t);
        final double omega = Math.toRadians(125.04 - 1934.136 * t);
        return e0 + 0.00256 * Math.cos(omega);
    }

    /**
     * Calculate the Geometric Mean Anomaly of the Sun.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Geometric Mean Anomaly of the Sun in degrees.
     */
    private static double sunGeometricMeanAnomaly(final double t) {
        return 357.52911 + t * (35999.05029 - 0.0001537*t);
    }

    /**
     * Calculate the eccentricity of earth's orbit. This is the ratio
     * {@code (a-b)/a} where <var>a</var> is the semi-major axis
     * length and <var>b</var> is the semi-minor axis length.   Value
     * is 0 for a circular orbit.
     *
     * @param  t number of Julian centuries since J2000.
     * @return The unitless eccentricity.
     */
    private static double eccentricityEarthOrbit(final double t) {
        return 0.016708634 - t*(0.000042037 + 0.0000001267*t);
    }

    /**
     * Calculate the declination of the sun. Declination is analogous to latitude
     * on Earth's surface, and measures an angular displacement north or south
     * from the projection of Earth's equator on the celestial sphere to the
     * location of a celestial body.
     *
     * @param t number of Julian centuries since J2000.
     * @return Sun's declination in degrees.
     */
    private static double sunDeclination(final double t) {
        final double e = Math.toRadians(obliquityCorrected(t));
        final double b = Math.toRadians(sunApparentLongitude(t));
        final double sint = Math.sin(e) * Math.sin(b);
        final double theta = Math.asin(sint);
        return Math.toDegrees(theta);
    }

    /**
     * Calculate the true longitude of the sun. This the geometric mean
     * longitude plus a correction factor ("equation of center" for the
     * sun).
     *
     * @param  t number of Julian centuries since J2000.
     * @return Sun's true longitude in degrees.
     */
    private static double sunTrueLongitude(final double t) {
        return sunGeometricMeanLongitude(t) + sunEquationOfCenter(t);
    }

    /**
     * Calculate the apparent longitude of the sun.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Sun's apparent longitude in degrees.
     */
    private static double sunApparentLongitude(final double t) {
        final double omega = Math.toRadians(125.04 - 1934.136 * t);
        return sunTrueLongitude(t) - 0.00569 - 0.00478 * Math.sin(omega);
    }

    /**
     * Calculate the equation of center for the sun. This value is a correction
     * to add to the geometric mean longitude in order to get the "true" longitude
     * of the sun.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Equation of center in degrees.
     */
    private static double sunEquationOfCenter(final double t) {
        final double m = Math.toRadians(sunGeometricMeanAnomaly(t));
        return Math.sin(1*m) * (1.914602 - t*(0.004817 + 0.000014*t)) +
                Math.sin(2*m) * (0.019993 - t*(0.000101             )) +
                Math.sin(3*m) * (0.000289);
    }

    private static class Pair{
        public Convention convention;
        public School school;

        public Pair(Convention convention, School school){
            this.convention = convention;
            this.school = school;
        }

        public Convention getConvention() {
            return convention;
        }

        public School getSchool() {
            return school;
        }
    }

    private static final HashMap<String, Pair> COUNTRY_DEFAULT_CONVENTION;
    static
    {
        COUNTRY_DEFAULT_CONVENTION = new HashMap<>();
        // Andorra
        COUNTRY_DEFAULT_CONVENTION.put("ad", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // United Arab Emirates
        COUNTRY_DEFAULT_CONVENTION.put("ae", new Pair(Convention.GENERAL_AUTHORITY_OF_ISLAMIC_AFFAIRS_AND_ENDOWMENTS_UAE, School.NOT_HANAFI));
        // Afghanistan
        COUNTRY_DEFAULT_CONVENTION.put("af", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Antigua and Barbuda
        COUNTRY_DEFAULT_CONVENTION.put("ag", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Anguilla
        COUNTRY_DEFAULT_CONVENTION.put("ai", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Albania
        COUNTRY_DEFAULT_CONVENTION.put("al", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Armenia
        COUNTRY_DEFAULT_CONVENTION.put("am", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Angola
        COUNTRY_DEFAULT_CONVENTION.put("ao", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Antarctica
        COUNTRY_DEFAULT_CONVENTION.put("aq", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Argentine
        COUNTRY_DEFAULT_CONVENTION.put("ar", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // American Samoa
        COUNTRY_DEFAULT_CONVENTION.put("as", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Austria
        COUNTRY_DEFAULT_CONVENTION.put("at", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Australia
        COUNTRY_DEFAULT_CONVENTION.put("au", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Aruba
        COUNTRY_DEFAULT_CONVENTION.put("aw", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Aland Islands
        COUNTRY_DEFAULT_CONVENTION.put("ax", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Azerbaijan
        COUNTRY_DEFAULT_CONVENTION.put("az", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Bosnia and Herzegovina
        COUNTRY_DEFAULT_CONVENTION.put("ba", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Barbados
        COUNTRY_DEFAULT_CONVENTION.put("bb", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Bangladesh
        COUNTRY_DEFAULT_CONVENTION.put("bd", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Belgium
        COUNTRY_DEFAULT_CONVENTION.put("be", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Burkina Faso
        COUNTRY_DEFAULT_CONVENTION.put("bf", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Bulgaria
        COUNTRY_DEFAULT_CONVENTION.put("bg", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Bahrain
        COUNTRY_DEFAULT_CONVENTION.put("bh", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Burundi
        COUNTRY_DEFAULT_CONVENTION.put("bi", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Benin
        COUNTRY_DEFAULT_CONVENTION.put("bj", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Saint-Barthelemy
        COUNTRY_DEFAULT_CONVENTION.put("bl", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Bermuda
        COUNTRY_DEFAULT_CONVENTION.put("bm", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Brunei Darussalam
        COUNTRY_DEFAULT_CONVENTION.put("bn", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Bolivia
        COUNTRY_DEFAULT_CONVENTION.put("bo", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Bonaire, Sint Eustatius and Saba
        COUNTRY_DEFAULT_CONVENTION.put("bq", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Brazil
        COUNTRY_DEFAULT_CONVENTION.put("br", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Bahamas
        COUNTRY_DEFAULT_CONVENTION.put("bs", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Bhutan
        COUNTRY_DEFAULT_CONVENTION.put("bt", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Bouvet Island
        COUNTRY_DEFAULT_CONVENTION.put("bv", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Botswana
        COUNTRY_DEFAULT_CONVENTION.put("bw", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Belarus
        COUNTRY_DEFAULT_CONVENTION.put("by", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Belize
        COUNTRY_DEFAULT_CONVENTION.put("bz", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Canada
        COUNTRY_DEFAULT_CONVENTION.put("ca", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Cocos Islands
        COUNTRY_DEFAULT_CONVENTION.put("cc", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Democratic Republic of the Congo
        COUNTRY_DEFAULT_CONVENTION.put("cd", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Central African Republic
        COUNTRY_DEFAULT_CONVENTION.put("cf", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Congo
        COUNTRY_DEFAULT_CONVENTION.put("cg", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Switzerland
        COUNTRY_DEFAULT_CONVENTION.put("ch", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Cote d'Ivoire
        COUNTRY_DEFAULT_CONVENTION.put("ci", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Cook Islands
        COUNTRY_DEFAULT_CONVENTION.put("ck", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Chile
        COUNTRY_DEFAULT_CONVENTION.put("cl", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Cameroon
        COUNTRY_DEFAULT_CONVENTION.put("cm", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // China
        COUNTRY_DEFAULT_CONVENTION.put("cn", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Colombia
        COUNTRY_DEFAULT_CONVENTION.put("co", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Costa Rica
        COUNTRY_DEFAULT_CONVENTION.put("cr", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Cuba
        COUNTRY_DEFAULT_CONVENTION.put("cu", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Cabo Verde
        COUNTRY_DEFAULT_CONVENTION.put("cv", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Curacao
        COUNTRY_DEFAULT_CONVENTION.put("cw", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Christmas Islands
        COUNTRY_DEFAULT_CONVENTION.put("cx", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Cyprus
        COUNTRY_DEFAULT_CONVENTION.put("cy", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Czech Republic
        COUNTRY_DEFAULT_CONVENTION.put("cz", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Germany
        COUNTRY_DEFAULT_CONVENTION.put("de", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Djibouti
        COUNTRY_DEFAULT_CONVENTION.put("dj", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Denmark
        COUNTRY_DEFAULT_CONVENTION.put("dk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Dominica
        COUNTRY_DEFAULT_CONVENTION.put("dm", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Dominican Republic
        COUNTRY_DEFAULT_CONVENTION.put("do", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Algeria
        COUNTRY_DEFAULT_CONVENTION.put("dz", new Pair(Convention.MINISTRY_RELIGIOUS_AFFAIRS_AND_WAKFS_ALGERIA, School.NOT_HANAFI));
        // Ecuador
        COUNTRY_DEFAULT_CONVENTION.put("ec", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Estonia
        COUNTRY_DEFAULT_CONVENTION.put("ee", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Egypt
        COUNTRY_DEFAULT_CONVENTION.put("eg", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Western Sahara
        COUNTRY_DEFAULT_CONVENTION.put("eh", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Eritrea
        COUNTRY_DEFAULT_CONVENTION.put("er", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Spain
        COUNTRY_DEFAULT_CONVENTION.put("es", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Ethiopia
        COUNTRY_DEFAULT_CONVENTION.put("et", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Finland
        COUNTRY_DEFAULT_CONVENTION.put("fi", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Fiji
        COUNTRY_DEFAULT_CONVENTION.put("fj", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Falkland Islands (Malvinas)
        COUNTRY_DEFAULT_CONVENTION.put("fk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Micronesia
        COUNTRY_DEFAULT_CONVENTION.put("fm", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Faroe Islands
        COUNTRY_DEFAULT_CONVENTION.put("fo", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // France
        COUNTRY_DEFAULT_CONVENTION.put("fr", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Gabon
        COUNTRY_DEFAULT_CONVENTION.put("ga", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // United Kingdom
        COUNTRY_DEFAULT_CONVENTION.put("gb", new Pair(Convention.BIRMINGHAM_CENTRAL_MOSQUE, School.HANAFI));
        // Grenada
        COUNTRY_DEFAULT_CONVENTION.put("gd", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Georgia
        COUNTRY_DEFAULT_CONVENTION.put("ge", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // French Guiana
        COUNTRY_DEFAULT_CONVENTION.put("gf", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Guernsey
        COUNTRY_DEFAULT_CONVENTION.put("gg", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Ghana
        COUNTRY_DEFAULT_CONVENTION.put("gh", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Gibraltar
        COUNTRY_DEFAULT_CONVENTION.put("gi", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Greenland
        COUNTRY_DEFAULT_CONVENTION.put("gl", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Gambia
        COUNTRY_DEFAULT_CONVENTION.put("gm", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Guinea
        COUNTRY_DEFAULT_CONVENTION.put("gn", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Guadeloupe
        COUNTRY_DEFAULT_CONVENTION.put("gp", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Equatorial Guinea
        COUNTRY_DEFAULT_CONVENTION.put("gq", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Greece
        COUNTRY_DEFAULT_CONVENTION.put("gr", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // South Georgia and the South Sandwich Islands
        COUNTRY_DEFAULT_CONVENTION.put("gs", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Guatemala
        COUNTRY_DEFAULT_CONVENTION.put("gt", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Guam
        COUNTRY_DEFAULT_CONVENTION.put("gu", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Guinea-Bissau
        COUNTRY_DEFAULT_CONVENTION.put("gw", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Guyana
        COUNTRY_DEFAULT_CONVENTION.put("gy", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Hong Kong
        COUNTRY_DEFAULT_CONVENTION.put("hk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Heard and MacDonald Islands
        COUNTRY_DEFAULT_CONVENTION.put("hm", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Honduras
        COUNTRY_DEFAULT_CONVENTION.put("hn", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Croatia
        COUNTRY_DEFAULT_CONVENTION.put("hr", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Haiti
        COUNTRY_DEFAULT_CONVENTION.put("ht", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Hungary
        COUNTRY_DEFAULT_CONVENTION.put("hu", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Indonesia
        COUNTRY_DEFAULT_CONVENTION.put("id", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Ireland
        COUNTRY_DEFAULT_CONVENTION.put("ie", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Israel
        COUNTRY_DEFAULT_CONVENTION.put("il", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Isle of Man
        COUNTRY_DEFAULT_CONVENTION.put("im", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // India
        COUNTRY_DEFAULT_CONVENTION.put("in", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.NOT_HANAFI));
        // British Indian Ocean Territory
        COUNTRY_DEFAULT_CONVENTION.put("io", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Iraq
        COUNTRY_DEFAULT_CONVENTION.put("iq", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Iran
        COUNTRY_DEFAULT_CONVENTION.put("ir", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Iceland
        COUNTRY_DEFAULT_CONVENTION.put("is", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Italia
        COUNTRY_DEFAULT_CONVENTION.put("it", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Jersey
        COUNTRY_DEFAULT_CONVENTION.put("je", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Jamaica
        COUNTRY_DEFAULT_CONVENTION.put("jm", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Jordan
        COUNTRY_DEFAULT_CONVENTION.put("jo", new Pair(Convention.MINISTRY_OF_AWQAF_ISLAMIC_AFFAIRS_AND_HOLY_PLACES_JORDAN, School.NOT_HANAFI));
        // Japan
        COUNTRY_DEFAULT_CONVENTION.put("jp", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Kenya
        COUNTRY_DEFAULT_CONVENTION.put("ke", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Kyrgyzstan
        COUNTRY_DEFAULT_CONVENTION.put("kg", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Cambodia
        COUNTRY_DEFAULT_CONVENTION.put("kh", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Kiribati
        COUNTRY_DEFAULT_CONVENTION.put("ki", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Comoros
        COUNTRY_DEFAULT_CONVENTION.put("km", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Saint Kitts and Nevis
        COUNTRY_DEFAULT_CONVENTION.put("kn", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // North Korea
        COUNTRY_DEFAULT_CONVENTION.put("kp", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // South Korea
        COUNTRY_DEFAULT_CONVENTION.put("kr", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Kuwait
        COUNTRY_DEFAULT_CONVENTION.put("kw", new Pair(Convention.MINISTRY_OF_AWQAF_AND_ISLAMIC_AFFAIRS_KUWAIT, School.NOT_HANAFI));
        // Cayman Islands
        COUNTRY_DEFAULT_CONVENTION.put("ky", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Kazakhstan
        COUNTRY_DEFAULT_CONVENTION.put("kz", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Laos
        COUNTRY_DEFAULT_CONVENTION.put("la", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Lebanon
        COUNTRY_DEFAULT_CONVENTION.put("lb", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Saint Lucia
        COUNTRY_DEFAULT_CONVENTION.put("lc", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Liechtenstein
        COUNTRY_DEFAULT_CONVENTION.put("li", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        COUNTRY_DEFAULT_CONVENTION.put("lk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Liberia
        COUNTRY_DEFAULT_CONVENTION.put("lr", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Lesotho
        COUNTRY_DEFAULT_CONVENTION.put("ls", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Lithuania
        COUNTRY_DEFAULT_CONVENTION.put("lt", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Luxembourg
        COUNTRY_DEFAULT_CONVENTION.put("lu", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Latvia
        COUNTRY_DEFAULT_CONVENTION.put("lv", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Lybia
        COUNTRY_DEFAULT_CONVENTION.put("ly", new Pair(Convention.MINISTRY_OF_ENDOWMENTS_AND_ISLAMIC_AFFAIRS_LYBIA, School.NOT_HANAFI));
        // Morocco
        COUNTRY_DEFAULT_CONVENTION.put("ma", new Pair(Convention.MINISTRY_HABOUS_AND_ISLAMIC_AFFAIRS_MOROCCO, School.NOT_HANAFI));
        // Monaco
        COUNTRY_DEFAULT_CONVENTION.put("mc", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Moldova
        COUNTRY_DEFAULT_CONVENTION.put("md", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Montenegro
        COUNTRY_DEFAULT_CONVENTION.put("me", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Saint Martin (French part)
        COUNTRY_DEFAULT_CONVENTION.put("mf", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Madagascar
        COUNTRY_DEFAULT_CONVENTION.put("mg", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Marshall Islands
        COUNTRY_DEFAULT_CONVENTION.put("mh", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Macedonia
        COUNTRY_DEFAULT_CONVENTION.put("mk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Mali
        COUNTRY_DEFAULT_CONVENTION.put("ml", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Myanmar
        COUNTRY_DEFAULT_CONVENTION.put("mm", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Mongolia
        COUNTRY_DEFAULT_CONVENTION.put("mn", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Macao
        COUNTRY_DEFAULT_CONVENTION.put("mo", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Northern Mariana Islands
        COUNTRY_DEFAULT_CONVENTION.put("mp", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Martinique
        COUNTRY_DEFAULT_CONVENTION.put("mq", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Mauritania
        COUNTRY_DEFAULT_CONVENTION.put("mr", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Montserrat
        COUNTRY_DEFAULT_CONVENTION.put("ms", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Malta
        COUNTRY_DEFAULT_CONVENTION.put("mt", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Mauritius
        COUNTRY_DEFAULT_CONVENTION.put("mu", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Maldives
        COUNTRY_DEFAULT_CONVENTION.put("mv", new Pair(Convention.MINISTRY_OF_ISLAMIC_AFFAIRS_MALDIVES, School.NOT_HANAFI));
        // Malawi
        COUNTRY_DEFAULT_CONVENTION.put("mw", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Mexico
        COUNTRY_DEFAULT_CONVENTION.put("mx", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Malaysia
        COUNTRY_DEFAULT_CONVENTION.put("my", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Mozambique
        COUNTRY_DEFAULT_CONVENTION.put("mz", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Namibia
        COUNTRY_DEFAULT_CONVENTION.put("na", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // New Caledonia
        COUNTRY_DEFAULT_CONVENTION.put("nc", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Niger
        COUNTRY_DEFAULT_CONVENTION.put("ne", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Norfolk
        COUNTRY_DEFAULT_CONVENTION.put("nf", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Nigeria
        COUNTRY_DEFAULT_CONVENTION.put("ng", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Nicaragua
        COUNTRY_DEFAULT_CONVENTION.put("ni", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Netherlands
        COUNTRY_DEFAULT_CONVENTION.put("nl", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Norway
        COUNTRY_DEFAULT_CONVENTION.put("no", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Nepal
        COUNTRY_DEFAULT_CONVENTION.put("np", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Nauru
        COUNTRY_DEFAULT_CONVENTION.put("nr", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Niue
        COUNTRY_DEFAULT_CONVENTION.put("nu", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // New Zealand
        COUNTRY_DEFAULT_CONVENTION.put("nz", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Oman
        COUNTRY_DEFAULT_CONVENTION.put("om", new Pair(Convention.MINISTRY_OF_ENDOWMENTS_AND_RELIGIOUS_AFFAIRS_OMAN, School.NOT_HANAFI));
        // Panama
        COUNTRY_DEFAULT_CONVENTION.put("pa", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Peru
        COUNTRY_DEFAULT_CONVENTION.put("pe", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // French Polynesia
        COUNTRY_DEFAULT_CONVENTION.put("pf", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Papua New Guinea
        COUNTRY_DEFAULT_CONVENTION.put("pg", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Philippines
        COUNTRY_DEFAULT_CONVENTION.put("ph", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Pakistan
        COUNTRY_DEFAULT_CONVENTION.put("pk", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Poland
        COUNTRY_DEFAULT_CONVENTION.put("pl", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Saint Pierre and Miquelon
        COUNTRY_DEFAULT_CONVENTION.put("pm", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Pitcairn
        COUNTRY_DEFAULT_CONVENTION.put("pn", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Puerto Rico
        COUNTRY_DEFAULT_CONVENTION.put("pr", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Palestine
        COUNTRY_DEFAULT_CONVENTION.put("ps", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Portugal
        COUNTRY_DEFAULT_CONVENTION.put("pt", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Palau
        COUNTRY_DEFAULT_CONVENTION.put("pw", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Paraguay
        COUNTRY_DEFAULT_CONVENTION.put("py", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Qatar
        COUNTRY_DEFAULT_CONVENTION.put("qa", new Pair(Convention.QATAR_CALENDAR_HOUSE, School.NOT_HANAFI));
        // Reunion
        COUNTRY_DEFAULT_CONVENTION.put("re", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Romania
        COUNTRY_DEFAULT_CONVENTION.put("ro", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Serbia
        COUNTRY_DEFAULT_CONVENTION.put("rs", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Russia
        COUNTRY_DEFAULT_CONVENTION.put("ru", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Rwanda
        COUNTRY_DEFAULT_CONVENTION.put("rw", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Saudi Arabia
        COUNTRY_DEFAULT_CONVENTION.put("sa", new Pair(Convention.UMM_AL_QURA_UNIVERSITY_MAKKAH, School.NOT_HANAFI));
        // Solomon Islands
        COUNTRY_DEFAULT_CONVENTION.put("sb", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Seychelles
        COUNTRY_DEFAULT_CONVENTION.put("sc", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Sudan
        COUNTRY_DEFAULT_CONVENTION.put("sd", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Sweden
        COUNTRY_DEFAULT_CONVENTION.put("se", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Singapore
        COUNTRY_DEFAULT_CONVENTION.put("sg", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Saint Helena, Ascension and Tristan da Cunha
        COUNTRY_DEFAULT_CONVENTION.put("sh", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Slovenia
        COUNTRY_DEFAULT_CONVENTION.put("si", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Svalbard and Jan Mayen
        COUNTRY_DEFAULT_CONVENTION.put("sj", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Slovakia
        COUNTRY_DEFAULT_CONVENTION.put("sk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Sierra Leone
        COUNTRY_DEFAULT_CONVENTION.put("sl", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // San Marino
        COUNTRY_DEFAULT_CONVENTION.put("sm", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Senegal
        COUNTRY_DEFAULT_CONVENTION.put("sn", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Somalia
        COUNTRY_DEFAULT_CONVENTION.put("so", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Suriname
        COUNTRY_DEFAULT_CONVENTION.put("sr", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // South Sudan
        COUNTRY_DEFAULT_CONVENTION.put("ss", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Sao Tome and Principe
        COUNTRY_DEFAULT_CONVENTION.put("st", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // El Salvador
        COUNTRY_DEFAULT_CONVENTION.put("sv", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Sint Maarten (Dutch part)
        COUNTRY_DEFAULT_CONVENTION.put("sx", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Syria
        COUNTRY_DEFAULT_CONVENTION.put("sy", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Swaziland
        COUNTRY_DEFAULT_CONVENTION.put("sz", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Turks and Caicos Islands
        COUNTRY_DEFAULT_CONVENTION.put("tc", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Chad
        COUNTRY_DEFAULT_CONVENTION.put("td", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // French Southern Territories
        COUNTRY_DEFAULT_CONVENTION.put("tf", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Togo
        COUNTRY_DEFAULT_CONVENTION.put("tg", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Thailand
        COUNTRY_DEFAULT_CONVENTION.put("th", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Tajikistan
        COUNTRY_DEFAULT_CONVENTION.put("tj", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Tokelau
        COUNTRY_DEFAULT_CONVENTION.put("tk", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Timor-Leste
        COUNTRY_DEFAULT_CONVENTION.put("tl", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Turkmenistan
        COUNTRY_DEFAULT_CONVENTION.put("tm", new Pair(Convention.UNIVERSITY_OF_ISLAMIC_SCIENCES_KARACHI, School.HANAFI));
        // Tunisia
        COUNTRY_DEFAULT_CONVENTION.put("tn", new Pair(Convention.MINISTRY_RELIGIOUS_AFFAIRS_TUNISIA, School.NOT_HANAFI));
        // Tonga
        COUNTRY_DEFAULT_CONVENTION.put("to", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Turkey
        COUNTRY_DEFAULT_CONVENTION.put("tr", new Pair(Convention.PRESIDENCY_OF_RELIGIOUS_AFFAIRS_TURKEY, School.NOT_HANAFI));
        // Trinidad and Tobago
        COUNTRY_DEFAULT_CONVENTION.put("tt", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Tuvalu
        COUNTRY_DEFAULT_CONVENTION.put("tv", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Taiwan
        COUNTRY_DEFAULT_CONVENTION.put("tw", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Tanzania
        COUNTRY_DEFAULT_CONVENTION.put("tz", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Ukraine
        COUNTRY_DEFAULT_CONVENTION.put("ua", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Uganda
        COUNTRY_DEFAULT_CONVENTION.put("ug", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // United States Minor Outlying Islands
        COUNTRY_DEFAULT_CONVENTION.put("um", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // United States
        COUNTRY_DEFAULT_CONVENTION.put("us", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Uruguay
        COUNTRY_DEFAULT_CONVENTION.put("uy", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Uzbekistan
        COUNTRY_DEFAULT_CONVENTION.put("uz", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Holy See
        COUNTRY_DEFAULT_CONVENTION.put("va", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Saint Vincent and the Grenadines
        COUNTRY_DEFAULT_CONVENTION.put("vc", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Venezuela
        COUNTRY_DEFAULT_CONVENTION.put("ve", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Virgin Islands (British)
        COUNTRY_DEFAULT_CONVENTION.put("vg", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Virgin Islands (U.S.)
        COUNTRY_DEFAULT_CONVENTION.put("vi", new Pair(Convention.ISLAMIC_SOCIETY_OF_NORTH_AMERICA, School.NOT_HANAFI));
        // Viet Nam
        COUNTRY_DEFAULT_CONVENTION.put("vn", new Pair(Convention.SOUTH_EAST_ASIA, School.NOT_HANAFI));
        // Vanuatu
        COUNTRY_DEFAULT_CONVENTION.put("vu", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Wallis and Futuna
        COUNTRY_DEFAULT_CONVENTION.put("wf", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Samoa
        COUNTRY_DEFAULT_CONVENTION.put("ws", new Pair(Convention.MUSLIM_WORLD_LEAGUE, School.NOT_HANAFI));
        // Yemen
        COUNTRY_DEFAULT_CONVENTION.put("ye", new Pair(Convention.UMM_AL_QURA_UNIVERSITY_MAKKAH, School.NOT_HANAFI));
        // Mayotte
        COUNTRY_DEFAULT_CONVENTION.put("yt", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // South Africa
        COUNTRY_DEFAULT_CONVENTION.put("za", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Zambia
        COUNTRY_DEFAULT_CONVENTION.put("zm", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
        // Zimbabwe
        COUNTRY_DEFAULT_CONVENTION.put("zw", new Pair(Convention.EGYPTIAN_GENERAL_AUTHORITY_OF_SURVEY, School.NOT_HANAFI));
    }
}
