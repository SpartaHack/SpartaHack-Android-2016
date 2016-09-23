package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Retrofit.ParseAPIService;
import com.spartahack.spartahack17.View.AnnouncementView;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 */
public class AnnouncementPresenter extends BasePresenter<AnnouncementView> implements Comparator<Announcement> {

    private static final String TAG = "AnnouncementPresenter";

    public void updateAnnouncements() {
        ParseAPIService.INSTANCE.getRestAdapter().getAnnouncements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(announcements -> {
                    ArrayList<Announcement> notifications = announcements.announcements;

                    Collections.sort(notifications, this);

                    if (isViewAttached()) {
                        getView().showAnnouncements(notifications);
                    }

                }, throwable -> Log.e(TAG, throwable.toString()));
    }

    @Override public int compare(Announcement lhs, Announcement rhs) {
        if (rhs.getPinned() && lhs.getPinned())
            return compare_time(lhs, rhs);
        else if (rhs.getPinned())
            return 1;
        else
            return compare_time(lhs, rhs);
    }

    private int compare_time(Announcement lhs, Announcement rhs) {
        int result = DateTimeComparator.getInstance().compare(lhs.getTime(), rhs.getTime());
        if (result == 1) {
            return -1;
        } else if (result == -1) {
            return 1;
        } else
            return result;
    }

}
