package net.gahfy.muslimcompanion.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * This class is a library of useful methods for location
 * @author Gahfy
 */
public class LocationUtils {
    public static final int GPS_ENABLED = 1;
    public static final int NETWORK_ENABLED = 2;
    public static final int NOT_PASSIVE_ENABLED = 3;
    public static final int PASSIVE_ENABLED = 4;
    public static final int NOT_NETWORK_ENABLED = 5;
    public static final int NOT_GPS_ENABLED = 6;

    /**
     * Returns the current status of location providers.
     * @param context Context in which the application is running
     * @return the current status of location providers
     */
    public static int getLocationProvidersStatus(Context context) {
        int currentStatus = 0;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean passive_enabled = lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

        if (gps_enabled) {
            currentStatus = currentStatus | GPS_ENABLED;
        }
        if (network_enabled) {
            currentStatus = currentStatus | NETWORK_ENABLED;
        }
        if (passive_enabled) {
            currentStatus = currentStatus | PASSIVE_ENABLED;
        }

        return currentStatus;
    }
}