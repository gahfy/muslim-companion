package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.AlarmUtils;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;

/**
 * This class is used as adapter for list of conventions in the settings.
 */
public class ConventionListAdapter extends ItemListAdapter {
    /** The parent convention of conventions to display */
    PrayerTimesUtils.Convention parentConvention = null;
    /** The list of conventions of the current adapter */
    PrayerTimesUtils.Convention[] conventions;

    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity     The parent activity
     */
    public ConventionListAdapter(RecyclerView recyclerView, MainActivity activity) {
        super(recyclerView, activity);
    }

    /**
     * Set the parent convention of the conventions to display.
     * @param parentConvention The parent convention of the conventions to display to set
     */
    public void setParentConvention(PrayerTimesUtils.Convention parentConvention){
        this.parentConvention = parentConvention;
        conventions = PrayerTimesUtils.getChildConventions(parentConvention);
    }

    @Override
    public int getItemCount() {
        return conventions.length+1;
    }

    @Override
    public boolean isSelected(int position) {
        int positionAdd = 0;
        if(parentConvention != null)
            positionAdd = 1;
        if(position+positionAdd == 0)
            return SharedPreferencesUtils.getConventionIsAutomatic(activity);
        // As position !=0
        // It should be not automatic (first line)
        // And the item saved in preference should be the one of the current position
        return (!SharedPreferencesUtils.getConventionIsAutomatic(activity))
              && (SharedPreferencesUtils.getConventionValue(activity) == PrayerTimesUtils.getConventionPreferenceValue(conventions[position+positionAdd - 1])
              || PrayerTimesUtils.getParentConvention(PrayerTimesUtils.getConventionFromPreferenceValue(SharedPreferencesUtils.getConventionValue(activity))) == conventions[position+positionAdd-1]);
    }

    @Override
    public int getTextResId(int position) {
        int positionAdd = 0;
        if(parentConvention != null)
            positionAdd = 1;
        if(position+positionAdd == 0)
            return R.string.automatic;
        else
            return PrayerTimesUtils.getConventionNameResId(conventions[position+positionAdd - 1]);
    }

    @Override
    public boolean onClick(int position) {
        int positionAdd = 0;
        if(parentConvention != null)
            positionAdd = 1;
        if(position+positionAdd == 0) {
            SharedPreferencesUtils.putConventionIsAutomatic(activity, true);
            SharedPreferencesUtils.putConvention(activity, -1);
        }
        else if(PrayerTimesUtils.hasChildren(conventions[position+positionAdd - 1])){
            activity.redirectToConventionList(conventions[position+positionAdd - 1]);
        }
        else{
            SharedPreferencesUtils.putConventionIsAutomatic(activity, false);
            SharedPreferencesUtils.putConvention(activity, PrayerTimesUtils.getConventionPreferenceValue(conventions[position+positionAdd - 1]));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmUtils.notifyAndSetNextAlarm(activity, false);
            }
        }).start();
        if(position+positionAdd - 1 == -1)
            return true;
        return !PrayerTimesUtils.hasChildren(conventions[position+positionAdd - 1]);
    }
}
