package com.example.spartahack.spartahack2016.Adapters;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Fragment.TicketFragment;
import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder>{
    private ArrayList<Ticket> mDataset;
    public Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ticket) TextView ticket;
        @Bind(R.id.info) TextView info;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        }
    }

    public void add(int position, Ticket item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Ticket item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TicketAdapter(ArrayList<Ticket> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public TicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ticket, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TicketAdapter.ViewHolder holder, int position) {
        final Ticket ticket = mDataset.get(position);
        holder.ticket.setText("Ticket #" + position);
        holder.ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove(ticket);
                fragmentJump(ticket);
            }
        });
        holder.info.setText(mDataset.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    Bundle mBundle;
    TicketFragment mFragment;

    private void fragmentJump(Ticket mItemSelected) {
        mFragment = new TicketFragment();
        mBundle = new Bundle();
        mBundle.putSerializable("ticket", mItemSelected);
        mFragment.setArguments(mBundle);
        switchContent(R.id.container, mFragment);
    }


    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag);
        }

    }

}
