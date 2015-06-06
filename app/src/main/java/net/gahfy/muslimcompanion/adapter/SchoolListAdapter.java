package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;

/**
 * This class is used as adapter for list of schools in the settings.
 */
public class SchoolListAdapter extends ItemListAdapter {
    /** The list of schools of the current adapter */
    PrayerTimesUtils.School[] schools;

    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity     The parent activity
     */
    public SchoolListAdapter(RecyclerView recyclerView, MainActivity activity) {
        super(recyclerView, activity);
        schools = PrayerTimesUtils.School.values();
    }

    @Override
    public int getItemCount() {
        return schools.length+1;
    }

    @Override
    public boolean isSelected(int position) {
        if(position == 0)
            return SharedPreferencesUtils.getSchoolIsAutomatic(activity);
        // As position !=0
        // It should be not automatic (first line)
        // And the item saved in preference should be the one of the current position
        return (!SharedPreferencesUtils.getSchoolIsAutomatic(activity))
                && SharedPreferencesUtils.getSchoolValue(activity) == PrayerTimesUtils.getSchoolPreferenceValue(schools[position - 1]);
    }

    @Override
    public int getTextResId(int position) {
        if(position == 0)
            return R.string.automatic;
        else
            return PrayerTimesUtils.getSchoolResId(schools[position - 1]);
    }

    @Override
    public void onClick(int position) {
        if(position == 0) {
            SharedPreferencesUtils.putSchoolIsAutomatic(activity, true);
            SharedPreferencesUtils.putSchool(activity, -1);
        }
        else{
            SharedPreferencesUtils.putSchoolIsAutomatic(activity, false);
            SharedPreferencesUtils.putSchool(activity, PrayerTimesUtils.getSchoolPreferenceValue(schools[position - 1]));
        }
    }
}
