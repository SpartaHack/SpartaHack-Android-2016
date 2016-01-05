package com.example.spartahack.spartahack2016.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Model.Company;
import com.example.spartahack.spartahack2016.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleCompanyAdapter extends RecyclerView.Adapter<SimpleCompanyAdapter.SimpleViewHolder> {


    private final Context mContext;
    private List<Company> mData;


    public void add(Company c,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData.add(position ,c);
        notifyItemInserted(position);
    }


    public void remove(int position){
        if (position < getItemCount()  ) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.logo) ImageView logo;
        @Bind(R.id.name) TextView name;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public SimpleCompanyAdapter(Context context, ArrayList<Company> data) {
        mContext = context;
        if (data != null)
            mData = data;
        else mData = new ArrayList<>();
    }


    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_company_item, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.name.setText(mData.get(position).getName());
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext,"Position ="+position,Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}
