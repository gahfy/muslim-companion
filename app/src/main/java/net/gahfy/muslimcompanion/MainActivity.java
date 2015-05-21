package net.gahfy.muslimcompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.gahfy.muslimcompanion.fragment.AbstractFragment;
import net.gahfy.muslimcompanion.fragment.CompassFragment;
import net.gahfy.muslimcompanion.fragment.ConventionListFragment;
import net.gahfy.muslimcompanion.fragment.HigherLatitudeModeListFragment;
import net.gahfy.muslimcompanion.fragment.PrayerTimeFragment;
import net.gahfy.muslimcompanion.fragment.SchoolListFragment;
import net.gahfy.muslimcompanion.fragment.SettingsFragment;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;
import java.util.Date;

public class MainActivity extends ActionBarActivity implements LocationListener{
    private AlertDialog locationDisabledDialog;

    /** The Category for analytics event about listeners */
    private static final String CATEGORY_LISTENER = "Listener";

    /** The Category for analytics event about Database */
    private static final String CATEGORY_DATABASE = "Database";

    /** The Action for analytics about compass */
    private static final String ACTION_COMPASS = "Compass";

    /** The Action for analytics about location */
    private static final String ACTION_LOCATION = "Location";

    /** The Action for analytics about City */
    private static final String ACTION_CITY = "City";

    /** The Label for analytics when compass is switched on */
    private static final String LABEL_COMPASS_ON = "CompassOn";

    /** The Label for analytics when compass has an error */
    private static final String LABEL_COMPASS_ERROR = "CompassError";

    /** The Label for analytics when location is found */
    private static final String LABEL_LOCATION_FOUND = "LocationFound";

    /** The Label for analytics when user leave before finding location */
    private static final String LABEL_LOCATION_LEAVE = "LocationLeave";

    /** The Label for analytics when retrieving the city */
    private static final String LABEL_CITY_FOUND = "CityFound";

    /** The Label for analytics when there is an error retrieving the city */
    private static final String LABEL_CITY_ERROR = "CityError";

    /** The current Fragment */
    private AbstractFragment currentFragment;

    /** The Geolocating layout */
    private LinearLayout lytGeolocatingContainer;

    /** The Menu container */
    private RelativeLayout lytIcMenuContainer;

    /** The drawer layout */
    private DrawerLayout drawerLayout;

    /** The scrollview with the navigation drawer */
    private ScrollView scrollDrawerView;

    /** The Google Analytics Tracker */
    private Tracker analyticsTracker;

    /** The Location Manager of the activity */
    private LocationManager locationManager;

    /** The current location of the Activity */
    private MuslimLocation currentLocation;

    /** The microtime when the location is switched on */
    private long locationStartTime;

    /** Whether location listeners are on or not */
    private boolean isGeolocationWorking = false;

    /** The toolbar of the activity */
    private Toolbar toolbar;

    /**
     * The status (enabled/disabled) of location listeners
     * @see net.gahfy.muslimcompanion.utils.LocationUtils
     */
    int locationProvidersStatus = 0;

    boolean shouldDisplayAlert = false;

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

        this.setContentView(R.layout.activity_main);

        initMembers();
        initToolbar();

