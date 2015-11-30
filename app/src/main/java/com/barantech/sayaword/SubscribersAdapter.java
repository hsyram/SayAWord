package com.barantech.sayaword;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.barantech.sayaword.web.model.SubscribersResponse;

import java.util.ArrayList;

/**
 * Created by mary on 11/30/15.
 */
public class SubscribersAdapter extends RecyclerView.Adapter<SubscribersAdapter.ViewHolder> {
    private ArrayList<SubscribersResponse.Subscriber> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTvUser;
        public TextView mTvDate;
        public ViewHolder(View v) {
            super(v);
            mTvUser = (TextView) v.findViewById(R.id.tv_user);
            mTvDate = (TextView) v.findViewById(R.id.tv_date);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SubscribersAdapter(ArrayList<SubscribersResponse.Subscriber> subscribers) {
        mDataset = subscribers;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SubscribersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_subscriber, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTvUser.setText(mDataset.get(position).user_id);
        holder.mTvDate.setText(mDataset.get(position).modified_date);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
