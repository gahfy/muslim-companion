package net.gahfy.muslimcompanion.adapter;

import android.content.Context;
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

public class ConventionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    PrayerTimesUtils.Convention[] conventions;
    RecyclerView recyclerView;
    MainActivity activity;

    public ConventionListAdapter(RecyclerView recyclerView, MainActivity activity){
        conventions = PrayerTimesUtils.Convention.values();
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        v.setOnClickListener(new ItemClickedListener());
        ViewHolder holder = new ViewHolder(v);
        ViewUtils.setTypefaceToTextView(activity, holder.lblConventionName, ViewUtils.FONT_WEIGHT.REGULAR);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if(position == 0){
            if(SharedPreferencesUtils.getConventionIsAutomatic(activity))
                holder.imgTick.setVisibility(View.VISIBLE);
            else
                holder.imgTick.setVisibility(View.INVISIBLE);
            holder.lblConventionName.setText(R.string.automatic);
        }
        else {
            if(!SharedPreferencesUtils.getConventionIsAutomatic(activity)) {
                if (SharedPreferencesUtils.getConventionValue(activity) == PrayerTimesUtils.getConventionPreferenceValue(conventions[position-1]))
                    holder.imgTick.setVisibility(View.VISIBLE);
                else
                    holder.imgTick.setVisibility(View.INVISIBLE);
            }
            else{
                holder.imgTick.setVisibility(View.INVISIBLE);
            }
            holder.lblConventionName.setText(PrayerTimesUtils.getConventionNameResId(conventions[position-1]));
        }
    }

    @Override
    public int getItemCount() {
        return conventions.length+1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblConventionName;
        ImageView imgTick;

        public ViewHolder(View itemView) {
            super(itemView);

            lblConventionName = (TextView) itemView.findViewById(R.id.lbl_item);
            imgTick = (ImageView) itemView.findViewById(R.id.img_tick_item);
        }
    }

    private class ItemClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildPosition(view);
            if(itemPosition == 0) {
                SharedPreferencesUtils.putConventionIsAutomatic(activity, true);
                SharedPreferencesUtils.putConvention(activity, -1);
            }
            else{
                SharedPreferencesUtils.putConventionIsAutomatic(activity, false);
                SharedPreferencesUtils.putConvention(activity, PrayerTimesUtils.getConventionPreferenceValue(conventions[itemPosition - 1]));
            }
            activity.onBackPressed();
        }
    }
}
