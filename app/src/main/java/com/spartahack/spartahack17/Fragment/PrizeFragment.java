package com.spartahack.spartahack17.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.spartahack.spartahack17.Adapters.PrizeAdapter;
import com.spartahack.spartahack17.Model.Prize;
import com.spartahack.spartahack17.Presenter.PrizePresenter;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.View.PrizeView;

import java.util.ArrayList;

import butterknife.BindView;

public class PrizeFragment extends MVPFragment<PrizeView, PrizePresenter> implements PrizeView {

    private static final String TAG = "PrizeFragment";

    /**
     * Recycler view that displays all objects
     */
    @BindView(android.R.id.list) RecyclerView recyclerView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.error_message) TextView errorMessage;

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getMVPPresenter().updatePrizes();
    }

    @NonNull @Override public PrizePresenter createPresenter() {
        return new PrizePresenter();
    }

    @Override int getLayout() {
        return R.layout.fragment_prizes;
    }

    @Override boolean registerEventbus() {
        return false;
    }

    @Override public void showPrizes(ArrayList<Prize> prizes) {
        recyclerView.setAdapter(new PrizeAdapter(getActivity(), prizes));

        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override public void showLoading() {
        errorMessage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override public void onError(String error) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);

        Log.e(TAG, error);
    }
}
