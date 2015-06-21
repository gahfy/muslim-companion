package net.gahfy.muslimcompanion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import net.gahfy.muslimcompanion.models.MuslimLocation;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SharedPreferencesUtils{
    /** The name of the shared preferences file */
    private static final String PREFERENCES_NAME = "muslimCompanionPrefs";

    /** The name of the key for whether there is a saved location or not */
    private static final String HAS_SAVED_LOCATION = "hasSavedLocation";

    /** The name of the key for the latitude of the saved location */
    private static final String LOCATION_LATITUDE = "locationLatitude";

    /** The name of the key for the longitude of the saved location */
    private static final String LOCATION_LONGITUDE = "locationLongitude";

    /** The name of the key for the microtime when the saved location has been performed */
    private static final String LOCATION_TIME = "locationTime";

    /** The name of the key for the mode (manual or from provider) of the saved location */
    private static final String LOCATION_MODE = "locationMode";

    /** The name of the key for the validity time of a location */
    private static final String LOCATION_VALIDITY_TIME = "locationValidityTime";

    /** The convention that has been saved */
    private static final String CONVENTION = "convention";

    private static final String SCHOOL = "school";

    private static final String HIGHER_LATITUDE_MODE = "higherLatitudeMode";

    private static final String CONVENTION_AUTOMATIC = "conventionIsAutomatic";

    private static final String SCHOOL_AUTOMATIC = "schoolIsAutomatic";

    private static final String HIGHER_LATITUDE_MODE_AUTOMATIC = "higherLatitudeModeIsAutomatic";

    private static final String LAST_USAGE = "lastUsage";

    private static final String TIME_LAST_NOTIFICATION_PRAYER = "timeLastNotificationPrayer";

    private static final String NOTIFY_PRAYER = "notifyPrayer";

    private static final String SOUND_NOTIFICATION_PRAYER = "soundNotificationPrayer";

    private static final String JUMUAH_FIRST_CALL = "jumuah_first_call";

    private static final String JUMUAH_FIRST_CALL_DELAY = "jumuah_first_call_delay";

    private static final String DATABASE_VERSION = "database_version";

    /**
     * Saves a MuslimLocation as the last location in Shared Preferences.
     * @param context Context in which the application is running
     * @param location The location to save
     */
    public static void putLastLocation(Context context, MuslimLocation location){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HAS_SAVED_LOCATION, location != null);
        if(location != null){
            editor.putString(LOCATION_LATITUDE, String.valueOf(location.getLocationLatitude()));
            editor.putString(LOCATION_LONGITUDE, String.valueOf(location.getLocationLongitude()));
            editor.putLong(LOCATION_TIME, location.getLocationTime());
            editor.putInt(LOCATION_MODE, location.getLocationMode() == MuslimLocation.MODE.MODE_PROVIDER ? 0 : 1);
            if(location.getLocationMode() == MuslimLocation.MODE.MODE_MANUAL){
                editor.putLong(LOCATION_VALIDITY_TIME, -1);
            }
            else{
                if(getLocationValidityTime(context) < 0){
                    editor.putLong(LOCATION_VALIDITY_TIME, 3600);
                }
            }
        }
        editor.commit();
    }

    /**
     * Returns the last MuslimLocation (may be null) that has been saved in Shared Preferences.
     * @param context Context in which the application is running
     * @return the last MuslimLocation (may be null) that has been saved in Shared Preferences
     */
    public static MuslimLocation getLastLocation(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(HAS_SAVED_LOCATION, false)){
            double locationLatitude = Double.valueOf(sharedPreferences.getString(LOCATION_LATITUDE, "200.0"));
            double locationLongitude = Double.valueOf(sharedPreferences.getString(LOCATION_LONGITUDE, "200.0"));
            long locationTime = sharedPreferences.getLong(LOCATION_TIME, 0l);
            MuslimLocation.MODE locationMode = (sharedPreferences.getInt(LOCATION_MODE, 0) == 0) ? MuslimLocation.MODE.MODE_PROVIDER : MuslimLocation.MODE.MODE_MANUAL;

            return new MuslimLocation(locationLatitude, locationLongitude, locationTime, locationMode);
        }
        return null;
    }

    public static void putJumuahFirstCallDelay(Context context, int jumuahFirstCallDelay){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(JUMUAH_FIRST_CALL_DELAY, jumuahFirstCallDelay);
        editor.commit();
    }

    public static int getDatabaseVersion(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(DATABASE_VERSION, 0);
    }

    public static void putDatabaseVersion(Context context, int databaseVersion){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DATABASE_VERSION, databaseVersion);
        editor.commit();
    }

    public static int getJumuahFirstCallDelay(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(JUMUAH_FIRST_CALL_DELAY, 30);
    }

    public static void putJumuahFirstCallEnabled(Context context, boolean isJumuahFirstCallEnabled){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(JUMUAH_FIRST_CALL, isJumuahFirstCallEnabled);
        editor.commit();
    }

    public static boolean getJumuahFirstCallEnabled(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(JUMUAH_FIRST_CALL, true);
    }

    public static void putLastNotificationPrayer(Context context, long lastNotificationPrayer){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_LAST_NOTIFICATION_PRAYER, lastNotificationPrayer);
        editor.commit();
    }

    public static long getLastNotificationPrayer(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(TIME_LAST_NOTIFICATION_PRAYER, -1);
    }

    public static void putNotifyPrayer(Context context, boolean notifyPrayer){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NOTIFY_PRAYER, notifyPrayer);
        editor.commit();
    }

    public static boolean getNotifyPrayer(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(NOTIFY_PRAYER, true);
    }

    public static void putSoundNotificationPrayer(Context context, int soundNotificationPrayer){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SOUND_NOTIFICATION_PRAYER, soundNotificationPrayer);
        editor.commit();
    }

    public static int getSoundNotificationPrayer(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SOUND_NOTIFICATION_PRAYER, 1);
    }

    public static void putHigherLatitudeMode(Context context, int higherLatitudeModeValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(HIGHER_LATITUDE_MODE, higherLatitudeModeValue);
        editor.commit();
    }

    public static int getHigherLatitudeModeValue(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(HIGHER_LATITUDE_MODE, -1);
    }

    public static boolean saveUsageandGetHasUsedYesterday(Context context){
        long lastUsage = getLastUsage(context);
        boolean shouldSave = false;
        boolean hasUsedYesterday = false;

        if(lastUsage != -1){
            GregorianCalendar currentDayCalendar = new GregorianCalendar();
            currentDayCalendar.setTimeInMillis(new Date().getTime());

            currentDayCalendar.set(Calendar.HOUR, 0);
            currentDayCalendar.set(Calendar.MINUTE, 0);
            currentDayCalendar.set(Calendar.SECOND, 0);
            currentDayCalendar.set(Calendar.MILLISECOND, 0);

            long todayMidnightDate = currentDayCalendar.getTimeInMillis();

            if(lastUsage < todayMidnightDate){
                shouldSave = true;
                hasUsedYesterday = lastUsage > (todayMidnightDate-86400l*1000l);
            }
        }
        else{
            shouldSave = true;
        }

        if(shouldSave){
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(LAST_USAGE, new Date().getTime());
            editor.commit();
        }
        return hasUsedYesterday;
    }

    public static long getLastUsage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LAST_USAGE, -1);
    }

    public static void putHigherLatitudeModeIsAutomatic(Context context, boolean higherLatitudeModeIsAutomatic){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(HIGHER_LATITUDE_MODE_AUTOMATIC, higherLatitudeModeIsAutomatic);
        editor.commit();
    }

    public static boolean getHigherLatitudeModeIsAutomatic(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(HIGHER_LATITUDE_MODE_AUTOMATIC, true);
    }

    public static void putSchool(Context context, int schoolValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SCHOOL, schoolValue);
        editor.commit();
    }

    public static int getSchoolValue(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SCHOOL, -1);
    }

    public static void putSchoolIsAutomatic(Context context, boolean higherLatitudeModeIsAutomatic){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SCHOOL_AUTOMATIC, higherLatitudeModeIsAutomatic);
        editor.commit();
    }

    public static boolean getSchoolIsAutomatic(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SCHOOL_AUTOMATIC, true);
    }

    public static void putConvention(Context context, int conventionValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CONVENTION, conventionValue);
        editor.commit();
    }

    public static int getConventionValue(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CONVENTION, -1);
    }

    public static void putConventionIsAutomatic(Context context, boolean higherLatitudeModeIsAutomatic){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CONVENTION_AUTOMATIC, higherLatitudeModeIsAutomatic);
        editor.commit();
    }

    public static boolean getConventionIsAutomatic(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CONVENTION_AUTOMATIC, true);
    }

    public static void putLocationValidityDate(Context context, long locationValidityTime){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(LOCATION_VALIDITY_TIME, locationValidityTime);
        editor.commit();
    }

    public static long getLocationValidityTime(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LOCATION_VALIDITY_TIME, -2l);
    }
}
