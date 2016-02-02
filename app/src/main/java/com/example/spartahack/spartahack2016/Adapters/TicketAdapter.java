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
        @Bind(R.id.title) TextView title;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.ticket_layout) View layout;


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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TicketAdapter.ViewHolder holder, int position) {
        final Ticket ticket = mDataset.get(position);
        holder.title.setText(ticket.getSubject());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove(ticket);
                fragmentJump(ticket);
            }
        });

        holder.description.setText(ticket.getDescription());
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
        mBundle.putSerializable(TicketFragment.I_TICKET, mItemSelected);
        mFragment.setArguments(mBundle);
        switchContent(R.id.container, mFragment);
    }


    public void switchContent(int id, Fragment fragment) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.switchContent(id, fragment);
        }

    }

}
