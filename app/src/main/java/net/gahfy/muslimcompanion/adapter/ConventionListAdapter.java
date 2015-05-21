package net.gahfy.muslimcompanion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.lblConventionName.setText(PrayerTimesUtils.getConventionNameResId(conventions[position]));
    }

    @Override
    public int getItemCount() {
        return conventions.length;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblConventionName;

        public ViewHolder(View itemView) {
            super(itemView);

            lblConventionName = (TextView) itemView.findViewById(R.id.lbl_item);
        }
    }

    private class ItemClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildPosition(view);
            SharedPreferencesUtils.putConvention(activity, PrayerTimesUtils.getConventionPreferenceValue(conventions[itemPosition]));
            activity.onBackPressed();
        }
    }
}
