package com.spartahack.spartahack17.Fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spartahack.spartahack17.Adapters.PrizeAdapter;
import com.spartahack.spartahack17.Model.Prize;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.ParseAPIService;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PrizeFragment extends BaseFragment {

    private static final String TAG = "PrizeFragment";

    /** Recycler view that displays all objects */
    @Bind(android.R.id.list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_prizes, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ParseAPIService.INSTANCE.getRestAdapter().getPrizes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(prizes -> {
                        ArrayList<Prize> prizeList = prizes.prizes;

                        Collections.sort(prizeList, (lhs, rhs) -> {
                            if (lhs.getSponsor().getLevel() - rhs.getSponsor().getLevel() != 0)
                                return lhs.getSponsor().getLevel() - rhs.getSponsor().getLevel();
                            return lhs.getName().compareTo(rhs.getName());
                        });

                        recyclerView.setAdapter(new PrizeAdapter(getActivity(), prizeList));

                    }, throwable -> Log.e(TAG, throwable.toString()));



        return v;
    }

}
