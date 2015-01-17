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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private double currentLatitude = 300.0;
    private double currentLongitude = 300.0;

    private RelativeLayout lytCompassContainer;
    private LinearLayout lytGeolocatingContainer;
    private TextView lblAngle;
    private ImageView imgCompassArrowDirection;

    private boolean isCompassAnimating = false;
    private boolean isCompassWorking = false;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    private Display mDisplay;

    /**
     * The status (enabled/disabled) of location listeners
     * @see net.gahfy.muslimcompanion.utils.LocationUtils
     */
    int locationProvidersStatus = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View resultView = inflater.inflate(R.layout.fragment_compass, container, false);

        WindowManager mWindowManager = (WindowManager) getMainActivity().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) getMainActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        lytCompassContainer = (RelativeLayout) resultView.findViewById(R.id.lyt_compass_container);
        lytGeolocatingContainer = (LinearLayout) resultView.findViewById(R.id.lyt_geolocating_container);

        TextView lblGeolocating = (TextView) resultView.findViewById(R.id.lbl_geolocating);
        TextView lblQibla = (TextView) resultView.findViewById(R.id.lbl_qibla);
        lblAngle = (TextView) resultView.findViewById(R.id.lbl_angle);

        imgCompassArrowDirection = (ImageView) resultView.findViewById(R.id.img_compass_arrow_direction);

        ViewUtils.setTypefaceToTextView(getMainActivity(), lblGeolocating, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblQibla, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblAngle, ViewUtils.FONT_WEIGHT.MEDIUM);

        return resultView;
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
            mSensorManager.unregisterListener(sensorEventListener, mAccelerometer);
            mSensorManager.unregisterListener(sensorEventListener, mMagnetometer);
            isCompassWorking = false;
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
                    mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
                    mSensorManager.registerListener(sensorEventListener, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
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
     * Returns whether a location is good for this fragment or not.
     * @param location the location to check
     * @return whether a location is good for this fragment or not
     */
    public static boolean isLocationGoodEnough(Location location){
        return location != null && location.getTime()+3600000l >= new Date().getTime();
    }

    /**
     * The location listener of the fragment
     */
    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            CompassFragment.this.currentLatitude = location.getLatitude();
            CompassFragment.this.currentLongitude = location.getLongitude();
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

    private SensorEventListener sensorEventListener = new SensorEventListener() {


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
                float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360);

                switch(mDisplay.getRotation()){
                    case Surface.ROTATION_90:
                        azimuthInDegress += 90;
                        break;
                    case Surface.ROTATION_180:
                        azimuthInDegress += 180;
                        break;
                    case Surface.ROTATION_270:
                        azimuthInDegress -= 90;
                        break;
                }
                azimuthInDegress = azimuthInDegress%360;

                if(!isCompassAnimating) {

                    isCompassAnimating = true;
                    RotateAnimation ra = new RotateAnimation(
                            mCurrentDegree,
                            -azimuthInDegress,
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

                    getView().findViewById(R.id.lyt_compass).startAnimation(ra);
                    mCurrentDegree = -azimuthInDegress;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private float[] exponentialSmoothing( float[] input, float[] output, float alpha ) {
        if ( output == null )
            return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + alpha * (input[i] - output[i]);
        }
        return output;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putDouble("latitude", currentLatitude);
        outState.putDouble("longitude", currentLongitude);
    }
}
