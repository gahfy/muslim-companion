package net.gahfy.muslimcompanion.fragment;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ConventionListAdapter;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;

/**
 * This fragment let the user choose a convention in the settings
 */
public class ConventionListFragment extends AbstractItemListFragment{
    @Override
    public ItemListAdapter getAdapter(RecyclerView recyclerView) {
        return new ConventionListAdapter(recyclerView, getMainActivity());
    }

    @Override
    public int getTitleResId() {
        return R.string.convention;
    }
}
