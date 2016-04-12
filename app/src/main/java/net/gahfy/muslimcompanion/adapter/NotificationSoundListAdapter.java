package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;

/**
 * This class is used as adapter for list of notification sounds in the settings.
 */
public class NotificationSoundListAdapter extends ItemListAdapter {
    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity     The parent activity
     */
    public NotificationSoundListAdapter(RecyclerView recyclerView, MainActivity activity) {
        super(recyclerView, activity);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public boolean isSelected(int position) {
        return SharedPreferencesUtils.getSoundNotificationPrayer(activity) == position;
    }

    @Override
    public int getTextResId(int position) {
        switch (position){
            case 0:
                return R.string.sound_none;
            case 1:
                return R.string.sound_android_default;
        }
        return R.string.sound_adhan;
    }

    @Override
    public boolean onClick(int position) {
        SharedPreferencesUtils.putSoundNotificationPrayer(activity, position);
        return true;
    }
}
