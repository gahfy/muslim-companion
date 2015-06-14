package net.gahfy.muslimcompanion.fragment;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;
import net.gahfy.muslimcompanion.adapter.JumuahDelayListAdapter;

/**
 * This fragment let the user choose a delay for first call of Jumu'ah in the settings
 */
public class JumuahDelayListFragment extends AbstractItemListFragment{
    @Override
    public ItemListAdapter getAdapter(RecyclerView recyclerView) {
        return new JumuahDelayListAdapter(recyclerView, getMainActivity());
    }

    @Override
    public int getTitleResId() {
        return R.string.jumuah_first_call;
    }
}
