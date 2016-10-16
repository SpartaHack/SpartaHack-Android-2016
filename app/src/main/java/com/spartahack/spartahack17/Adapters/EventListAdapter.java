package com.spartahack.spartahack17.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spartahack.spartahack17.Model.Event;
import com.spartahack.spartahack17.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.SimpleViewHolder> {


    private final Context context;
    private final List<Event> data;


    public void add(Event e,int position) {
        position = position == -1 ? getItemCount()  : position;
        data.add(position, e);
        notifyItemInserted(position);
    }


    public void remove(int position){
        if (position < getItemCount()  ) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.time) TextView time;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.location) TextView location;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public EventListAdapter(Context context, ArrayList<Event> data) {
        this.context = context;
        if (data != null)
            this.data = data;
        else this.data = new ArrayList<>();
    }


    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.layout_event_item_1, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        Event e = data.get(position);

        holder.title.setText(e.getTitle());
        holder.description.setText(e.getDescription());
        holder.location.setText(e.getEventLocation());

        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
        holder.time.setText(format.format(e.getTime().toDate()));

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}