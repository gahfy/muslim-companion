package net.gahfy.muslimcompanion.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;
import net.gahfy.muslimcompanion.view.CompassArrowView;

import java.util.Date;
import java.util.Locale;

/**
 * The fragment with the Compass and the Qibla
 * @author Gahfy
 */
public class CompassFragment extends AbstractFragment implements LocationListener, SensorEventListener{

    /** The latitude in use for this Fragment */
    private double currentLatitude = 300.0;
    /** The longitude in use for this Fragment */
    private double currentLongitude = 300.0;

    /** The alert dialog shown to the user for requiring him to enable location providers */
    private AlertDialog locationDisabledDialog;

    /** The layout that contains the compass with the text of the Qibla */
    private RelativeLayout lytCompassContainer;

    /** The layout that contains the geolocating text with the progress bar */
    private LinearLayout lytGeolocatingContainer;

    private TextView lblQibla;

    /** The TextView on which the Angle of the Qibla is written */
    private TextView lblAngle;

    /** The ImageView that contains the arrow with the direction of the Qibla */
    private CompassArrowView imgCompassArrowDirection;


    /** Whether the location listener is on (true) or off (false) */
    private boolean isLocationListenerEnabled = false;

    /** Whether the compass is currently animating or not */
    private boolean isCompassAnimating = false;
    /** Whether the compass is currently working or not */
    private boolean isCompassWorking = false;

    /** The Location manager of the fragment */
    private LocationManager locationManager;

    /** The Sensor manager of the fragment */
    private SensorManager mSensorManager;

    /** The sensor accelerometer of the fragment */
    private Sensor mAccelerometer;
    /** The sensor magnetometer of the fragment */
    private Sensor mMagnetometer;

    /** The last data of the sensor accelerometer */
    private float[] mLastAccelerometer = new float[3];
    /** The last data of the sensor magnetometer */
    private float[] mLastMagnetometer = new float[3];
    /** The rotation matrix of the sensor event listener */
    private float[] mR = new float[9];
    /** The orientation of the sensor event listener */
    private float[] mOrientation = new float[3];

    /** Whether the data of the sensor accelerometer has been set at least one time */
    private boolean mLastAccelerometerSet = false;
    /** Whether the data of the sensor magnetometer has been set at least one time */
    private boolean mLastMagnetometerSet = false;

    /** The current rotation in degrees of the compass */
    private float mCurrentDegree = 0f;

    /** The current display of the screen */
    private Display mDisplay;

    /** The time in ms when the user started Geolocation */
    private long geolocationStartTime;

    /** The view of the fragment */
    private View fragmentView;

    /**
     * The status (enabled/disabled) of location listeners
     * @see net.gahfy.muslimcompanion.utils.LocationUtils
     */
    int locationProvidersStatus = 0;

