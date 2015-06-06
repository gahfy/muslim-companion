package net.gahfy.muslimcompanion.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;

/**
 * This class is used to handle fragment with list of items
 */
public abstract class AbstractItemListFragment extends AbstractFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_basic_list, container, false);

        RecyclerView listSettings = (RecyclerView) view.findViewById(R.id.list_settings);

        listSettings.setLayoutManager(new LinearLayoutManager(getActivity()));
        listSettings.setAdapter(getAdapter(listSettings));

        getMainActivity().setTitle(getTitleResId());

        return view;
    }

    /**
     * Returns the adapter to apply to the RecyclerView.
     * @param recyclerView the RecyclerView to which the adapter should be applied
     * @return the adapter to apply to the RecyclerView
     */
    public abstract ItemListAdapter getAdapter(RecyclerView recyclerView);

    /**
     * Returns the resource id of the title of the fragment.
     * @return the resource id of the title of the fragment
     */
    public abstract int getTitleResId();
}
