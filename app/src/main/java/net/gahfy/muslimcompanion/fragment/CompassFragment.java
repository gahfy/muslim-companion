package net.gahfy.muslimcompanion.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.MathUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;
import net.gahfy.muslimcompanion.view.CompassArrowView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CompassFragment extends AbstractFragment implements ViewTreeObserver.OnGlobalLayoutListener, SensorEventListener {
    /** The layout that contains the compass with the text of the Qibla */
    private RelativeLayout lytCompassContainer;

    /** The TextView which contains the Qibla label */
    private TextView lblQibla;

    /** The TextView which contains the Angle of the Qibla */
    private TextView lblAngle;

    /** The ImageView that contains the arrow with the direction of the Qibla */
    private CompassArrowView imgCompassArrowDirection;

    /** Whether the compass is currently animating or not */
    private boolean isCompassAnimating = false;
    /** Whether the compass is currently working or not */
    private boolean isCompassWorking = false;

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

    /** The view of the fragment */
    private View fragmentView;

    /** The current width of the fragment */
    float fragmentWidth = 0f;
    /** The current height of the fragment */
    float fragmentHeight = 0f;

    /** Whether the fragment has been resumed or not */
    private boolean hasResumed = false;

    /** The name of the current city */
    private String cityName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_compass, container, false);

        initMembers();

        if(mAccelerometer != null && mMagnetometer != null) {
            isCompassWorking = true;
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
            getMainActivity().sendCompassOnEvent();
        }
        else{
            //TODO : Manage Error
            getMainActivity().sendCompassErrorEvent();
        }

        ViewUtils.setTypefaceToTextView(getMainActivity(), lblQibla, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getMainActivity(), lblAngle, ViewUtils.FONT_WEIGHT.MEDIUM);

        if(savedInstanceState == null) {
            getMainActivity().setTitle(R.string.qibla);
            if (getMainActivity().getCurrentLocation() != null) {
                this.manageFoundLocation(getMainActivity().getCurrentLocation());
            }
        }
        else{
            restoreState(savedInstanceState);
        }

        return fragmentView;
    }

    public void initMembers(){
        isCompassAnimating = false;
        fragmentWidth = 0;
        fragmentHeight = 0;

        WindowManager mWindowManager = (WindowManager) getMainActivity().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) getMainActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        lytCompassContainer = (RelativeLayout) fragmentView.findViewById(R.id.lyt_compass_container);

        lblQibla = (TextView) fragmentView.findViewById(R.id.lbl_qibla);
        lblAngle = (TextView) fragmentView.findViewById(R.id.lbl_angle);

        imgCompassArrowDirection = (CompassArrowView) fragmentView.findViewById(R.id.img_compass_arrow_direction);

        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        hasResumed = true;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(isCompassWorking){
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
            isCompassWorking = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mAccelerometer) {
            float[] temp = MathUtils.exponentialSmoothing(mLastAccelerometer, event.values, 0.5f);
            System.arraycopy(temp, 0, mLastAccelerometer, 0, temp.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            float[] temp = MathUtils.exponentialSmoothing(mLastMagnetometer, event.values, 0.8f);
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

                if (compassWidth > compassHeight * 1.7f)
                    compassWidth = compassHeight * 1.7f;
                else
                    compassHeight = compassWidth / 1.7f;

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

    @Override
    public GEOLOCATION_TYPE getGeolocationTypeNeeded(){
        return GEOLOCATION_TYPE.ONCE;
    }

    @Override
    public void onLocationChanged(MuslimLocation location){
            manageFoundLocation(location);
    }

    /**
     * Called when a location is found
     */
    public void manageFoundLocation(MuslimLocation location){
        if(hasResumed) {
            int kaabaBearing = (int) LocationUtils.bearingToKaaba(location.getLocationLatitude(), location.getLocationLongitude());
            lblAngle.setText(getMainActivity().getString(R.string.angle, kaabaBearing));

            Animation an = new RotateAnimation(0.0f, kaabaBearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            an.setDuration(0);
            an.setRepeatCount(0);
            an.setRepeatMode(Animation.REVERSE);
            an.setFillAfter(true);

            // Aply animation to image view
            imgCompassArrowDirection.startAnimation(an);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    applyCity();
                }
            }).start();
        }
    }

    public void applyCity(){
        try {
            String[] locationDatas = LocationUtils.getCountryIsoAndCityName(getActivity(), getMainActivity().getCurrentLocation());

            if(locationDatas != null)
                cityName = locationDatas[1];

            getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getMainActivity().setTitle(getMainActivity().getString(R.string.qibla_at, cityName));
                    if(getMainActivity().getCurrentLocation().getLocationMode() == MuslimLocation.MODE.MODE_PROVIDER) {
                        String date = new SimpleDateFormat(getMainActivity().getString(R.string.short_date_format), Locale.getDefault()).format(getMainActivity().getCurrentLocation().getLocationTime());
                        String hour = new SimpleDateFormat(getMainActivity().getString(R.string.hour_format), Locale.getDefault()).format(getMainActivity().getCurrentLocation().getLocationTime());
                        getMainActivity().setSubTitle(getMainActivity().getString(R.string.last_geolocation_on, date, hour));
                    }
                }
            });
        }
        catch(Exception e){
            getMainActivity().sendCityErrorEvent();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("cityName", cityName);

        super.onSaveInstanceState(outState);
    }

    public void restoreState(Bundle savedInstanceState){
        cityName = savedInstanceState.getString("cityName");
        if(cityName != null){
            getMainActivity().setTitle(getMainActivity().getString(R.string.qibla_at, cityName));
        }
        else{
            getMainActivity().setTitle(R.string.qibla);
        }
        if(getMainActivity().getCurrentLocation() != null){
            if(getMainActivity().getCurrentLocation().getLocationMode() == MuslimLocation.MODE.MODE_PROVIDER) {
                String date = new SimpleDateFormat(getMainActivity().getString(R.string.short_date_format), Locale.getDefault()).format(getMainActivity().getCurrentLocation().getLocationTime());
                String hour = new SimpleDateFormat(getMainActivity().getString(R.string.hour_format), Locale.getDefault()).format(getMainActivity().getCurrentLocation().getLocationTime());
                getMainActivity().setSubTitle(getMainActivity().getString(R.string.last_geolocation_on, date, hour));
            }

            int kaabaBearing = (int) LocationUtils.bearingToKaaba(getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLongitude());
            lblAngle.setText(getMainActivity().getString(R.string.angle, kaabaBearing));

            Animation an = new RotateAnimation(0.0f, kaabaBearing, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

            an.setDuration(0);
            an.setRepeatCount(0);
            an.setRepeatMode(Animation.REVERSE);
            an.setFillAfter(true);

            // Aply animation to image view
            imgCompassArrowDirection.startAnimation(an);
        }
    }
}
