package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.SpartaHackAPIService;
import com.spartahack.spartahack17.View.AnnouncementView;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 */
public class AnnouncementPresenter extends RxPresenter<AnnouncementView, GSONMock.Announcements> implements Comparator<Announcement> {
    private static final int RIGHT_FIRST = 1;
    private static final int LEFT_FIRST = -1;

    private static final String TAG = "AnnouncementPresenter";

    public void updateAnnouncements() {
        if (isViewAttached()) {
            getView().showLoading();
        }

        Observable observable = SpartaHackAPIService.INSTANCE.getRestAdapter().getAnnouncements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        subscribe(observable);
    }

    @Override public int compare(Announcement lhs, Announcement rhs) {
        if (!rhs.getPinned() && !lhs.getPinned())
            return DateTimeComparator.getInstance().compare(rhs.getTime(), lhs.getTime());
        else if (rhs.getPinned())
            return RIGHT_FIRST;
        return LEFT_FIRST;
    }

    @Override void onError(Throwable e) {
        if (isViewAttached()) {
            getView().onError(e.toString());
        }
    }

    @Override void onNext(GSONMock.Announcements data) {
        ArrayList<Announcement> notifications = data.announcements;
        Collections.sort(notifications, this);
        if (isViewAttached()) {
            getView().showAnnouncements(notifications);
        }
    }
}
