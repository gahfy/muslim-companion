package net.gahfy.muslimcompanion.fragment;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.AlarmUtils;
import net.gahfy.muslimcompanion.utils.DateUtils;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PrayerTimeFragment extends AbstractFragment{
    private View fragmentView;
    private int dayDifference = 0;
    private boolean hasResumed = false;

    private String cityName;
    private String staticCityName = null;
    private String countryIso;

    private TextView lblPrayerDhuhr;
    private TextView lblTimeFajr;
    private TextView lblTimeSunrise;
    private TextView lblTimeDhuhr;
    private TextView lblTimeAsr;
    private TextView lblTimeMaghrib;
    private TextView lblTimeIsha;

    private Bundle savedInstanceState;

    public PrayerTimeFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_prayer_time, container, false);

        if(savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
        }

        return fragmentView;
    }

    @Override
    public void onResume(){
        super.onResume();

        initMembers();

        if(savedInstanceState == null) {
            getMainActivity().setTitle(R.string.salat);
            if (getMainActivity().getCurrentLocation() != null) {
                this.manageFoundLocation(getMainActivity().getCurrentLocation());
            }
        }
        else{
            restoreState(savedInstanceState);
            savedInstanceState = null;
        }
    }

    public void initMembers() {
        int width, height;
        float fontSize;
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }
        else{
            int[] size = getDisplaySizeBeforeHoneyComb(display);
            width = size[0];
            height = size[1];
        }

        if(width <= height) {
            if (((double) width / 1080.0) * 1776.0 < (double) height) {
                height = (int) (((double) width / 1080.0) * 1776.0);
            }
            fontSize = (float) ((((double) height)/1776.0)*85.0);
        }
        else{
            if (((double) width*1080.0)/1794.0 < (double) height) {
                height = (int) (((double) width * 1080.0) / 1794.0);
            }
            fontSize = (float) ((((double) height)/1080.0)*85.0);
        }

        TextView lblPrayerFajr = (TextView) fragmentView.findViewById(R.id.lbl_prayer_fajr);
        TextView lblPrayerSunrise = (TextView) fragmentView.findViewById(R.id.lbl_prayer_sunrise);
        lblPrayerDhuhr = (TextView) fragmentView.findViewById(R.id.lbl_prayer_dhuhr);
        TextView lblPrayerAsr = (TextView) fragmentView.findViewById(R.id.lbl_prayer_asr);
        TextView lblPrayerMaghrib = (TextView) fragmentView.findViewById(R.id.lbl_prayer_maghrib);
        TextView lblPrayerIsha = (TextView) fragmentView.findViewById(R.id.lbl_prayer_isha);

        lblTimeFajr = (TextView) fragmentView.findViewById(R.id.lbl_time_fajr);
        lblTimeSunrise = (TextView) fragmentView.findViewById(R.id.lbl_time_sunrise);
        lblTimeDhuhr = (TextView) fragmentView.findViewById(R.id.lbl_time_dhuhr);
        lblTimeAsr = (TextView) fragmentView.findViewById(R.id.lbl_time_asr);
        lblTimeMaghrib = (TextView) fragmentView.findViewById(R.id.lbl_time_maghrib);
        lblTimeIsha = (TextView) fragmentView.findViewById(R.id.lbl_time_isha);

        lblPrayerFajr.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblPrayerSunrise.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblPrayerDhuhr.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblPrayerAsr.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblPrayerMaghrib.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblPrayerIsha.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);

        lblTimeFajr.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblTimeSunrise.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblTimeDhuhr.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblTimeAsr.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblTimeMaghrib.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        lblTimeIsha.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);

        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerFajr, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerSunrise, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerDhuhr, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerAsr, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerMaghrib, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerIsha, ViewUtils.FONT_WEIGHT.LIGHT);

        ViewUtils.setTypefaceToTextView(getActivity(), lblTimeFajr, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblTimeSunrise, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblTimeDhuhr, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblTimeAsr, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblTimeMaghrib, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblTimeIsha, ViewUtils.FONT_WEIGHT.MEDIUM);

        hasResumed = true;
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
            long timeStamp = new Date().getTime() + ((long) dayDifference * 24L * 3600L * 1000L);
            int[] calendarDatas = DateUtils.getDayMonthYear(timeStamp);

            countryIso = LocationUtils.getCountryIso(getActivity());
            final PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(getMainActivity(), calendarDatas[0], calendarDatas[1], calendarDatas[2], location.getLocationLatitude(), location.getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
            if(countryIso != null)
                prayerTimesUtils.changeCountry(countryIso);

            updatePrayerTimes(prayerTimesUtils);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    applyCity(prayerTimesUtils);
                }
            }).start();
        }
    }

    public void applyCity(final PrayerTimesUtils prayerTimesUtils){
        try {
            String[] locationDatas = LocationUtils.getCountryIsoAndCityName(getActivity(), getMainActivity().getCurrentLocation());

            if(locationDatas != null) {
                countryIso = locationDatas[0].toLowerCase();
                cityName = locationDatas[1];
            }

            if(prayerTimesUtils.isYearlyRepeatedStaticTime)
                staticCityName = getResources().getStringArray(R.array.salat_title_static)[(int) prayerTimesUtils.staticPrayerCityId];

            getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!prayerTimesUtils.isYearlyRepeatedStaticTime && cityName != null)
                        getMainActivity().setTitle(getMainActivity().getString(R.string.salat_at, cityName));
                    else if(prayerTimesUtils.isYearlyRepeatedStaticTime)
                        getMainActivity().setTitle(getMainActivity().getString(R.string.salat_at, staticCityName));

                    long timeStamp = new Date().getTime() + ((long) dayDifference * 24L * 3600L * 1000L);
                    int[] calendarDatas = DateUtils.getDayMonthYear(timeStamp);

                    PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(getMainActivity(), calendarDatas[0], calendarDatas[1], calendarDatas[2], getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
                    if(countryIso != null)
                        prayerTimesUtils.changeCountry(countryIso);

                    updatePrayerTimes(prayerTimesUtils);
                }
            });
        }
        catch(Exception e){
            //TODO: Handle error
        }
    }

    public void restoreState(Bundle savedInstanceState){
        cityName = savedInstanceState.getString("cityName");
        staticCityName = savedInstanceState.getString("staticCityName");
        countryIso = savedInstanceState.getString("countryIso");

        if(staticCityName != null)
            getMainActivity().setTitle(getMainActivity().getString(R.string.salat_at, staticCityName));
        else if(cityName != null)
            getMainActivity().setTitle(getMainActivity().getString(R.string.salat_at, cityName));
        else
            getMainActivity().setTitle(R.string.salat);
        if(getMainActivity().getCurrentLocation() != null){
            long timeStamp = new Date().getTime() + ((long) dayDifference * 24L * 3600L * 1000L);
            int[] calendarDatas = DateUtils.getDayMonthYear(timeStamp);

            PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(getMainActivity(), calendarDatas[0], calendarDatas[1], calendarDatas[2], getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
            if(countryIso != null)
                prayerTimesUtils.changeCountry(countryIso);

            updatePrayerTimes(prayerTimesUtils);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("cityName", cityName);
        outState.putString("countryIso", countryIso);
        outState.putString("staticCityName", staticCityName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getLocationDetailsTextResId(){
        return R.string.location_details_salat;
    }

    public void updatePrayerTimes(PrayerTimesUtils prayerTimesUtils){
        if(getActivity() != null) {
            int year = prayerTimesUtils.getYear();
            int month = prayerTimesUtils.getMonth();
            int day = prayerTimesUtils.getDay();

            if(prayerTimesUtils.isFriday())
                lblPrayerDhuhr.setText(R.string.prayer_name_jumuah);
            else
                lblPrayerDhuhr.setText(R.string.prayer_name_dhuhr);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(getActivity().getString(R.string.hour_format), Locale.getDefault());

            String[] islamicMonths = getActivity().getResources().getStringArray(R.array.islamic_month);
            String[] gregorianMonths = getActivity().getResources().getStringArray(R.array.gregorian_month);
            String[] daySuffix = getActivity().getResources().getStringArray(R.array.day_number_suffix);

            int[] hijri = DateUtils.getHijriFromJulianDay(DateUtils.dateToJulian(year, month, day));

            String gregorianDateFormat = getActivity().getString(R.string.gregorian_date_format);
            String islamicDateFormat = getActivity().getString(R.string.islamic_date_format);


            String gregorianDate = String.format(gregorianDateFormat, gregorianMonths[month], day, year, daySuffix[day]);
            String islamicDate = String.format(islamicDateFormat, hijri[2], islamicMonths[hijri[1]], hijri[0]);

            getMainActivity().setSubTitle(String.format("%s (%s)", gregorianDate, islamicDate));

            long currentTimestamp = new Date().getTime();

            if (prayerTimesUtils.getFajrTimestamp() > currentTimestamp) {
                lblTimeFajr.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            } else if (prayerTimesUtils.getSunriseTimestamp() > currentTimestamp) {
                lblTimeFajr.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                lblTimeSunrise.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            } else if (prayerTimesUtils.getDhuhrTimestamp() > currentTimestamp) {
                lblTimeSunrise.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                lblTimeDhuhr.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            } else if (prayerTimesUtils.getAsrTimestamp() > currentTimestamp) {
                lblTimeDhuhr.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                lblTimeAsr.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            } else if (prayerTimesUtils.getMaghribTimestamp() > currentTimestamp) {
                lblTimeAsr.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                lblTimeMaghrib.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            } else if (prayerTimesUtils.getIshaTimestamp() > currentTimestamp) {
                lblTimeMaghrib.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
                lblTimeIsha.setTextColor(ContextCompat.getColor(getActivity(), R.color.accent));
            } else {
                lblTimeIsha.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
            }

            lblTimeFajr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestamp())));
            lblTimeSunrise.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getSunriseTimestamp())));
            lblTimeDhuhr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getDhuhrTimestamp())));
            lblTimeAsr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getAsrTimestamp())));
            lblTimeMaghrib.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getMaghribTimestamp())));
            lblTimeIsha.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestamp())));

            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        AlarmUtils.notifyAndSetNextAlarm(getMainActivity(), false);
                    }
                }
            ).start();
        }
    }

    @SuppressWarnings("deprecation")
    private int[] getDisplaySizeBeforeHoneyComb(Display display){
        return new int[]{display.getWidth(), display.getHeight()};
    }
}
