package com.spartahack.spartahack17.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Adapters.AnnouncementAdapter;
import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Presenter.AnnouncementPresenter;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.View.AnnouncementView;

import java.util.ArrayList;

import butterknife.BindView;

import static com.google.firebase.analytics.FirebaseAnalytics.Event.BEGIN_CHECKOUT;

/**
 * Fragment that displays notifications in a list
 */
public class AnnouncementFragment extends MVPFragment<AnnouncementView, AnnouncementPresenter>
        implements AnnouncementView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "AnnouncementFragment";

    /** Listview that notificaitons are in */
    @BindView(android.R.id.list) ListView notificationList;
    @BindView(android.R.id.empty) TextView emptyView;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.error_message) TextView errorMessage;

    /** Swipe refresh layout for refreshing the list */
    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override int getLayout() {
        return R.layout.fragment_notification;
    }

    @Override boolean registerEventbus() {
        return false;
    }

    @NonNull @Override public AnnouncementPresenter createPresenter() {
        return new AnnouncementPresenter();
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAnalytics.getInstance(getActivity()).logEvent(BEGIN_CHECKOUT, null);
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
        AnnouncementAdapter adapter = new AnnouncementAdapter((MainActivity) getActivity(), announcements);
        notificationList.setAdapter(adapter);

        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        notificationList.setVisibility(View.VISIBLE);
    }

    @Override public void showLoading() {
        emptyView.setVisibility(View.GONE);
        notificationList.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override public void onError(String error) {
        emptyView.setVisibility(View.GONE);
        notificationList.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorMessage.setVisibility(View.VISIBLE);
    }
}
