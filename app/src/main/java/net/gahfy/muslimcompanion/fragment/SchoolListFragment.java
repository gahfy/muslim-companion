package net.gahfy.muslimcompanion.fragment;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;
import net.gahfy.muslimcompanion.adapter.SchoolListAdapter;

/**
 * This fragment let the user choose a school for asr in the settings
 */
public class SchoolListFragment extends AbstractItemListFragment{
    @Override
    public ItemListAdapter getAdapter(RecyclerView recyclerView) {
        return new SchoolListAdapter(recyclerView, getMainActivity());
    }

    @Override
    public int getTitleResId() {
        return R.string.asr_juristic_method;
    }
}