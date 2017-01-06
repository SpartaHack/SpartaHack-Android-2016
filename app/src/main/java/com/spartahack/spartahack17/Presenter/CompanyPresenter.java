package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.Model.Company;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;
import com.spartahack.spartahack17.View.CompanyView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public class CompanyPresenter extends RxPresenter<CompanyView, ArrayList<Company>>
        implements Comparator<Company> {

    public void loadCompanies() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        Observable<ArrayList<Company>> observable = SpartaHackAPIService.INSTANCE.getRestAdapter().getCompanies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscribe(observable);
    }

    @Override void onError(Throwable e) {
        if (isViewAttached()) {
            getView().onError(e.toString());
        }
    }

    @Override void onNext(ArrayList<Company> data) {
        Collections.sort(data, this);

        if (isViewAttached()) {
            getView().showCompanies(data);
        }
    }

    @Override public int compare(Company lhs, Company rhs) {
        if (lhs.getLevel() != rhs.getLevel())
            return lhs.getLevel() - rhs.getLevel();
        return rhs.getName().compareTo(lhs.getName());
    }
}
