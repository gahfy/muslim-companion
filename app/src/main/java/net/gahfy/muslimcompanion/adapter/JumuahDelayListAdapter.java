package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.AlarmUtils;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;

/**
 * This class is used as adapter for list of delay before Jumuah in the settings.
 */
public class JumuahDelayListAdapter extends ItemListAdapter {
    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity     The parent activity
     */
    public JumuahDelayListAdapter(RecyclerView recyclerView, MainActivity activity) {
        super(recyclerView, activity);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public boolean isSelected(int position) {
        return (SharedPreferencesUtils.getJumuahFirstCallDelay(activity)-10)/5 == position;
    }

    @Override
    public String getText(int position) {
        return activity.getString(R.string.minutes_before_jumuah, (position*5)+10);
    }

    @Override
    public void onClick(int position) {
        SharedPreferencesUtils.putJumuahFirstCallDelay(activity, (position*5)+10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                AlarmUtils.notifyAndSetNextAlarm(activity, false);
            }
        }).start();
    }
}
