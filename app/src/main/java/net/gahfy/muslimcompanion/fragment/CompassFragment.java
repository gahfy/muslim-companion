package net.gahfy.muslimcompanion.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Outline;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

import java.util.Date;

/**
 * The fragment with the Compass and the Qibla
 * @author Gahfy
 */
public class CompassFragment extends AbstractFragment {
    /** The alert dialog shown to the user for requiring him to enable location providers */
    private AlertDialog locationDisabledDialog;

    /** The location manager of the fragment */
    private LocationManager locationManager;

    /** Whether the location listener is on (true) or off (false) */
    private boolean isLocationListenerEnabled = false;

    /** The location in use for the Fragment */
    private Location location;

    /**
     * The status (enabled/disabled) of location listeners
     * @see net.gahfy.muslimcompanion.utils.LocationUtils
     */
    int locationProvidersStatus = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_compass, container, false);

        TextView lblGeolocating = (TextView) resultView.findViewById(R.id.lbl_geolocating);
        TextView lblQibla = (TextView) resultView.findViewById(R.id.lbl_qibla);
        TextView lblAngle = (TextView) resultView.findViewById(R.id.lbl_angle);

        ViewUtils.setTypefaceToTextView(getMainActivity(), lblGeolocating, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblQibla, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblAngle, ViewUtils.FONT_WEIGHT.MEDIUM);

        lblAngle.setText(getMainActivity().getString(R.string.angle, 35));
        return resultView;
    }

    @Override
    public void onStart(){
        super.onStart();

        locationProvidersStatus = LocationUtils.getLocationProvidersStatus(getMainActivity());
        if(locationManager == null)
            locationManager = (LocationManager) getMainActivity().getSystemService(Context.LOCATION_SERVICE);

        checkLastKnownLocation();
        if(location != null){
            manageFoundLocation();
        }
        else {
            checkLocationProviders();
            enableLocationListeners();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        disableLocationListeners();
    }

    /**
     * Checks whether location providers are on or off, and displays alert if there are off.
     */
    public void checkLocationProviders(){

        if(locationDisabledDialog != null)
            if(locationDisabledDialog.isShowing())
                locationDisabledDialog.dismiss();

        if((locationProvidersStatus & LocationUtils.GPS_ENABLED) == 0 && (locationProvidersStatus & LocationUtils.NETWORK_ENABLED) == 0)
            showLocationDisabledAlert();
    }

    /**
     * Called when a location is found
     */
    public void manageFoundLocation(){

    }

    /**
     * Checks the last known locations
     */
    public void checkLastKnownLocation(){
        Location lastKnownGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownNetworkLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(isLocationGoodEnough(lastKnownGpsLocation)){
            location = lastKnownGpsLocation;
        }
        else if(isLocationGoodEnough(lastKnownNetworkLocation)){
            location = lastKnownNetworkLocation;
        }
    }

    /**
     * Enables location listeners
     */
    public void enableLocationListeners(){
        if(!isLocationListenerEnabled) {
            isLocationListenerEnabled = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            }
            catch(IllegalArgumentException e){

            }
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
        }
    }

    /**
     * Disable location listeners
     */
    public void disableLocationListeners(){
        if(isLocationListenerEnabled){
            isLocationListenerEnabled = false;
            locationManager.removeUpdates(locationListener);
        }
    }

    /**
     * Shows the location providers required alert
     */
    public void showLocationDisabledAlert(){
        if(locationDisabledDialog != null){
            if(locationDisabledDialog.isShowing())
                locationDisabledDialog.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getMainActivity());

        builder.setMessage(R.string.location_required_message)
                .setTitle(R.string.location_required_title)
                .setPositiveButton(R.string.location_go_to_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationDisabledDialog.dismiss();
                    }
                });
        locationDisabledDialog = builder.create();

        locationDisabledDialog.show();
    }

    /**
     * The location listener of the fragment
     */
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            CompassFragment.this.location = location;
            disableLocationListeners();
            manageFoundLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras){

        }

        public void onProviderEnabled(String provider) {
            switch(provider){
                case LocationManager.GPS_PROVIDER:
                    locationProvidersStatus = locationProvidersStatus | LocationUtils.GPS_ENABLED;
                    break;
                case LocationManager.NETWORK_PROVIDER:
                    locationProvidersStatus = locationProvidersStatus | LocationUtils.NETWORK_ENABLED;
                    break;
                case LocationManager.PASSIVE_PROVIDER:
                    locationProvidersStatus = locationProvidersStatus | LocationUtils.PASSIVE_ENABLED;
                    break;
            }
            checkLocationProviders();
        }

        public void onProviderDisabled(String provider) {
            switch(provider){
                case LocationManager.GPS_PROVIDER:
                    locationProvidersStatus = locationProvidersStatus & LocationUtils.NOT_GPS_ENABLED;
                    break;
                case LocationManager.NETWORK_PROVIDER:
                    locationProvidersStatus = locationProvidersStatus & LocationUtils.NOT_NETWORK_ENABLED;
                    break;
                case LocationManager.PASSIVE_PROVIDER:
                    locationProvidersStatus = locationProvidersStatus & LocationUtils.NOT_PASSIVE_ENABLED;
                    break;
            }
            checkLocationProviders();
        }
    };

    /**
     * Returns whether a location is good for this fragment or not.
     * @param location the location to check
     * @return whether a location is good for this fragment or not
     */
    public static boolean isLocationGoodEnough(Location location){
        return location != null && location.getTime()+3600000l >= new Date().getTime();
    }
}
