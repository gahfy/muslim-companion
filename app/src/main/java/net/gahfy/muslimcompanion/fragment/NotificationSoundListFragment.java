package net.gahfy.muslimcompanion.fragment;

import android.support.v7.widget.RecyclerView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.adapter.ItemListAdapter;
import net.gahfy.muslimcompanion.adapter.NotificationSoundListAdapter;

/**
 * This fragment let the user choose a notification sound in the settings
 */
public class NotificationSoundListFragment extends AbstractItemListFragment{
    @Override
    public ItemListAdapter getAdapter(RecyclerView recyclerView) {
        return new NotificationSoundListAdapter(recyclerView, getMainActivity());
    }

    @Override
    public int getTitleResId() {
        return R.string.prayer_notification_sound;
    }
}