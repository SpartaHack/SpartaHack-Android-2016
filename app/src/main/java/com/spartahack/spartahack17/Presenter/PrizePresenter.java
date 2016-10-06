package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Adapters.PrizeAdapter;
import com.spartahack.spartahack17.Model.Prize;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.ParseAPIService;
import com.spartahack.spartahack17.View.PrizeView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by memuyskens on 10/5/16.
 * SpartaHack-Android
 */
public class PrizePresenter extends RxPresenter<PrizeView, GSONMock.Prizes> implements Comparator<Prize> {

    private static final String TAG = "PrizePresenter";

    public void updatePrizes() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        Observable observable = ParseAPIService.INSTANCE.getRestAdapter().getPrizes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        subscribe(observable);
    }

    @Override public int compare(Prize lhs, Prize rhs) {
        if (lhs.getSponsor().getLevel() - rhs.getSponsor().getLevel() != 0)
            return lhs.getSponsor().getLevel() - rhs.getSponsor().getLevel();
        return lhs.getName().compareTo(rhs.getName());
    }

    @Override void onError(Throwable e) {
        if (isViewAttached()) {
            getView().onError(e.toString());
        }
    }

    @Override void onNext(GSONMock.Prizes data) {
        ArrayList<Prize> prizes = data.prizes;
        Collections.sort(prizes, this);
        if (isViewAttached()) {
            getView().showPrizes(prizes);
        }
    }
}
