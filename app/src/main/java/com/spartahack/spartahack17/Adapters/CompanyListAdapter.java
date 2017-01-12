package com.spartahack.spartahack17.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.spartahack.spartahack17.Model.Company;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Utils.Base64Decoder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.SimpleViewHolder> {

    private final Context mContext;
    private final List<Company> mData;


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
        @BindView(R.id.logo) ImageView logo;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public CompanyListAdapter(Context context, ArrayList<Company> data) {
        mContext = context;
        if (data != null) mData = data;
        else mData = new ArrayList<>();
    }


    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.layout_company_item, parent, false);
        return new SimpleViewHolder(view);
    }


    @Override public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        final Company c = mData.get(position);

        if (c.getLogo_png_light() != null){
            Bitmap bitmap = Base64Decoder.decodeSampledBitmapFromBase64(c.getLogo_png_light(), 150, 150);
            holder.logo.setImageBitmap(bitmap);
        } else {
            Glide.with(mContext).load(R.drawable.logo_17).into(holder.logo);
        }
        holder.logo.setOnClickListener(view -> EventBus.getDefault().post(c.getUrl()));
    }


    @Override public int getItemCount() {
        return mData.size();
    }
}
