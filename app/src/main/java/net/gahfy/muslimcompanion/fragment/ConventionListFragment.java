package net.gahfy.muslimcompanion.fragment;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ConventionListAdapter;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;

/**
 * This fragment let the user choose a convention in the settings
 */
public class ConventionListFragment extends AbstractItemListFragment{
    /** The parent convention of conventions to display */
    PrayerTimesUtils.Convention parentConvention = null;

    /**
     * Set the parent convention of the conventions to display.
     * @param parentConvention The parent convention of the conventions to display to set
     */
    public void setParentConvention(PrayerTimesUtils.Convention parentConvention){
        this.parentConvention = parentConvention;
    }

    @Override
    public ItemListAdapter getAdapter(RecyclerView recyclerView) {
        ConventionListAdapter conventionListAdapter =  new ConventionListAdapter(recyclerView, getMainActivity());
        conventionListAdapter.setParentConvention(parentConvention);
        return conventionListAdapter;
    }

    @Override
    public int getTitleResId() {
        return R.string.convention;
    }
}
