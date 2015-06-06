package net.gahfy.muslimcompanion.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.ViewUtils;

/**
 * This class is used to handle adapter for list of items
 */
public abstract class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    /** The RecyclerView on which the adapter is applied */
    protected RecyclerView recyclerView;
    /** The parent activity */
    protected MainActivity activity;

    /**
     * Returns whether the current item is selected or not.
     * @param position the position of the item
     * @return whether the current item is selected or not
     */
    public abstract boolean isSelected(int position);

    /**
     * Returns the res id of the text of the current item.
     * @param position the position of the item
     * @return the res id of the text the current item
     */
    public abstract int getTextResId(int position);

    /**
     * This method is called when a view is clicked.
     * @param position The position of the view that has been clicked
     */
    public abstract void onClick(int position);

    /**
     * Instantiates a new adapter
     * @param recyclerView The RecyclerView on which the adapter is applied
     * @param activity The parent activity
     */
    public ItemListAdapter(RecyclerView recyclerView, MainActivity activity){
        this.recyclerView = recyclerView;
        this.activity = activity;
    }

    /**
     * The ViewHolder of the current adapter
     */
    private class ViewHolder extends RecyclerView.ViewHolder{
        /** The TextView which contains the text of the item */
        TextView lblItem;
        /** The tick, which is visible when the item is selected */
        ImageView imgTickItem;

        /**
         * Instantiates a new ViewHolder
         * @param itemView the view from which to generate the ViewHolder
         */
        public ViewHolder(View itemView) {
            super(itemView);

            lblItem = (TextView) itemView.findViewById(R.id.lbl_item);
            imgTickItem = (ImageView) itemView.findViewById(R.id.img_tick_item);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;

        holder.imgTickItem.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.lblItem.setText(getTextResId(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        v.setOnClickListener(new ItemClickedListener());
        ViewHolder holder = new ViewHolder(v);
        ViewUtils.setTypefaceToTextView(activity, holder.lblItem, ViewUtils.FONT_WEIGHT.REGULAR);
        return holder;
    }

    private class ItemClickedListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildPosition(view);
            ItemListAdapter.this.onClick(itemPosition);
            // We call this to go back to previous fragment
            activity.onBackPressed();
        }
    }
}