    float fragmentWidth = 0f;
    float fragmentHeight = 0f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_compass, container, false);

        WindowManager mWindowManager = (WindowManager) getMainActivity().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) getMainActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        lytCompassContainer = (RelativeLayout) fragmentView.findViewById(R.id.lyt_compass_container);
        lytGeolocatingContainer = (LinearLayout) fragmentView.findViewById(R.id.lyt_geolocating_container);

        TextView lblGeolocating = (TextView) fragmentView.findViewById(R.id.lbl_geolocating);
        lblQibla = (TextView) fragmentView.findViewById(R.id.lbl_qibla);
        lblAngle = (TextView) fragmentView.findViewById(R.id.lbl_angle);

        imgCompassArrowDirection = (CompassArrowView) fragmentView.findViewById(R.id.img_compass_arrow_direction);

        ViewUtils.setTypefaceToTextView(getMainActivity(), lblGeolocating, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblQibla, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblAngle, ViewUtils.FONT_WEIGHT.MEDIUM);
        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(layoutObserver);

        lytCompassContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(CompassFragment.class.getSimpleName(), String.format(Locale.US, "CompassContainer width: %d / height: %d", lytCompassContainer.getMeasuredWidth(), lytCompassContainer.getMeasuredHeight()));
            }
        });
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            currentLatitude = savedInstanceState.getDouble("latitude");
            currentLongitude = savedInstanceState.getDouble("longitude");
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        // Send a screen view.
        getMainActivity().activityTracker.send(new HitBuilders.AppViewBuilder().build());

        if(currentLatitude < 300.0) {
            manageFoundLocation();
        }
        else{
            lytCompassContainer.setVisibility(View.GONE);

            locationProvidersStatus = LocationUtils.getLocationProvidersStatus(getMainActivity());
            if (locationManager == null)
                locationManager = (LocationManager) getMainActivity().getSystemService(Context.LOCATION_SERVICE);

            checkLastKnownLocation();

            if (currentLatitude < 300.0) {
                manageFoundLocation();
            } else {
                checkLocationProviders();
                enableLocationListeners();
            }
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        disableLocationListeners();
        if(isCompassWorking){
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
            isCompassWorking = false;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble("latitude", currentLatitude);
        outState.putDouble("longitude", currentLongitude);
    }

    @Override
    public void onLocationChanged(Location location) {
        CompassFragment.this.currentLatitude = location.getLatitude();
        CompassFragment.this.currentLongitude = location.getLongitude();


        getMainActivity().activityTracker.send(new HitBuilders.TimingBuilder()
                .setCategory("Listener")
                .setValue(new Date().getTime() - geolocationStartTime)
                .setVariable("Time to geolocate")
                .setLabel("Geolocation")
                .build());
        disableLocationListeners();
        manageFoundLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){

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
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            float[] temp = exponentialSmoothing(mLastAccelerometer, event.values, 0.5f);
            System.arraycopy(temp, 0, mLastAccelerometer, 0, temp.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            float[] temp = exponentialSmoothing(mLastMagnetometer, event.values, 0.8f);
            System.arraycopy(temp, 0, mLastMagnetometer, 0, temp.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegrees = (float)(Math.toDegrees(azimuthInRadians)+360);

            switch(mDisplay.getRotation()){
                case Surface.ROTATION_90:
                    azimuthInDegrees += 90;
                    break;
                case Surface.ROTATION_180:
                    azimuthInDegrees += 180;
                    break;
                case Surface.ROTATION_270:
                    azimuthInDegrees -= 90;
                    break;
            }
            azimuthInDegrees = azimuthInDegrees%360;

            if(Math.abs(-azimuthInDegrees-mCurrentDegree) >= 180f){
                if(-azimuthInDegrees < mCurrentDegree)
                    azimuthInDegrees -= 360f;
                else
                    azimuthInDegrees += 360f;
            }

            if(!isCompassAnimating) {
                isCompassAnimating = true;
                RotateAnimation ra = new RotateAnimation(
                        mCurrentDegree,
                        -azimuthInDegrees,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                ra.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        isCompassAnimating = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                ra.setDuration(250);

                ra.setFillAfter(true);

                if(getView() != null)
                    getView().findViewById(R.id.lyt_compass).startAnimation(ra);
                mCurrentDegree = -azimuthInDegrees;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
        int kaabaBearing = (int) LocationUtils.bearingToKaaba(currentLatitude, currentLongitude);
        lblAngle.setText(getMainActivity().getString(R.string.angle, kaabaBearing));

        Animation an = new RotateAnimation(0.0f, kaabaBearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        an.setDuration(0);
        an.setRepeatCount(0);
        an.setRepeatMode(Animation.REVERSE);
        an.setFillAfter(true);

        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if(mAccelerometer != null && mMagnetometer != null) {
                    isCompassWorking = true;
                    mSensorManager.registerListener(CompassFragment.this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
                    mSensorManager.registerListener(CompassFragment.this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        // Aply animation to image view
        imgCompassArrowDirection.startAnimation(an);

        ViewUtils.crossFadeAnimation(getMainActivity(), lytCompassContainer, lytGeolocatingContainer);
    }



    /**
     * Checks the last known locations
     */
    public void checkLastKnownLocation(){
        Location lastKnownGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location lastKnownNetworkLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(isLocationGoodEnough(lastKnownGpsLocation)){
            currentLatitude = lastKnownGpsLocation.getLatitude();
            currentLongitude = lastKnownGpsLocation.getLongitude();
        }
        else if(isLocationGoodEnough(lastKnownNetworkLocation)){
            currentLatitude = lastKnownNetworkLocation.getLatitude();
            currentLongitude = lastKnownNetworkLocation.getLongitude();
        }
    }

    /**
     * Enables location listeners
     */
    public void enableLocationListeners(){
        if(!isLocationListenerEnabled) {
            isLocationListenerEnabled = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
            catch(IllegalArgumentException e){
                e.printStackTrace();
            }
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);

            geolocationStartTime = new Date().getTime();
        }
    }

    /**
     * Disable location listeners
     */
    public void disableLocationListeners(){
        if(isLocationListenerEnabled){
            isLocationListenerEnabled = false;
            locationManager.removeUpdates(this);
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
     * Returns whether a location is good for this fragment or not.
     * @param location the location to check
     * @return whether a location is good for this fragment or not
     */
    public static boolean isLocationGoodEnough(Location location){
        return location != null && location.getTime()+3600000l >= new Date().getTime();
    }

    /**
     * Calculate the exponentialSmoothing for a smoother rotation of the compass
     * @param input the previous values
     * @param output the gross values to go to
     * @param alpha The coefficient of smoothness
     * @return the array with exponential smoothing applied on it
     * @link http://en.wikipedia.org/wiki/Exponential_smoothing
     */
    private float[] exponentialSmoothing( float[] input, float[] output, float alpha ) {
        if ( output == null )
            return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

private ViewTreeObserver.OnGlobalLayoutListener layoutObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
    @Override
    public void onGlobalLayout() {
        if(fragmentView.getMeasuredWidth() != fragmentWidth || fragmentView.getMeasuredHeight() != fragmentHeight) {
            fragmentWidth = fragmentView.getMeasuredWidth();
            fragmentHeight = fragmentView.getMeasuredHeight();

            if (fragmentWidth < fragmentHeight) {
                float compassWidth = fragmentView.getMeasuredWidth() * 0.83f;
                float compassHeight = fragmentView.getMeasuredHeight() * 0.75f;

                if (compassWidth > compassHeight * 0.75f)
                    compassWidth = compassHeight * 0.75f;
                else
                    compassHeight = compassWidth / 0.75f;

                RelativeLayout.LayoutParams lytCompassContainerLytParams = (RelativeLayout.LayoutParams) lytCompassContainer.getLayoutParams();
                lytCompassContainerLytParams.width = (int) compassWidth;
                lytCompassContainerLytParams.height = (int) compassHeight;
                lytCompassContainer.setLayoutParams(lytCompassContainerLytParams);

                RelativeLayout.LayoutParams lytCompassShadowLytParams = (RelativeLayout.LayoutParams) fragmentView.findViewById(R.id.lyt_compass_shadow).getLayoutParams();
                lytCompassShadowLytParams.width = (int) compassWidth;
                lytCompassShadowLytParams.height = (int) compassWidth;
                fragmentView.findViewById(R.id.lyt_compass_shadow).setLayoutParams(lytCompassShadowLytParams);

                RelativeLayout.LayoutParams lytCompassLytParams = (RelativeLayout.LayoutParams) fragmentView.findViewById(R.id.lyt_compass).getLayoutParams();
                lytCompassLytParams.width = (int) (compassWidth * 0.9f);
                lytCompassLytParams.height = (int) (compassWidth * 0.9f);
                lytCompassLytParams.setMargins((int) (compassWidth / 20f), (int) (compassWidth / 20f), (int) (compassWidth / 20f), (int) (compassWidth / 20f));
                fragmentView.findViewById(R.id.lyt_compass).setLayoutParams(lytCompassLytParams);

                lblQibla.setTextSize(TypedValue.COMPLEX_UNIT_PX, compassWidth * 0.107f);
                lblAngle.setTextSize(TypedValue.COMPLEX_UNIT_PX, compassWidth * 0.16f);
            } else {
                float compassWidth = fragmentView.getMeasuredWidth() * 0.66f;
                float compassHeight = fragmentView.getMeasuredHeight() * 0.90f;

                if (compassWidth > compassHeight * 1.6f)
                    compassWidth = compassHeight * 1.6f;
                else
                    compassHeight = compassWidth / 1.6f;

                RelativeLayout.LayoutParams lytCompassContainerLytParams = (RelativeLayout.LayoutParams) lytCompassContainer.getLayoutParams();
                lytCompassContainerLytParams.width = (int) compassWidth;
                lytCompassContainerLytParams.height = (int) compassHeight;
                lytCompassContainer.setLayoutParams(lytCompassContainerLytParams);

                RelativeLayout.LayoutParams lytCompassShadowLytParams = (RelativeLayout.LayoutParams) fragmentView.findViewById(R.id.lyt_compass_shadow).getLayoutParams();
                lytCompassShadowLytParams.width = (int) compassHeight;
                lytCompassShadowLytParams.height = (int) compassHeight;
                fragmentView.findViewById(R.id.lyt_compass_shadow).setLayoutParams(lytCompassShadowLytParams);

                RelativeLayout.LayoutParams lytCompassLytParams = (RelativeLayout.LayoutParams) fragmentView.findViewById(R.id.lyt_compass).getLayoutParams();
                lytCompassLytParams.width = (int) (compassHeight * 0.9f);
                lytCompassLytParams.height = (int) (compassHeight * 0.9f);
                lytCompassLytParams.setMargins((int) (compassHeight / 20f), (int) (compassHeight / 20f), (int) (compassHeight / 20f), (int) (compassHeight / 20f));
                fragmentView.findViewById(R.id.lyt_compass).setLayoutParams(lytCompassLytParams);

                lblQibla.setTextSize(TypedValue.COMPLEX_UNIT_PX, compassHeight * 0.13f);
                lblAngle.setTextSize(TypedValue.COMPLEX_UNIT_PX, compassHeight * 0.19f);
            }
        }
    }
};
}
