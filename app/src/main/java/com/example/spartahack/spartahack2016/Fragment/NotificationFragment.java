package com.example.spartahack.spartahack2016.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Adapters.NotificationAdapter;
import com.example.spartahack.spartahack2016.Model.PushNotification;
import com.example.spartahack.spartahack2016.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.notificaiton_list) ListView notificationList;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ButterKnife.bind(this, view);

        updateNotifications();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light);

        return view;
    }

    private void updateNotifications(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PushNotification> results = realm.where(PushNotification.class).findAllSorted("pinned", Sort.DESCENDING);
        List<PushNotification> notifications = results.subList(0, results.size());

        NotificationAdapter adapter = new NotificationAdapter((MainActivity) getActivity(), R.layout.layout_notificaiton_item, notifications);
        notificationList.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        refresh(true);
    }

    private void refresh(boolean force){
        updateNotifications();
        swipeRefreshLayout.setRefreshing(false);
    }


}
