package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.ViewUtils;

public class SettingsFragment extends AbstractFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();

        TextView lblTitleActivateGeolocation = (TextView) getView().findViewById(R.id.lbl_title_activate_geolocation);

        ViewUtils.setTypefaceToTextView(getMainActivity(), lblTitleActivateGeolocation, ViewUtils.FONT_WEIGHT.REGULAR);
    }
}
