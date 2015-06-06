package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;

/**
 * This class is used as adapter for list of conventions in the settings.
 */
public class ConventionListAdapter extends ItemListAdapter {
    /** The list of conventions of the current adapter */
    PrayerTimesUtils.Convention[] conventions;

    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity     The parent activity
     */
    public ConventionListAdapter(RecyclerView recyclerView, MainActivity activity) {
        super(recyclerView, activity);
        conventions = PrayerTimesUtils.Convention.values();
    }

    @Override
    public int getItemCount() {
        return conventions.length+1;
    }

    @Override
    public boolean isSelected(int position) {
        if(position == 0)
            return SharedPreferencesUtils.getConventionIsAutomatic(activity);
        // As position !=0
        // It should be not automatic (first line)
        // And the item saved in preference should be the one of the current position
        return (!SharedPreferencesUtils.getConventionIsAutomatic(activity))
                && SharedPreferencesUtils.getConventionValue(activity) == PrayerTimesUtils.getConventionPreferenceValue(conventions[position - 1]);
    }

    @Override
    public int getTextResId(int position) {
        if(position == 0)
            return R.string.automatic;
        else
            return PrayerTimesUtils.getConventionNameResId(conventions[position - 1]);
    }

    @Override
    public void onClick(int position) {
        if(position == 0) {
            SharedPreferencesUtils.putConventionIsAutomatic(activity, true);
            SharedPreferencesUtils.putConvention(activity, -1);
        }
        else{
            SharedPreferencesUtils.putConventionIsAutomatic(activity, false);
            SharedPreferencesUtils.putConvention(activity, PrayerTimesUtils.getConventionPreferenceValue(conventions[position - 1]));
        }
    }
}
