package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.AlarmUtils;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;

/**
 * This class is used as adapter for list of methods for higher latitudes in the settings.
 */
public class HigherLatitudeListAdapter extends ItemListAdapter {
    /** The list of methods for higher latitudes of the current adapter */
    PrayerTimesUtils.HigherLatitudeMode[] higherLatitudes;

    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity     The parent activity
     */
    public HigherLatitudeListAdapter(RecyclerView recyclerView, MainActivity activity) {
        super(recyclerView, activity);
        higherLatitudes = PrayerTimesUtils.HigherLatitudeMode.values();
    }

    @Override
    public int getItemCount() {
        return higherLatitudes.length+1;
    }

    @Override
    public boolean isSelected(int position) {
        if(position == 0)
            return SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(activity);
        // As position !=0
        // It should be not automatic (first line)
        // And the item saved in preference should be the one of the current position
        return (!SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(activity))
                && SharedPreferencesUtils.getHigherLatitudeModeValue(activity) == PrayerTimesUtils.getHigherLatitudeModePreferenceValue(higherLatitudes[position - 1]);
    }

    @Override
    public int getTextResId(int position) {
        if(position == 0)
            return R.string.automatic;
        else
            return PrayerTimesUtils.getHigherLatitudeModeResId(higherLatitudes[position - 1]);
    }

    @Override
    public boolean onClick(int position) {
        if(position == 0) {
            SharedPreferencesUtils.putHigherLatitudeModeIsAutomatic(activity, true);
            SharedPreferencesUtils.putHigherLatitudeMode(activity, -1);
        }
        else{
            SharedPreferencesUtils.putHigherLatitudeModeIsAutomatic(activity, false);
            SharedPreferencesUtils.putHigherLatitudeMode(activity, PrayerTimesUtils.getHigherLatitudeModePreferenceValue(higherLatitudes[position - 1]));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmUtils.notifyAndSetNextAlarm(activity, false);
            }
        }).start();
        return true;
    }
}
