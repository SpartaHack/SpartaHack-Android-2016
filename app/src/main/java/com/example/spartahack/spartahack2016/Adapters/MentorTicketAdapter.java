package com.example.spartahack.spartahack2016.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by ryancasler on 2/2/16.
 */
public class MentorTicketAdapter extends RecyclerView.Adapter<MentorTicketAdapter.ViewHolder>{

    private ArrayList<Ticket> mDataset;
    public Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.title) TextView title;
        @Bind(R.id.description) TextView description;
        @Bind(R.id.ticket_layout) View layout;
        @Bind(R.id.status) TextView status;

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
    public MentorTicketAdapter(ArrayList<Ticket> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public MentorTicketAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ticket, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MentorTicketAdapter.ViewHolder holder, int position) {
        final Ticket ticket = mDataset.get(position);
        holder.title.setText(ticket.getSubject());
        holder.status.setText(ticket.getSubcategory());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MainActivity.StartViewTicketActivity(ticket));
            }
        });

        holder.description.setText(ticket.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
