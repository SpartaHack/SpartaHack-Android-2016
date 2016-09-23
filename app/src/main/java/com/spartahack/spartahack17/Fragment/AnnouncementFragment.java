package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Adapters.AnnouncementAdapter;
import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Presenter.AnnouncementPresenter;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.View.AnnouncementView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Fragment that displays notifications in a list
 */
public class AnnouncementFragment extends MVPFragment<AnnouncementView, AnnouncementPresenter>
        implements AnnouncementView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "AnnouncementFragment";

    /** Listview that notificaitons are in */
    @Bind(android.R.id.list) ListView notificationList;
    @Bind(android.R.id.empty) TextView emptyView;

    /** Swipe refresh layout for refreshing the list */
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override int getLayout() {
        return R.layout.fragment_notification;
    }

    @Override boolean registerEventbus() {
        return false;
    }

    @NonNull @Override public AnnouncementPresenter createPresenter() {
        return new AnnouncementPresenter();
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.background);

        notificationList.setEmptyView(emptyView);

        refresh(true);
    }

    @Override public void onRefresh() {
        refresh(true);
    }

    /**
     * Refreshes the list. Can either load form network if forced or load from Realm if not forced
     * @param force if the refresh is forced or not
     */
    private void refresh(boolean force){
        getMVPPresenter().updateAnnouncements();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override public void showAnnouncements(ArrayList<Announcement> announcements) {
        // create adapter and add to arraylist
        AnnouncementAdapter adapter = new AnnouncementAdapter((MainActivity) getActivity(), announcements);
        notificationList.setAdapter(adapter);
    }
}
