package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.PrayerTimesUtils;
import net.gahfy.muslimcompanion.utils.SharedPreferencesUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

public class HigherLatitudeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    PrayerTimesUtils.HigherLatitudeMode[] higherLatitudeModes;
    RecyclerView recyclerView;
    MainActivity activity;

    public HigherLatitudeListAdapter(RecyclerView recyclerView, MainActivity activity){
        higherLatitudeModes = PrayerTimesUtils.HigherLatitudeMode.values();
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        v.setOnClickListener(new ItemClickedListener());
        ViewHolder holder = new ViewHolder(v);
        ViewUtils.setTypefaceToTextView(activity, holder.lblHigherLatitudeName, ViewUtils.FONT_WEIGHT.REGULAR);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if(position == 0){
            if(SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(activity))
                holder.imgTick.setVisibility(View.VISIBLE);
            else
                holder.imgTick.setVisibility(View.INVISIBLE);
            holder.lblHigherLatitudeName.setText(R.string.automatic);
        }
        else {
            if(!SharedPreferencesUtils.getHigherLatitudeModeIsAutomatic(activity)) {
                if (SharedPreferencesUtils.getHigherLatitudeModeValue(activity) == PrayerTimesUtils.getHigherLatitudeModePreferenceValue(higherLatitudeModes[position-1]))
                    holder.imgTick.setVisibility(View.VISIBLE);
                else
                    holder.imgTick.setVisibility(View.INVISIBLE);
            }
            else{
                holder.imgTick.setVisibility(View.INVISIBLE);
            }
            holder.lblHigherLatitudeName.setText(PrayerTimesUtils.getHigherLatitudeModeResId(higherLatitudeModes[position-1]));
        }
    }

    @Override
    public int getItemCount() {
        return higherLatitudeModes.length+1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblHigherLatitudeName;
        ImageView imgTick;

        public ViewHolder(View itemView) {
            super(itemView);

            lblHigherLatitudeName = (TextView) itemView.findViewById(R.id.lbl_item);
            imgTick = (ImageView) itemView.findViewById(R.id.img_tick_item);
        }
    }

    private class ItemClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildPosition(view);
            if(itemPosition == 0) {
                SharedPreferencesUtils.putHigherLatitudeModeIsAutomatic(activity, true);
                SharedPreferencesUtils.putHigherLatitudeMode(activity, -1);
            }
            else{
                SharedPreferencesUtils.putHigherLatitudeModeIsAutomatic(activity, false);
                SharedPreferencesUtils.putHigherLatitudeMode(activity, PrayerTimesUtils.getHigherLatitudeModePreferenceValue(higherLatitudeModes[itemPosition-1]));
            }
            activity.onBackPressed();
        }
    }
}
