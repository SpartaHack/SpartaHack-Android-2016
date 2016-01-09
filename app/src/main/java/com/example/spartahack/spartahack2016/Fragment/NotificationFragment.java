package com.example.spartahack.spartahack2016.Fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Adapters.AnnouncementAdapter;
import com.example.spartahack.spartahack2016.Model.Announcement;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Fragment that displays notifications in a list
 */
public class NotificationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    /** Listview that notificaitons are in */
    @Bind(android.R.id.list) ListView notificationList;
    @Bind(android.R.id.empty) TextView emptyView;

    /** Swipe refresh layout for refreshing the list */
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ButterKnife.bind(this, view);

        updateNotifications();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light);

        notificationList.setEmptyView(emptyView);

        return view;
    }

    private void updateNotifications() {
        ParseAPIService.INSTANCE.getRestAdapter().getAnnouncements()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.Announcements>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.Announcements announcements) {
                        ArrayList<Announcement> notifications = announcements.announcements;

                        Collections.sort(notifications, new Comparator<Announcement>() {
                            @Override
                            public int compare(Announcement lhs, Announcement rhs) {
                                if (rhs.getPinned() && lhs.getPinned())
                                    return DateTimeComparator.getInstance().compare(lhs.getTime(), rhs.getTime());
                                else if (rhs.getPinned())
                                    return 1;
                                else
                                    return -1;
                            }
                        });

                        // create adapter and add to arraylist
                        AnnouncementAdapter adapter = new AnnouncementAdapter((MainActivity) getActivity(), notifications);
                        notificationList.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void onRefresh() {
        refresh(true);
    }

    /**
     * Refreshes the list. Can either load form network if forced or load from Realm if not forced
     * @param force if the refresh is forced or not
     */
    private void refresh(boolean force){
        updateNotifications();
        swipeRefreshLayout.setRefreshing(false);
    }

}
