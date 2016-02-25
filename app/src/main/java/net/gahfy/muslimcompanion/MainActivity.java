package net.gahfy.muslimcompanion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import net.gahfy.muslimcompanion.fragment.AbstractFragment;
import net.gahfy.muslimcompanion.fragment.CompassFragment;
import net.gahfy.muslimcompanion.fragment.ConventionListFragment;
import net.gahfy.muslimcompanion.fragment.HigherLatitudeModeListFragment;
import net.gahfy.muslimcompanion.fragment.JumuahDelayListFragment;
import net.gahfy.muslimcompanion.fragment.NotificationSoundListFragment;
import net.gahfy.muslimcompanion.fragment.PrayerTimeFragment;
import net.gahfy.muslimcompanion.fragment.SchoolListFragment;
import net.gahfy.muslimcompanion.fragment.SettingsFragment;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int REQUEST_CODE_PERMISSION_FINE_LOCATION = 1;

    private AlertDialog locationDisabledDialog;

    /** The current Fragment */
    private AbstractFragment currentFragment;

    /** The Geolocating layout */
    private LinearLayout lytGeolocatingContainer;

    private LinearLayout lytLocationRefusedLayout;

    /** The Menu container */
    private RelativeLayout lytIcMenuContainer;

    /** The drawer layout */
    private DrawerLayout drawerLayout;

    /** The scrollview with the navigation drawer */
    private ScrollView scrollDrawerView;

    private TextView lbllocationRationale;

    /** The Location Manager of the activity */
    private LocationManager locationManager;

    /** The current location of the Activity */
    private MuslimLocation currentLocation;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        initMembers();
        initToolbar();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            setTitle(R.string.app_name);
            redirectToFragment(new PrayerTimeFragment(), false);
        }
    }

    public void initMembers() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        lytLocationRefusedLayout = (LinearLayout) findViewById(R.id.lyt_location_refused_container);
        lytIcMenuContainer = (RelativeLayout) findViewById(R.id.lyt_ic_menu_container);
        lytGeolocatingContainer = (LinearLayout) findViewById(R.id.lyt_geolocating_container);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        scrollDrawerView = (ScrollView) findViewById(R.id.scroll_drawer_view);
        lbllocationRationale = (TextView) findViewById(R.id.lbl_location_rationale);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationProvidersStatus = LocationUtils.getLocationProvidersStatus(this);
    }

    public void initToolbar() {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        lytIcMenuContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(scrollDrawerView);
            }
        });
    }

    public void redirectToNotificationSoundList() {
        redirectToFragment(new NotificationSoundListFragment(), true);
    }

    public void redirectToConventionList() {
        redirectToFragment(new ConventionListFragment(), true);
    }

    public void redirectToJumuahDelayList() {
        redirectToFragment(new JumuahDelayListFragment(), true);
    }

    public void redirectToHigherLatitudeList() {
        redirectToFragment(new HigherLatitudeModeListFragment(), true);
    }

    public void redirectToSchoolList() {
        redirectToFragment(new SchoolListFragment(), true);
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView lblGeolocating = (TextView) findViewById(R.id.lbl_geolocating);
        TextView lblMenuSettings = (TextView) findViewById(R.id.lbl_menu_settings);
        TextView lblMenuPrayerTimes = (TextView) findViewById(R.id.lbl_menu_prayer_time);
        TextView lblMenuQibla = (TextView) findViewById(R.id.lbl_menu_qibla);
        //TextView lblMenuQuran = (TextView) findViewById(R.id.lbl_menu_quran);
        TextView lblLocationNeeded = (TextView) findViewById(R.id.lbl_location_needed);
        Button btLocationPermission = (Button) findViewById(R.id.bt_location_permission);

        ViewUtils.setTypefaceToTextView(this, lblGeolocating, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(this, lblMenuSettings, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(this, lblMenuPrayerTimes, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(this, lblMenuQibla, ViewUtils.FONT_WEIGHT.MEDIUM);
        //ViewUtils.setTypefaceToTextView(this, lblMenuQuran, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(this, lblLocationNeeded, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(this, lbllocationRationale, ViewUtils.FONT_WEIGHT.REGULAR);
        ViewUtils.setTypefaceToTextView(this, btLocationPermission, ViewUtils.FONT_WEIGHT.MEDIUM);

        btLocationPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermission(true);
            }
        });
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


        /*findViewById(R.id.lyt_menu_quran_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new SuraFragment(), false);
            }
        });*/
    }

    @Override
    public void setTitle(int titleResId) {
        TextView textViewTitle = (TextView) findViewById(R.id.toolbar_title);
        if (textViewTitle != null) {
            textViewTitle.setText(titleResId);
            ViewUtils.setTypefaceToTextView(this, textViewTitle, ViewUtils.FONT_WEIGHT.TOOLBAR_TITLE);
        }
    }

    public void setTitle(String title) {
        TextView textViewTitle = (TextView) findViewById(R.id.toolbar_title);
        if (textViewTitle != null) {
            textViewTitle.setText(title);
            ViewUtils.setTypefaceToTextView(this, textViewTitle, ViewUtils.FONT_WEIGHT.TOOLBAR_TITLE);
        }
    }

    public void showToolbar() {
        findViewById(R.id.lyt_status_bar_container).setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
    }

    public void setSubTitle(String subtitle) {
        TextView textViewSubTitle = (TextView) findViewById(R.id.toolbar_subtitle);
        if (subtitle != null) {
            textViewSubTitle.setVisibility(View.VISIBLE);
            textViewSubTitle.setText(subtitle);
            ViewUtils.setTypefaceToTextView(this, textViewSubTitle, ViewUtils.FONT_WEIGHT.REGULAR_ITALIC);
        } else {
            textViewSubTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        switchOffLocationListeners();
    }

    public MuslimLocation getCurrentLocation() {
        return this.currentLocation;
    }

    public void switchOnLocationListeners() {
        if (!isGeolocationWorking && checkLocationPermission(false)) {
            isGeolocationWorking = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            } catch (IllegalArgumentException e) {
                //TODO: Handle error
            }
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO)
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
        if(isGeolocationWorking && checkLocationPermission(false)) {
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
                .beginTransaction();
        fragmentTransaction.replace(R.id.lyt_fragment_container, currentFragment);

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
        lytLocationRefusedLayout.setVisibility(View.GONE);
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
            lytLocationRefusedLayout.setVisibility(View.GONE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    switchOnLocationListeners();
                } else {
                    lytLocationRefusedLayout.setVisibility(View.VISIBLE);
                    if(currentFragment.getLocationDetailsTextResId() != 0)
                        lbllocationRationale.setText(currentFragment.getLocationDetailsTextResId());
                }
            }
        }
    }

    private void restoreState(Bundle savedInstanceState){
        if(!savedInstanceState.getBoolean("isCurrentLocationNull")){
            String latitude = savedInstanceState.getString("currentLocationLatitude");
            String longitude = savedInstanceState.getString("currentLocationLongitude");

            currentLocation = new MuslimLocation(
                    Double.valueOf(latitude != null ? latitude : "0.0"),
                    Double.valueOf(longitude != null ? longitude : "0.0"),
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


    private boolean checkLocationPermission(boolean fromButton){
        return checkLocationPermission(fromButton, true);
    }

    private boolean checkLocationPermission(boolean fromButton, boolean displayDialog){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) && !fromButton && displayDialog) {
                lytLocationRefusedLayout.setVisibility(View.VISIBLE);
                if(currentFragment.getLocationDetailsTextResId() != 0)
                    lbllocationRationale.setText(currentFragment.getLocationDetailsTextResId());
            } else if(displayDialog){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION_FINE_LOCATION);
            }
            return false;
        }
        lytLocationRefusedLayout.setVisibility(View.GONE);
        return true;
    }
}
