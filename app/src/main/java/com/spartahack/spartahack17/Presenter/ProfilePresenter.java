package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Model.Login;
import com.spartahack.spartahack17.Model.Session;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;
import com.spartahack.spartahack17.View.ProfileView;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public class ProfilePresenter extends RxPresenter<ProfileView, Session> {

    private static final String TAG = "ProfilePresenter";

    public void attemptLogin(String email, String password) {
        Login login = new Login();
        login.email = email;
        login.password = password;

        Observable<Session> observable = SpartaHackAPIService.INSTANCE.getRestAdapter()
                .login(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        subscribe(observable);
    }

    public void logOut(String authToken) {
        if (isViewAttached()) {
            getView().showLoading();
        }

        SpartaHackAPIService.INSTANCE.getRestAdapter()
                .logout(authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(voidResponse -> {
                    if (isViewAttached()) {
                        getView().logOutSuccess();
                    }

                }, throwable -> {
                    Log.e(TAG, throwable.toString());
                    if (isViewAttached()) {
                        getView().logOutSuccess();
                    }
                });
    }

    @Override void onError(Throwable e) {
        if (isViewAttached()) {
            getView().onError(e.toString());
        }
    }

    @Override void onNext(Session data) {
        if (data != null) {
            if (isViewAttached()) {
                getView().loginSuccess(data);
            }
        } else {
            if (isViewAttached()) {
                getView().onError("Invalid Credentials");
            }
        }
    }
}
