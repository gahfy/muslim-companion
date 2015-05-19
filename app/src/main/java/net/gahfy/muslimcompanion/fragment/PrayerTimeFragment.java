package net.gahfy.muslimcompanion.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gahfy.muslimcompanion.DbManager;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.DateUtils;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class PrayerTimeFragment extends AbstractFragment{
    private View fragmentView;
    private int dayDifference = 0;
    private boolean hasResumed = false;

    private String cityName;
    private String countryIso;

    private TextView lblTimeFajr;
    private TextView lblTimeSunrise;
    private TextView lblTimeDhuhr;
    private TextView lblTimeAsr;
    private TextView lblTimeMaghrib;
    private TextView lblTimeIsha;

    public PrayerTimeFragment(){
        super();
    }

    public void setDayDifference(int dayDifference){
        this.dayDifference = dayDifference;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_prayer_time, container, false);

        initMembers();

        if(savedInstanceState == null) {
            getMainActivity().setTitle(R.string.salat);
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
        lblTimeFajr = (TextView) fragmentView.findViewById(R.id.lbl_time_fajr);
        lblTimeSunrise = (TextView) fragmentView.findViewById(R.id.lbl_time_sunrise);
        lblTimeDhuhr = (TextView) fragmentView.findViewById(R.id.lbl_time_dhuhr);
        lblTimeAsr = (TextView) fragmentView.findViewById(R.id.lbl_time_asr);
        lblTimeMaghrib = (TextView) fragmentView.findViewById(R.id.lbl_time_maghrib);
        lblTimeIsha = (TextView) fragmentView.findViewById(R.id.lbl_time_isha);

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
            long timeStamp = new Date().getTime() + ((long) dayDifference * 24l * 3600l * 1000l);
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTimeInMillis(timeStamp);

            int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            int month = gregorianCalendar.get(Calendar.MONTH)+1;
            int year = gregorianCalendar.get(Calendar.YEAR);
            countryIso = LocationUtils.getCountryIso(getActivity());
            PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(year, month, day, location.getLocationLatitude(), location.getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
            if(countryIso != null)
                prayerTimesUtils.changeCountry(countryIso);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

            String[] islamicMonths = getActivity().getResources().getStringArray(R.array.islamic_month);
            String[] gregorianMonths = getActivity().getResources().getStringArray(R.array.gregorian_month);

            int[] hijri = DateUtils.getHijriFromJulianDay(DateUtils.dateToJulian(year, month, day));

            String gregorianDateFormat = getActivity().getString(R.string.gregorian_date_format);
            String islamicDateFormat = getActivity().getString(R.string.islamic_date_format);


            String gregorianDate = String.format(gregorianDateFormat, gregorianMonths[month], day, year);
            String islamicDate = String.format(islamicDateFormat, hijri[2], islamicMonths[hijri[1]], hijri[0]);

            getMainActivity().setSubTitle(String.format("%s (%s)", gregorianDate, islamicDate));

            long currentTimestamp = new Date().getTime();

            if(prayerTimesUtils.getFajrTimestamp() > currentTimestamp) {
                lblTimeFajr.setTextColor(getActivity().getResources().getColor(R.color.accent));
            }
            else if(prayerTimesUtils.getSunriseTimestamp() > currentTimestamp) {
                lblTimeFajr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                lblTimeSunrise.setTextColor(getActivity().getResources().getColor(R.color.accent));
            }
            else if(prayerTimesUtils.getDhuhrTimestamp() > currentTimestamp) {
                lblTimeSunrise.setTextColor(getActivity().getResources().getColor(R.color.primary));
                lblTimeDhuhr.setTextColor(getActivity().getResources().getColor(R.color.accent));
            }
            else if(prayerTimesUtils.getAsrTimestamp() > currentTimestamp) {
                lblTimeDhuhr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                lblTimeAsr.setTextColor(getActivity().getResources().getColor(R.color.accent));
            }
            else if(prayerTimesUtils.getMaghribTimestamp() > currentTimestamp) {
                lblTimeAsr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                lblTimeMaghrib.setTextColor(getActivity().getResources().getColor(R.color.accent));
            }
            else if(prayerTimesUtils.getIshaTimestamp() > currentTimestamp) {
                lblTimeMaghrib.setTextColor(getActivity().getResources().getColor(R.color.primary));
                lblTimeIsha.setTextColor(getActivity().getResources().getColor(R.color.accent));
            }
            else{
                lblTimeIsha.setTextColor(getActivity().getResources().getColor(R.color.primary));
            }

            lblTimeFajr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestamp())));
            lblTimeSunrise.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getSunriseTimestamp())));
            lblTimeDhuhr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getDhuhrTimestamp())));
            lblTimeAsr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getAsrTimestamp())));
            lblTimeMaghrib.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getMaghribTimestamp())));
            lblTimeIsha.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestamp())));

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
            DbManager dbManager = new DbManager(getActivity());
            dbManager.createDataBase();
            dbManager.openDataBase();
            SQLiteDatabase db = dbManager.getDb();
            Cursor c = db.rawQuery(String.format(Locale.US, "SELECT cities.iso, CASE WHEN (alternateNames.alternate_name IS NULL) THEN cities.name ELSE alternateNames.alternate_name END as cityName\n" +
                    "FROM cities\n" +
                    "LEFT OUTER JOIN (SELECT * FROM alternateNames WHERE isolanguage IN (".concat(getMainActivity().getString(R.string.language_name_for_database)).concat(")) alternateNames ON alternateNames.geonameid = cities._id\n" +
                            "ORDER BY ((cities.latitude - %f)*(cities.latitude - %f)) + ((cities.longitude - %f)*(cities.longitude - %f))\n" +
                            "LIMIT 0,1;"), getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLongitude(), getMainActivity().getCurrentLocation().getLocationLongitude()), null);
            c.moveToFirst();
            countryIso = c.getString(0).toLowerCase();
            cityName = c.getString(1);
            c.close();
            db.close();
            getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getMainActivity().setTitle(getMainActivity().getString(R.string.salat_at, cityName));


                    long timeStamp = new Date().getTime() + ((long) dayDifference * 24l * 3600l * 1000l);
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    gregorianCalendar.setTimeInMillis(timeStamp);

                    int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
                    int month = gregorianCalendar.get(Calendar.MONTH)+1;
                    int year = gregorianCalendar.get(Calendar.YEAR);
                    PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(year, month, day, getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
                    prayerTimesUtils.changeCountry(countryIso);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                    String[] islamicMonths = getActivity().getResources().getStringArray(R.array.islamic_month);
                    String[] gregorianMonths = getActivity().getResources().getStringArray(R.array.gregorian_month);

                    int[] hijri = DateUtils.getHijriFromJulianDay(DateUtils.dateToJulian(year, month, day));

                    String gregorianDateFormat = getActivity().getString(R.string.gregorian_date_format);
                    String islamicDateFormat = getActivity().getString(R.string.islamic_date_format);


                    String gregorianDate = String.format(gregorianDateFormat, gregorianMonths[month], day, year);
                    String islamicDate = String.format(islamicDateFormat, hijri[2], islamicMonths[hijri[1]], hijri[0]);

                    getMainActivity().setSubTitle(String.format("%s (%s)", gregorianDate, islamicDate));

                    long currentTimestamp = new Date().getTime();

                    if(prayerTimesUtils.getFajrTimestamp() > currentTimestamp) {
                        lblTimeFajr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                    }
                    else if(prayerTimesUtils.getSunriseTimestamp() > currentTimestamp) {
                        lblTimeFajr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                        lblTimeSunrise.setTextColor(getActivity().getResources().getColor(R.color.accent));
                    }
                    else if(prayerTimesUtils.getDhuhrTimestamp() > currentTimestamp) {
                        lblTimeSunrise.setTextColor(getActivity().getResources().getColor(R.color.primary));
                        lblTimeDhuhr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                    }
                    else if(prayerTimesUtils.getAsrTimestamp() > currentTimestamp) {
                        lblTimeDhuhr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                        lblTimeAsr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                    }
                    else if(prayerTimesUtils.getMaghribTimestamp() > currentTimestamp) {
                        lblTimeAsr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                        lblTimeMaghrib.setTextColor(getActivity().getResources().getColor(R.color.accent));
                    }
                    else if(prayerTimesUtils.getIshaTimestamp() > currentTimestamp) {
                        lblTimeMaghrib.setTextColor(getActivity().getResources().getColor(R.color.primary));
                        lblTimeIsha.setTextColor(getActivity().getResources().getColor(R.color.accent));
                    }
                    else{
                        lblTimeIsha.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    }

                    lblTimeFajr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestamp())));
                    lblTimeSunrise.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getSunriseTimestamp())));
                    lblTimeDhuhr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getDhuhrTimestamp())));
                    lblTimeAsr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getAsrTimestamp())));
                    lblTimeMaghrib.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getMaghribTimestamp())));
                    lblTimeIsha.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestamp())));


                }
            });
        }
        catch(Exception e){
            getMainActivity().sendCityErrorEvent();
        }
    }

    public void restoreState(Bundle savedInstanceState){
        cityName = savedInstanceState.getString("cityName");
        if(cityName != null){
            getMainActivity().setTitle(getMainActivity().getString(R.string.salat_at, cityName));
            if(getMainActivity().getCurrentLocation() != null){
                long timeStamp = new Date().getTime() + ((long) dayDifference * 24l * 3600l * 1000l);
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTimeInMillis(timeStamp);

                int day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
                int month = gregorianCalendar.get(Calendar.MONTH)+1;
                int year = gregorianCalendar.get(Calendar.YEAR);

                PrayerTimesUtils prayerTimesUtils = new PrayerTimesUtils(year, month, day, getMainActivity().getCurrentLocation().getLocationLatitude(), getMainActivity().getCurrentLocation().getLocationLongitude(), PrayerTimesUtils.Convention.MUSLIM_WORLD_LEAGUE, PrayerTimesUtils.School.NOT_HANAFI);
                if(countryIso != null)
                    prayerTimesUtils.changeCountry(countryIso);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                String[] islamicMonths = getActivity().getResources().getStringArray(R.array.islamic_month);
                String[] gregorianMonths = getActivity().getResources().getStringArray(R.array.gregorian_month);

                int[] hijri = DateUtils.getHijriFromJulianDay(DateUtils.dateToJulian(year, month, day));

                String gregorianDateFormat = getActivity().getString(R.string.gregorian_date_format);
                String islamicDateFormat = getActivity().getString(R.string.islamic_date_format);


                String gregorianDate = String.format(gregorianDateFormat, gregorianMonths[month], day, year);
                String islamicDate = String.format(islamicDateFormat, hijri[2], islamicMonths[hijri[1]], hijri[0]);

                getMainActivity().setSubTitle(String.format("%s (%s)", gregorianDate, islamicDate));

                long currentTimestamp = new Date().getTime();

                if(prayerTimesUtils.getFajrTimestamp() > currentTimestamp) {
                    lblTimeFajr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                }
                else if(prayerTimesUtils.getSunriseTimestamp() > currentTimestamp) {
                    lblTimeFajr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    lblTimeSunrise.setTextColor(getActivity().getResources().getColor(R.color.accent));
                }
                else if(prayerTimesUtils.getDhuhrTimestamp() > currentTimestamp) {
                    lblTimeSunrise.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    lblTimeDhuhr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                }
                else if(prayerTimesUtils.getAsrTimestamp() > currentTimestamp) {
                    lblTimeDhuhr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    lblTimeAsr.setTextColor(getActivity().getResources().getColor(R.color.accent));
                }
                else if(prayerTimesUtils.getMaghribTimestamp() > currentTimestamp) {
                    lblTimeAsr.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    lblTimeMaghrib.setTextColor(getActivity().getResources().getColor(R.color.accent));
                }
                else if(prayerTimesUtils.getIshaTimestamp() > currentTimestamp) {
                    lblTimeMaghrib.setTextColor(getActivity().getResources().getColor(R.color.primary));
                    lblTimeIsha.setTextColor(getActivity().getResources().getColor(R.color.accent));
                }
                else{
                    lblTimeIsha.setTextColor(getActivity().getResources().getColor(R.color.primary));
                }

                lblTimeFajr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getFajrTimestamp())));
                lblTimeSunrise.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getSunriseTimestamp())));
                lblTimeDhuhr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getDhuhrTimestamp())));
                lblTimeAsr.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getAsrTimestamp())));
                lblTimeMaghrib.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getMaghribTimestamp())));
                lblTimeIsha.setText(simpleDateFormat.format(new Date(prayerTimesUtils.getIshaTimestamp())));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("cityName", cityName);
        super.onSaveInstanceState(outState);
    }
}
