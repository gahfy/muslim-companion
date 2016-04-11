package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

public class SettingsFragment extends AbstractFragment{
    boolean isNotificationEnabled;
    boolean isJumuahFirstCallEnabled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View printedView;
        boolean isAlternative = false;
        try{
            printedView = inflater.inflate(R.layout.fragment_settings, container, false);
        }
        catch(InflateException e){
            printedView = inflater.inflate(R.layout.fragment_settings_alternative, container, false);
            isAlternative = true;
        }
        final View view = printedView;

        RelativeLayout lytChooseConvention = (RelativeLayout) view.findViewById(R.id.lyt_choose_convention);
        RelativeLayout lytChooseAsr = (RelativeLayout) view.findViewById(R.id.lyt_choose_asr);
        RelativeLayout lytChooseHigherLatitude = (RelativeLayout) view.findViewById(R.id.lyt_choose_higher_latitude);
        RelativeLayout lytChooseNotificationSound = (RelativeLayout) view.findViewById(R.id.lyt_choose_notification_sound);
        RelativeLayout lytChooseFirstCallJumuah = (RelativeLayout) view.findViewById(R.id.lyt_choose_first_call_jumuah);

        TextView lblPrayerTitle = (TextView) view.findViewById(R.id.lbl_prayer_title);
        TextView lblPrayerNotificationTitle = (TextView) view.findViewById(R.id.lbl_prayer_notification_title);

        TextView lblChooseConvention = (TextView) view.findViewById(R.id.lbl_choose_convention);
        TextView lblChooseConventionDetail= (TextView) view.findViewById(R.id.lbl_choose_convention_details);

        TextView lblChooseFirstCallJumuah = (TextView) view.findViewById(R.id.lbl_choose_first_call_jumuah);
        final TextView lblChooseFirstCallJumuahDetail= (TextView) view.findViewById(R.id.lbl_choose_first_call_jumuah_details);

        TextView lblChooseAsr= (TextView) view.findViewById(R.id.lbl_choose_asr);
        TextView lblChooseAsrDetail= (TextView) view.findViewById(R.id.lbl_choose_asr_details);

        TextView lblChooseHigherLatitude= (TextView) view.findViewById(R.id.lbl_choose_higher_latitude);
        TextView lblChooseHigherLatitudeDetail= (TextView) view.findViewById(R.id.lbl_choose_higher_latitude_details);

        final TextView lblChooseIfNotification = (TextView) view.findViewById(R.id.lbl_choose_if_notification);

        TextView lblChooseNotificationSound = (TextView) view.findViewById(R.id.lbl_choose_notification_sound);
        TextView lblChooseNotificationSoundDetail= (TextView) view.findViewById(R.id.lbl_choose_notification_sound_details);

        isNotificationEnabled = SharedPreferencesUtils.getNotifyPrayer(getActivity());
        isJumuahFirstCallEnabled = SharedPreferencesUtils.getJumuahFirstCallEnabled(getActivity());
        final int preferedJumuahFirstCallDelay = SharedPreferencesUtils.getJumuahFirstCallDelay(getActivity());

