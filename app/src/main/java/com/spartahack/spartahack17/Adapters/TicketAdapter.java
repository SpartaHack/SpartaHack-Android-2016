package com.spartahack.spartahack17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Model.Ticket;
import com.spartahack.spartahack17.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder>{
    private final ArrayList<Ticket> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.ticket_layout) View layout;
        @BindView(R.id.status) TextView status;

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
        holder.status.setText(ticket.getStatus());

        holder.layout.setOnClickListener(view -> EventBus.getDefault().post(new MainActivity.StartViewTicketActivity(ticket)));

        holder.description.setText(ticket.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
