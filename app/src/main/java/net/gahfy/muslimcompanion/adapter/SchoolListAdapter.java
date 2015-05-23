package net.gahfy.muslimcompanion.adapter;

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

public class SchoolListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    PrayerTimesUtils.School[] schools;
    RecyclerView recyclerView;
    MainActivity activity;

    public SchoolListAdapter(RecyclerView recyclerView, MainActivity activity){
        schools = PrayerTimesUtils.School.values();
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        v.setOnClickListener(new ItemClickedListener());
        ViewHolder holder = new ViewHolder(v);
        ViewUtils.setTypefaceToTextView(activity, holder.lblSchoolName, ViewUtils.FONT_WEIGHT.REGULAR);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if(position == 0){
            holder.lblSchoolName.setText(R.string.automatic);
        }
        else {
            holder.lblSchoolName.setText(PrayerTimesUtils.getSchoolResId(schools[position-1]));
        }
    }

    @Override
    public int getItemCount() {
        return schools.length+1;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblSchoolName;

        public ViewHolder(View itemView) {
            super(itemView);

            lblSchoolName = (TextView) itemView.findViewById(R.id.lbl_item);
        }
    }

    private class ItemClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildPosition(view);
            if(itemPosition == 0) {
                SharedPreferencesUtils.putSchoolIsAutomatic(activity, true);
                SharedPreferencesUtils.putSchool(activity, -1);
            }
            else{
                SharedPreferencesUtils.putSchoolIsAutomatic(activity, false);
                SharedPreferencesUtils.putSchool(activity, PrayerTimesUtils.getSchoolPreferenceValue(schools[itemPosition-1]));
            }
            activity.onBackPressed();
        }
    }
}
