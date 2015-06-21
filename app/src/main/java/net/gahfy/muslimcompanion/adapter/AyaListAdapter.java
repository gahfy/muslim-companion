package net.gahfy.muslimcompanion.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.StringUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;

public class AyaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String[] ayaList;

    public AyaListAdapter(String[] ayaList){
        this.ayaList = ayaList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aya, parent, false);
        ViewHolder holder = new ViewHolder(v);
        ViewUtils.setTypefaceToTextView(parent.getContext(), holder.lblOriginalAya, ViewUtils.FONT_WEIGHT.QURAN_ARABIC);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.lblOriginalAya.setText(ayaList[position].concat(" \uFD3F").concat(StringUtils.convertToArabicNumber(position+1)).concat("\uFD3E"));
    }

    @Override
    public int getItemCount() {
        return ayaList.length;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView lblOriginalAya;

        public ViewHolder(View itemView) {
            super(itemView);

            lblOriginalAya = (TextView) itemView.findViewById(R.id.lbl_original_aya);
        }
    }
}