        if(isAlternative) {
            final ImageView swChooseIfNotification = (ImageView) view.findViewById(R.id.sw_choose_if_notification);
            final ImageView swChooseJumuahFirstCall = (ImageView) view.findViewById(R.id.sw_choose_if_first_call_jumuah);

            ViewUtils.setDrawableToImageView(swChooseIfNotification, isNotificationEnabled ? R.drawable.switch_compat_on : R.drawable.switch_compat_off);
            ViewUtils.setDrawableToImageView(swChooseJumuahFirstCall, isJumuahFirstCallEnabled ? R.drawable.switch_compat_on : R.drawable.switch_compat_off);

            swChooseIfNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isNotificationEnabled = !isNotificationEnabled;
                    ViewUtils.setDrawableToImageView(swChooseIfNotification, isNotificationEnabled ? R.drawable.switch_compat_on : R.drawable.switch_compat_off);
                    lblChooseIfNotification.setText(isNotificationEnabled ? R.string.prayer_notification_enabled : R.string.prayer_notification_disabled);
                    SharedPreferencesUtils.putNotifyPrayer(getMainActivity(), isNotificationEnabled);
                }
            });

            swChooseJumuahFirstCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isJumuahFirstCallEnabled = !isJumuahFirstCallEnabled;
                    ViewUtils.setDrawableToImageView(swChooseJumuahFirstCall, isJumuahFirstCallEnabled ? R.drawable.switch_compat_on : R.drawable.switch_compat_off);
                    lblChooseFirstCallJumuahDetail.setText(isJumuahFirstCallEnabled ? getMainActivity().getString(R.string.minutes_before_jumuah, preferedJumuahFirstCallDelay) : getActivity().getString(R.string.jumuah_first_call_disabled));
                    SharedPreferencesUtils.putJumuahFirstCallEnabled(getMainActivity(), isJumuahFirstCallEnabled);
                }
            });
        }
        else{
            SwitchCompat swChooseIfNotification = (SwitchCompat) view.findViewById(R.id.sw_choose_if_notification);
            SwitchCompat swChooseJumuahFirstCall = (SwitchCompat) view.findViewById(R.id.sw_choose_if_first_call_jumuah);

            swChooseIfNotification.setChecked(isNotificationEnabled);
            swChooseJumuahFirstCall.setChecked(isJumuahFirstCallEnabled);

            swChooseIfNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    lblChooseIfNotification.setText(isChecked ? R.string.prayer_notification_enabled : R.string.prayer_notification_disabled);
                    SharedPreferencesUtils.putNotifyPrayer(getMainActivity(), isChecked);
                }
            });

            swChooseJumuahFirstCall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    lblChooseFirstCallJumuahDetail.setText(isChecked ? getMainActivity().getString(R.string.minutes_before_jumuah, preferedJumuahFirstCallDelay) : getActivity().getString(R.string.jumuah_first_call_disabled));
                    SharedPreferencesUtils.putJumuahFirstCallEnabled(getMainActivity(), isChecked);
                }
            });
        }
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerTitle, ViewUtils.FONT_WEIGHT.BOLD);
        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerNotificationTitle, ViewUtils.FONT_WEIGHT.BOLD);

        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseConvention, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseAsr, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseHigherLatitude, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseIfNotification, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseNotificationSound, ViewUtils.FONT_WEIGHT.MEDIUM);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseFirstCallJumuah, ViewUtils.FONT_WEIGHT.MEDIUM);

        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseConventionDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseAsrDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseHigherLatitudeDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseNotificationSoundDetail, ViewUtils.FONT_WEIGHT.LIGHT);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseFirstCallJumuahDetail, ViewUtils.FONT_WEIGHT.LIGHT);

        boolean isConventionAutomatic = SharedPreferencesUtils.getConventionIsAutomatic(getActivity());
        boolean isSchoolAutomatic = SharedPreferencesUtils.getSchoolIsAutomatic(getActivity());
        boolean isHigherLatitudeAutomatic = SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(getActivity());

        int preferedConvention = SharedPreferencesUtils.getConventionValue(getMainActivity());
        int preferedAsr = SharedPreferencesUtils.getSchoolValue(getMainActivity());
        int preferedHigherLatitude = SharedPreferencesUtils.getHigherLatitudeModeValue(getMainActivity());
        int preferedNotificationSound = SharedPreferencesUtils.getSoundNotificationPrayer(getActivity());

        lblChooseFirstCallJumuahDetail.setText(isJumuahFirstCallEnabled ? getMainActivity().getString(R.string.minutes_before_jumuah, preferedJumuahFirstCallDelay) : getActivity().getString(R.string.jumuah_first_call_disabled));
        lblChooseIfNotification.setText(isNotificationEnabled ? R.string.prayer_notification_enabled : R.string.prayer_notification_disabled);


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

        lytChooseFirstCallJumuah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().redirectToJumuahDelayList();
            }
        });
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
