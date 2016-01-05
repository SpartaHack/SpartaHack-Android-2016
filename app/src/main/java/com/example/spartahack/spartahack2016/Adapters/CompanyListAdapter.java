package com.example.spartahack.spartahack2016.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Model.Company;
import com.example.spartahack.spartahack2016.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ryancasler on 1/4/16.
 */
public class CompanyListAdapter extends BaseAdapter {

    private MainActivity context;
    private ArrayList<Company> companies;

    public CompanyListAdapter(MainActivity context, ArrayList<Company> companies) {
        this.context = context;
        this.companies = companies;
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Object getItem(int position) {
        return companies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        CompanyViewHolder vh;

        if (v == null){
            LayoutInflater inflater = context.getLayoutInflater();
            v = inflater.inflate(R.layout.layout_company_item, parent, false);
            vh = new CompanyViewHolder(v);
            v.setTag(vh);
        }else {
            vh = (CompanyViewHolder) v.getTag();
        }

        final Company company = (Company) getItem(position);
        vh.name.setText(company.getName());

        try {
            Glide.with(context).load(company.getUrl()).into(vh.logo);
        }catch (Exception e){
            Log.e("Company pic", e.toString() + " " + company.getPicUrl());
        }

        return v;
    }

    static class  CompanyViewHolder{
        @Bind(R.id.logo) ImageView logo;
        @Bind(R.id.name) TextView name;

        public CompanyViewHolder(View v) {ButterKnife.bind(this,v);}
    }
}

