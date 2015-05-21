package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.HigherLatitudeListAdapter;
import net.gahfy.muslimcompanion.adapter.SchoolListAdapter;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;

public class HigherLatitudeModeListFragment extends AbstractFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_basic_list, container, false);

        RecyclerView listHigherLatitudeMode = (RecyclerView) view.findViewById(R.id.list_settings);
        HigherLatitudeListAdapter adapter = new HigherLatitudeListAdapter(listHigherLatitudeMode, getMainActivity());

        listHigherLatitudeMode.setLayoutManager(new LinearLayoutManager(getActivity()));
        listHigherLatitudeMode.setAdapter(adapter);

        return view;
    }
}
