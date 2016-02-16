package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.PrizeAdapter;
import com.example.spartahack.spartahack2016.Model.Prize;
import com.example.spartahack.spartahack16.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class PrizeFragment extends BaseFragment {

    /** Recycler view that displays all objects */
    @Bind(android.R.id.list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_prizes, container, false);
        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ParseAPIService.INSTANCE.getRestAdapter().getPrizes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.Prizes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.Prizes prizes) {
                        ArrayList<Prize> prizeList = prizes.prizes;

                        Collections.sort(prizeList, new Comparator<Prize>() {
                            @Override
                            public int compare(Prize lhs, Prize rhs) {
                                if (lhs.getSponsor().getLevel() - rhs.getSponsor().getLevel() != 0)
                                    return lhs.getSponsor().getLevel() - rhs.getSponsor().getLevel();
                                return lhs.getName().compareTo(rhs.getName());
                            }
                        });

                        recyclerView.setAdapter(new PrizeAdapter(getActivity(), prizeList));

                    }
                });



        return v;
    }

}
