package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ConventionListAdapter;
import net.gahfy.muslimcompanion.adapter.SchoolListAdapter;

public class SchoolListFragment extends AbstractFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_basic_list, container, false);

        RecyclerView listSchools = (RecyclerView) view.findViewById(R.id.list_settings);
        SchoolListAdapter adapter = new SchoolListAdapter(listSchools, getMainActivity());

        listSchools.setLayoutManager(new LinearLayoutManager(getActivity()));
        listSchools.setAdapter(adapter);

        getMainActivity().setTitle(R.string.asr_juristic_method);

        return view;
    }
}
