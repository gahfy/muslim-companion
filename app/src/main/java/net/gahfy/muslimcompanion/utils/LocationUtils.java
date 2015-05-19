package net.gahfy.muslimcompanion.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;

/**
 * This class is a library of useful methods for location
 * @author Gahfy
 */
public class LocationUtils {
    public static final double KAABA_LATITUDE = 21.422491;
    public static final double KAABA_LONGITUDE = 39.826209;

    public static final int GPS_ENABLED = 1;
    public static final int NETWORK_ENABLED = 2;
    public static final int NOT_PASSIVE_ENABLED = 3;
    public static final int PASSIVE_ENABLED = 4;
    public static final int NOT_NETWORK_ENABLED = 5;
    public static final int NOT_GPS_ENABLED = 6;

    public static Location getLastKnownLocation(LocationManager locationManager){
        Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        Location passiveLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if(gpsLocation != null){
            return gpsLocation;
        }
        else if(networkLocation != null){
            return networkLocation;
        }
        else if(passiveLocation != null){
            return passiveLocation;
        }
        return null;
    }

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

    /**
     * Returns the bearing from one point to the Kaaba.
     * @param pointLatitude The latitude of the point from which to get the bearing
     * @param pointLongitude The longitude of the point from which to get the bearing
     * @return the bearing from one point to the Kaaba
     */
    public static double bearingToKaaba(double pointLatitude, double pointLongitude){
        return bearingTo(pointLatitude, pointLongitude, KAABA_LATITUDE, KAABA_LONGITUDE);
    }

    /**
     * Returns the bearing from one point to another.
     * @param latFrom The latitude of the point from
     * @param lonFrom The longitude of the point from
     * @param latTo The latitude of the point to
     * @param lonTo The longitude of the point to
     * @return the bearing from one point to another
     */
    private static double bearingTo(double latFrom, double lonFrom, double latTo, double lonTo){
        double longitude1 = lonFrom;
        double longitude2 = lonTo;
        double latitude1 = Math.toRadians(latFrom);
        double latitude2 = Math.toRadians(latTo);
        double longDiff= Math.toRadians(longitude2-longitude1);
        double y= Math.sin(longDiff)*Math.cos(latitude2);
        double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x))+360)%360;
    }

    /**
     * Returns the ISO code of the current country
     * @param context Context in which the application is running
     * @return the ISO code of the current country, or null if not found
     */
    @Nullable
    public static String getCountryIso(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager != null)
            return telephonyManager.getNetworkCountryIso().toLowerCase();
        return null;
    }
}