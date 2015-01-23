package net.gahfy.muslimcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.gahfy.muslimcompanion.fragment.AbstractFragment;
import net.gahfy.muslimcompanion.fragment.CompassFragment;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

import java.util.Date;

public class MainActivity extends ActionBarActivity implements LocationListener{
    private AlertDialog locationDisabledDialog;

    /** The Category for analytics event about listeners */
    private static final String CATEGORY_LISTENER = "Listener";

    /** The Action for analytics about compass */
    private static final String ACTION_COMPASS = "Compass";

    /** The Action for analytics about location */
    private static final String ACTION_LOCATION = "Location";

    /** The Label for analytics when compass is switched on */
    private static final String LABEL_COMPASS_ON = "CompassOn";

    /** The Label for analytics when compass has an error */
    private static final String LABEL_COMPASS_ERROR = "CompassError";

    /** The Label for analytics when location is found */
    private static final String LABEL_LOCATION_FOUND = "LocationFound";

    /** The Label for analytics when user leave before finding location */
    private static final String LABEL_LOCATION_LEAVE = "LocationLeave";

    /** The current Fragment */
    private AbstractFragment currentFragment;

    /** The Geolocating TextView */
    private TextView lblGeolocating;

    /** The Geolocating layout */
    private LinearLayout lytGeolocatingContainer;

    /** The Google Analytics Tracker */
    private Tracker analyticsTracker;

    /** The toolbar */
    private Toolbar toolbar;

    /** Whether location listeners are on or not */
    private boolean isGeolocationWorking = false;

    /** The Location Manager of the activity */
    private LocationManager locationManager;

    /** The current location of the Activity */
    private MuslimLocation currentLocation;

    /** The microtime when the location is switched on */
    private long locationStartTime;

    /**
     * The status (enabled/disabled) of location listeners
     * @see net.gahfy.muslimcompanion.utils.LocationUtils
     */
    int locationProvidersStatus = 0;

    /**
     * Returns the Google Analytics Tracker
     * @return the Google Analytics Tracker
     */
    public Tracker getAnalyticsTracker(){
        return analyticsTracker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        analyticsTracker = ((MuslimCompanionApplication) getApplication()).getTracker(MuslimCompanionApplication.TrackerName.GLOBAL_TRACKER);

        this.setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        lblGeolocating = (TextView) findViewById(R.id.lbl_geolocating);
        lytGeolocatingContainer = (LinearLayout) findViewById(R.id.lyt_geolocating_container);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvidersStatus = LocationUtils.getLocationProvidersStatus(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setTitle(R.string.app_name);

        ViewUtils.setTypefaceToTextView(this, lblGeolocating, ViewUtils.FONT_WEIGHT.LIGHT);

        redirectToFragment(new CompassFragment());
    }

    @Override
    public void setTitle(int titleResId){
        TextView textViewTitle = (TextView) findViewById(R.id.toolbar_title);
        if(textViewTitle != null){
            textViewTitle.setText(titleResId);
            ViewUtils.setTypefaceToTextView(this, textViewTitle, ViewUtils.FONT_WEIGHT.MEDIUM);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(isGeolocationWorking && currentLocation == null){
            sendLocationFoundEvent(new Date().getTime() - locationStartTime);
        }
        switchOffLocationListeners();
    }

    public MuslimLocation getCurrentLocation(){
        return this.currentLocation;
    }

    public void switchOnLocationListeners(){
        if(!isGeolocationWorking) {
            locationStartTime = new Date().getTime();
            isGeolocationWorking = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
        }
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
     * Shows the location providers required alert
     */
    public void showLocationDisabledAlert(){
        if(locationDisabledDialog != null){
            if(locationDisabledDialog.isShowing())
                locationDisabledDialog.dismiss();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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

    public void switchOffLocationListeners(){
        if(isGeolocationWorking) {
            isGeolocationWorking = false;
            locationManager.removeUpdates(this);
        }
    }

    private void redirectToFragment(AbstractFragment fragment){
        currentFragment = fragment;

        switch(currentFragment.getGeolocationTypeNeeded()) {
            case NONE:
                manageNoGeolocationNeeded();
                break;
            case ONCE:
                manageOnceGeolocationNeeded();
                break;
            case CONTINUOUS:
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lyt_fragment_container, currentFragment)
                .commitAllowingStateLoss();
    }

    public void manageNoGeolocationNeeded(){
        lytGeolocatingContainer.setVisibility(View.GONE);

        MuslimLocation lastKnownMuslimLocation = getLastValidMuslimLocation();
        if(lastKnownMuslimLocation != null)
            switchOffLocationListeners();
    }

    public void manageOnceGeolocationNeeded(){
        MuslimLocation lastKnownMuslimLocation = getLastValidMuslimLocation();
        if(lastKnownMuslimLocation != null){
            currentLocation = lastKnownMuslimLocation;
            lytGeolocatingContainer.setVisibility(View.GONE);
            switchOffLocationListeners();
            currentFragment.onLocationChanged(currentLocation);
        }
        else{
            switchOnLocationListeners();
        }
    }

    public MuslimLocation getLastValidMuslimLocation(){
        MuslimLocation muslimLocation = SharedPreferencesUtils.getLastLocation(this);
        long muslimLocationValidityTime = SharedPreferencesUtils.getLocationValidityTime(this);
        if(muslimLocation != null && muslimLocation.getLocationTime() + muslimLocationValidityTime*1000 > new Date().getTime())
            return muslimLocation;
        return null;
    }

    public void sendLocationFoundEvent(long timeTaken){
        sendAnalyticsEvent(CATEGORY_LISTENER, ACTION_LOCATION, LABEL_LOCATION_FOUND, timeTaken);
    }

    public void sendLocationLeaveEvent(long afterTime){
        sendAnalyticsEvent(CATEGORY_LISTENER, ACTION_LOCATION, LABEL_LOCATION_LEAVE, afterTime);
    }

    public void sendCompassOnEvent(){
        sendAnalyticsEvent(CATEGORY_LISTENER, ACTION_COMPASS, LABEL_COMPASS_ON);
    }

    public void sendCompassErrorEvent(){
        sendAnalyticsEvent(CATEGORY_LISTENER, ACTION_COMPASS, LABEL_COMPASS_ERROR);
    }

    private void sendAnalyticsEvent(String category, String action, String label){
        if(getAnalyticsTracker() != null) {
            getAnalyticsTracker().send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .build());
        }
    }

    private void sendAnalyticsEvent(String category, String action, String label, long value){
        if(getAnalyticsTracker() != null) {
            getAnalyticsTracker().send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .setLabel(label)
                    .setValue(value)
                    .build());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        switch(currentFragment.getGeolocationTypeNeeded()) {
            case NONE:
            case ONCE:
                lytGeolocatingContainer.setVisibility(View.GONE);
                switchOffLocationListeners();
                currentLocation = new MuslimLocation(
                        location.getLatitude(),
                        location.getLongitude(),
                        location.getTime(),
                        MuslimLocation.MODE.MODE_PROVIDER
                );
                SharedPreferencesUtils.putLastLocation(this, currentLocation);
                currentFragment.onLocationChanged(currentLocation);
                sendLocationFoundEvent(new Date().getTime() - locationStartTime);
                break;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
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

    @Override
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
}
