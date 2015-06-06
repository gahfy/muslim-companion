package net.gahfy.muslimcompanion.fragment;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.HigherLatitudeListAdapter;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;

/**
 * This fragment let the user choose a method for higher latitudes in the settings
 */
public class HigherLatitudeModeListFragment extends AbstractItemListFragment{
    @Override
    public ItemListAdapter getAdapter(RecyclerView recyclerView) {
        return new HigherLatitudeListAdapter(recyclerView, getMainActivity());
    }

    @Override
    public int getTitleResId() {
        return R.string.higher_latitude_method;
    }
}