        if(savedInstanceState != null){
            restoreState(savedInstanceState);
        }
        else {
            setTitle(R.string.app_name);
            redirectToFragment(new PrayerTimeFragment(), false);
        }
    }

    public void initMembers(){
        analyticsTracker = ((MuslimCompanionApplication) getApplication()).getTracker(MuslimCompanionApplication.TrackerName.GLOBAL_TRACKER);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        lytIcMenuContainer = (RelativeLayout) findViewById(R.id.lyt_ic_menu_container);
        lytGeolocatingContainer = (LinearLayout) findViewById(R.id.lyt_geolocating_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        scrollDrawerView = (ScrollView) findViewById(R.id.scroll_drawer_view);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvidersStatus = LocationUtils.getLocationProvidersStatus(this);
    }

    public void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        lytIcMenuContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(scrollDrawerView);
            }
        });
    }

    public void redirectToConventionList(){
        redirectToFragment(new ConventionListFragment(), true);
    }

    public void redirectToHigherLatitudeList(){
        redirectToFragment(new HigherLatitudeModeListFragment(), true);
    }

    public void redirectToSchoolList(){
        redirectToFragment(new SchoolListFragment(), true);
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView lblGeolocating = (TextView) findViewById(R.id.lbl_geolocating);
        TextView lblMenuSettings = (TextView) findViewById(R.id.lbl_menu_settings);
        TextView lblMenuPrayerTimes = (TextView) findViewById(R.id.lbl_menu_prayer_time);
        TextView lblMenuQibla = (TextView) findViewById(R.id.lbl_menu_qibla);

        ViewUtils.setTypefaceToTextView(this, lblGeolocating, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(this, lblMenuSettings, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(this, lblMenuPrayerTimes, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(this, lblMenuQibla, ViewUtils.FONT_WEIGHT.MEDIUM);

        findViewById(R.id.lyt_menu_settings_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new SettingsFragment());
            }
        });

        findViewById(R.id.lyt_menu_prayer_time_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new PrayerTimeFragment(), false);
            }
        });

        findViewById(R.id.lyt_menu_qibla_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new CompassFragment(), false);
            }
        });
    }

    @Override
    public void setTitle(int titleResId){
        TextView textViewTitle = (TextView) findViewById(R.id.toolbar_title);
        if(textViewTitle != null){
            textViewTitle.setText(titleResId);
            ViewUtils.setTypefaceToTextView(this, textViewTitle, ViewUtils.FONT_WEIGHT.TOOLBAR_TITLE);
        }
    }

    public void setTitle(String title){
        TextView textViewTitle = (TextView) findViewById(R.id.toolbar_title);
        if(textViewTitle != null){
            textViewTitle.setText(title);
            ViewUtils.setTypefaceToTextView(this, textViewTitle, ViewUtils.FONT_WEIGHT.TOOLBAR_TITLE);
        }
    }

    public void hideToolbar(){
        findViewById(R.id.lyt_status_bar_container).setBackgroundColor(getResources().getColor(R.color.white_93));
        findViewById(R.id.toolbar).setVisibility(View.GONE);
    }

    public void showToolbar(){
        findViewById(R.id.lyt_status_bar_container).setBackgroundColor(getResources().getColor(R.color.primary));
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
    }

    public void setSubTitle(int subtitleResId){
        TextView textViewSubTitle = (TextView) findViewById(R.id.toolbar_subtitle);
        if(textViewSubTitle != null){
            textViewSubTitle.setText(subtitleResId);
            ViewUtils.setTypefaceToTextView(this, textViewSubTitle, ViewUtils.FONT_WEIGHT.REGULAR_ITALIC);
        }
    }

    public void setSubTitle(String subtitle){
        TextView textViewSubTitle = (TextView) findViewById(R.id.toolbar_subtitle);
        if(subtitle != null) {
            textViewSubTitle.setVisibility(View.VISIBLE);
            if (textViewSubTitle != null) {
                textViewSubTitle.setText(subtitle);
                ViewUtils.setTypefaceToTextView(this, textViewSubTitle, ViewUtils.FONT_WEIGHT.REGULAR_ITALIC);
            }
        }
        else{
            textViewSubTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(isGeolocationWorking && currentLocation == null){
            sendLocationLeaveEvent(new Date().getTime() - locationStartTime);
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

        if(shouldDisplayAlert && (locationProvidersStatus & LocationUtils.GPS_ENABLED) == 0 && (locationProvidersStatus & LocationUtils.NETWORK_ENABLED) == 0) {
            showLocationDisabledAlert();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.location_required_message)
                .setTitle(R.string.location_required_title)
                .setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
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
        redirectToFragment(fragment, true);
    }

    private void redirectToFragment(AbstractFragment fragment, boolean addToBackStack){
        showToolbar();
        this.setSubTitle(null);

        if(drawerLayout.isDrawerOpen(scrollDrawerView))
            drawerLayout.closeDrawers();
        currentFragment = fragment;

        switch(currentFragment.getGeolocationTypeNeeded()) {
            case NONE:
                manageNoGeolocationNeeded();
                break;
            case ONCE:
                manageOnceGeolocationNeeded(true);
                break;
            case CONTINUOUS:
                break;
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lyt_fragment_container, currentFragment);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        else{
            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void manageNoGeolocationNeeded(){
        lytGeolocatingContainer.setVisibility(View.GONE);
        MuslimLocation lastKnownMuslimLocation = getLastMuslimLocation();
        if(lastKnownMuslimLocation != null) {
            if(lastKnownMuslimLocation.getLocationTime() + (SharedPreferencesUtils.getLocationValidityTime(this)*1000) > new Date().getTime()) {
                switchOffLocationListeners();
            }
        }
    }

    public void manageOnceGeolocationNeeded(boolean communicateToFragment){
        MuslimLocation lastKnownMuslimLocation = getLastMuslimLocation();
        if(lastKnownMuslimLocation != null){
            currentLocation = lastKnownMuslimLocation;
            lytGeolocatingContainer.setVisibility(View.GONE);
            if(communicateToFragment)
                currentFragment.onLocationChanged(currentLocation);
            if(lastKnownMuslimLocation.getLocationTime() + (SharedPreferencesUtils.getLocationValidityTime(this)*1000) < new Date().getTime()) {
                switchOnLocationListeners();
            }
            else{
                switchOffLocationListeners();
            }
        }
        else{
            shouldDisplayAlert = true;
            switchOnLocationListeners();
        }
    }

    public MuslimLocation getLastMuslimLocation(){
        return SharedPreferencesUtils.getLastLocation(this);
    }

    public void sendCityFoundEvent(long timeTaken){
        sendAnalyticsEvent(CATEGORY_DATABASE, ACTION_CITY, LABEL_CITY_FOUND, timeTaken);
    }

    public void sendCityErrorEvent(){
        sendAnalyticsEvent(CATEGORY_DATABASE, ACTION_CITY, LABEL_CITY_ERROR);
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

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putBoolean("isCurrentLocationNull", currentLocation == null);
        if(currentLocation != null){
            outState.putString("currentLocationLatitude", String.valueOf(currentLocation.getLocationLatitude()));
            outState.putString("currentLocationLongitude", String.valueOf(currentLocation.getLocationLongitude()));
            outState.putLong("currentLocationTime", currentLocation.getLocationTime());
            outState.putInt("currentLocationMode", currentLocation.getLocationModeInt());
        }

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    private void restoreState(Bundle savedInstanceState){
        if(!savedInstanceState.getBoolean("isCurrentLocationNull")){
            currentLocation = new MuslimLocation(
                    Double.valueOf(savedInstanceState.getString("currentLocationLatitude")),
                    Double.valueOf(savedInstanceState.getString("currentLocationLongitude")),
                    savedInstanceState.getLong("currentLocationTime"),
                    savedInstanceState.getInt("currentLocationMode")
            );
        }

        currentFragment = (AbstractFragment) getSupportFragmentManager().findFragmentById(R.id.lyt_fragment_container);

        switch(currentFragment.getGeolocationTypeNeeded()) {
            case NONE:
                manageNoGeolocationNeeded();
                break;
            case ONCE:
                manageOnceGeolocationNeeded(false);
                break;
            case CONTINUOUS:
                break;
        }
    }
}
