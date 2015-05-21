package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ConventionListAdapter;

public class ConventionListFragment extends AbstractFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_basic_list, container, false);

        RecyclerView listConvention = (RecyclerView) view.findViewById(R.id.list_settings);
        ConventionListAdapter adapter = new ConventionListAdapter(listConvention, getMainActivity());

        listConvention.setLayoutManager(new LinearLayoutManager(getActivity()));
        listConvention.setAdapter(adapter);

        return view;
    }
}
