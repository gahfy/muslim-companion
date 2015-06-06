package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

import org.w3c.dom.Text;

public class SettingsFragment extends AbstractFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RelativeLayout lytChooseConvention = (RelativeLayout) view.findViewById(R.id.lyt_choose_convention);
        RelativeLayout lytChooseAsr = (RelativeLayout) view.findViewById(R.id.lyt_choose_asr);
        RelativeLayout lytChooseHigherLatitude = (RelativeLayout) view.findViewById(R.id.lyt_choose_higher_latitude);
        RelativeLayout lytChooseNotificationSound = (RelativeLayout) view.findViewById(R.id.lyt_choose_notification_sound);

        TextView lblPrayerTitle = (TextView) view.findViewById(R.id.lbl_prayer_title);
        TextView lblPrayerNotificationTitle = (TextView) view.findViewById(R.id.lbl_prayer_notification_title);

        TextView lblChooseConvention = (TextView) view.findViewById(R.id.lbl_choose_convention);
        TextView lblChooseConventionDetail= (TextView) view.findViewById(R.id.lbl_choose_convention_details);

        TextView lblChooseAsr= (TextView) view.findViewById(R.id.lbl_choose_asr);
        TextView lblChooseAsrDetail= (TextView) view.findViewById(R.id.lbl_choose_asr_details);

        TextView lblChooseHigherLatitude= (TextView) view.findViewById(R.id.lbl_choose_higher_latitude);
        TextView lblChooseHigherLatitudeDetail= (TextView) view.findViewById(R.id.lbl_choose_higher_latitude_details);

        final TextView lblChooseIfNotification = (TextView) view.findViewById(R.id.lbl_choose_if_notification);

        TextView lblChooseNotificationSound = (TextView) view.findViewById(R.id.lbl_choose_notification_sound);
        TextView lblChooseNotificationSoundDetail= (TextView) view.findViewById(R.id.lbl_choose_notification_sound_details);

        SwitchCompat swChooseIfNotification = (SwitchCompat) view.findViewById(R.id.sw_choose_if_notification);

        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerTitle, ViewUtils.FONT_WEIGHT.BOLD);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerNotificationTitle, ViewUtils.FONT_WEIGHT.BOLD);

        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseConvention, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseAsr, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseHigherLatitude, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseIfNotification, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseNotificationSound, ViewUtils.FONT_WEIGHT.MEDIUM);

        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseConventionDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseAsrDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseHigherLatitudeDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseNotificationSoundDetail, ViewUtils.FONT_WEIGHT.LIGHT);

        boolean isNotificationEnabled = SharedPreferencesUtils.getNotifyPrayer(getActivity());
        boolean isConventionAutomatic = SharedPreferencesUtils.getConventionIsAutomatic(getActivity());
        boolean isSchoolAutomatic = SharedPreferencesUtils.getSchoolIsAutomatic(getActivity());
        boolean isHigherLatitudeAutomatic = SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(getActivity());

        int preferedConvention = SharedPreferencesUtils.getConventionValue(getMainActivity());
        int preferedAsr = SharedPreferencesUtils.getSchoolValue(getMainActivity());
        int preferedHigherLatitude = SharedPreferencesUtils.getHigherLatitudeModeValue(getMainActivity());
        int preferedNotificationSound = SharedPreferencesUtils.getSoundNotificationPrayer(getActivity());

        lblChooseIfNotification.setText(isNotificationEnabled ? R.string.prayer_notification_enabled : R.string.prayer_notification_disabled);
        swChooseIfNotification.setChecked(isNotificationEnabled);

        swChooseIfNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lblChooseIfNotification.setText(isChecked ? R.string.prayer_notification_enabled : R.string.prayer_notification_disabled);
                SharedPreferencesUtils.putNotifyPrayer(getMainActivity(), isChecked);
            }
        });

        switch (preferedNotificationSound){
            case 0:
                lblChooseNotificationSoundDetail.setText(R.string.sound_none);
                break;
            case 1:
                lblChooseNotificationSoundDetail.setText(R.string.sound_android_default);
                break;
            default:
                lblChooseNotificationSoundDetail.setText(R.string.sound_adhan);
                break;
        }

        if(isConventionAutomatic) {
            if(preferedConvention == -1) {
                lblChooseConventionDetail.setText(R.string.automatic);
            }
            else{
                lblChooseConventionDetail.setText(String.format("%s (%s)", getActivity().getString(R.string.automatic), getActivity().getString(PrayerTimesUtils.getConventionNameResId(PrayerTimesUtils.getConventionFromPreferenceValue(preferedConvention)))));
            }
        }
        else{
            lblChooseConventionDetail.setText(PrayerTimesUtils.getConventionNameResId(PrayerTimesUtils.getConventionFromPreferenceValue(preferedConvention)));
        }

        if(isSchoolAutomatic) {
            if(preferedAsr == -1) {
                lblChooseAsrDetail.setText(R.string.automatic);
            }
            else{
                lblChooseAsrDetail.setText(String.format("%s (%s)", getActivity().getString(R.string.automatic), getActivity().getString(PrayerTimesUtils.getSchoolResId(PrayerTimesUtils.getSchoolFromPreferenceValue(preferedAsr)))));
            }
        }
        else{
            lblChooseAsrDetail.setText(PrayerTimesUtils.getSchoolResId(PrayerTimesUtils.getSchoolFromPreferenceValue(preferedAsr)));
        }

        if(isHigherLatitudeAutomatic) {
            if(preferedHigherLatitude == -1) {
                lblChooseHigherLatitudeDetail.setText(R.string.automatic);
            }
            else{
                lblChooseHigherLatitudeDetail.setText(String.format("%s (%s)", getActivity().getString(R.string.automatic), getActivity().getString(PrayerTimesUtils.getHigherLatitudeModeResId(PrayerTimesUtils.getHigherLatitudeModeFromPreferenceValue(preferedHigherLatitude)))));
            }
        }
        else{
            lblChooseHigherLatitudeDetail.setText(PrayerTimesUtils.getHigherLatitudeModeResId(PrayerTimesUtils.getHigherLatitudeModeFromPreferenceValue(preferedHigherLatitude)));
        }

        lytChooseConvention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().redirectToConventionList();
            }
        });
        lytChooseAsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().redirectToSchoolList();
            }
        });
        lytChooseHigherLatitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().redirectToHigherLatitudeList();
            }
        });
        lytChooseNotificationSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().redirectToNotificationSoundList();
            }
        });

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        getMainActivity().setTitle(R.string.settings);
    }
}
