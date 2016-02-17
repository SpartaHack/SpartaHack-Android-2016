package com.example.spartahack.spartahack16.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spartahack.spartahack16.R;
import com.example.spartahack.spartahack16.Model.Prize;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ryancasler on 1/5/16.
 */
public class PrizeAdapter  extends RecyclerView.Adapter<PrizeAdapter.SimpleViewHolder> {


    private final Context context;
    private List<Prize> data;


    public void add(Prize p,int position) {
        position = position == -1 ? getItemCount()  : position;
        data.add(position, p);
        notifyItemInserted(position);
    }


    public void remove(int position){
        if (position < getItemCount()  ) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title) TextView title;
        @Bind(R.id.sponsor) TextView sponsor;
        @Bind(R.id.description) TextView description;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public PrizeAdapter(Context context, ArrayList<Prize> data) {
        this.context = context;
        if (data != null)
            this.data = data;
        else this.data = new ArrayList<>();
    }


    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.layout_prize_item, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        Prize p = data.get(position);

        holder.title.setText(p.getName());
        holder.description.setText(p.getDescription());
//        holder.sponsor.setText(String.format(context.getResources().getString(R.string.sponsored_by), p.getSponsor().getName()));
        holder.sponsor.setText(p.getSponsor().getName());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}