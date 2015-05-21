package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        RelativeLayout lytChooseConvention = (RelativeLayout) view.findViewById(R.id.lyt_choose_convention);
        RelativeLayout lytChooseAsr = (RelativeLayout) view.findViewById(R.id.lyt_choose_asr);
        RelativeLayout lytChooseHigherLatitude = (RelativeLayout) view.findViewById(R.id.lyt_choose_higher_latitude);

        TextView lblPrayerTitle = (TextView) view.findViewById(R.id.lbl_prayer_title);
        TextView lblChooseConvention = (TextView) view.findViewById(R.id.lbl_choose_convention);

        TextView lblChooseConventionDetail= (TextView) view.findViewById(R.id.lbl_choose_convention_details);
        TextView lblChooseAsrDetail= (TextView) view.findViewById(R.id.lbl_choose_asr_details);
        TextView lblChooseHigherLatitudeDetail= (TextView) view.findViewById(R.id.lbl_choose_higher_latitude_details);

        int preferedConvention = SharedPreferencesUtils.getConventionValue(getMainActivity());
        int preferedAsr = SharedPreferencesUtils.getSchoolValue(getMainActivity());
        int preferedHigherLatitude = SharedPreferencesUtils.getHigherLatitudeModeValue(getMainActivity());


        lblChooseConventionDetail.setText(R.string.automatic);
        lblChooseAsrDetail.setText(R.string.automatic);
        lblChooseHigherLatitudeDetail.setText(R.string.automatic);

        if(preferedConvention != -1){
            lblChooseConventionDetail.setText(PrayerTimesUtils.getConventionNameResId(PrayerTimesUtils.getConventionFromPreferenceValue(preferedConvention)));
        }
        if(preferedAsr != -1){
            lblChooseAsrDetail.setText(PrayerTimesUtils.getSchoolResId(PrayerTimesUtils.getSchoolFromPreferenceValue(preferedAsr)));
        }
        if(preferedHigherLatitude != -1){
            lblChooseHigherLatitudeDetail.setText(PrayerTimesUtils.getHigherLatitudeModeResId(PrayerTimesUtils.getHigherLatitudeModeFromPreferenceValue(preferedHigherLatitude)));
        }

        ViewUtils.setTypefaceToTextView(getActivity(), lblPrayerTitle, ViewUtils.FONT_WEIGHT.BOLD);
        ViewUtils.setTypefaceToTextView(getActivity(), lblChooseConvention, ViewUtils.FONT_WEIGHT.REGULAR);

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

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        getMainActivity().setTitle(R.string.settings);
    }
}
