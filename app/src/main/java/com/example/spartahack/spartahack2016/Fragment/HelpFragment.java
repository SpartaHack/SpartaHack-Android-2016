package com.example.spartahack.spartahack2016.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Activity.CreateTicketActivity;
import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Adapters.TicketAdapter;
import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.PushNotificationReceiver;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class HelpFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycler) RecyclerView ticketView;
    @Bind(R.id.no_user) LinearLayout noUser;
    @Bind(R.id.user) RelativeLayout userExists;
    @Bind(R.id.no_tix) TextView noTix;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private final String I_EXTRA_FROM = "from help";

    /** Arguments passed in from Helpdesk fragment from main activity from pushreciever*/
    private Bundle args;

    private ArrayList<Ticket> tickets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        registerEventBus = true;

        tickets = new ArrayList<>();

        ButterKnife.bind(this, view);

        // get any Arguments passed in from Helpdesk fragment from main activity from pushreciever
        args = this.getArguments();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.background);

        if (ParseUser.getCurrentUser() == null) {
            noUser.setVisibility(View.VISIBLE);
            userExists.setVisibility(View.GONE);
            noTix.setVisibility(View.GONE);
        } else {
            noUser.setVisibility(View.GONE);
            userExists.setVisibility(View.VISIBLE);
            noTix.setVisibility(View.GONE);

            //RecyclerView
            ticketView.setHasFixedSize(true);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            ticketView.setLayoutManager(mLayoutManager);

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    public void onEvent(Ticket ticket) {
        // add ticket and update recyclerview
        tickets.add(0, ticket);
        setRecyclerViewSections();
    }

    @OnClick(R.id.fab)
    void viewTicket() {
        MainActivity activity = ((MainActivity) getActivity());
        activity.startActivity(new Intent(activity, CreateTicketActivity.class));
    }

    @OnClick(R.id.login)
    void onLogin() {
        MainActivity activity = ((MainActivity) getActivity());
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ProfileFragment.I_EXTRA_FROM, ProfileFragment.I_EXTRA_FROM);
        fragment.setArguments(bundle);
        activity.switchContent(R.id.container, fragment);
    }

    /**
     * Setup the recyclerview with the proper sections
     */
    private void setRecyclerViewSections() {
        if (tickets.isEmpty()) {
            ticketView.setAdapter(new TicketAdapter(tickets));
            noTix.setVisibility(View.VISIBLE);
            return;
        }

        if (noTix != null) noTix.setVisibility(View.GONE);
        // sort tix first on expired or not, then by created date
        Collections.sort(tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket lhs, Ticket rhs) {
                if (lhs.getStatus().equals("Expired") && !rhs.getStatus().equals("Expired"))
                    return 1;
//                            else if (!lhs.getStatus().equals("Expired") && rhs.getStatus().equals("Expired"))
                else
                    return -1;
                // TODO: 1/27/16 Sort by time if neither
            }
        });

        ticketView.setAdapter(new TicketAdapter(tickets));
    }

    public void refreshTicket(String objectID, String status, boolean not) {
        ParseAPIService.INSTANCE.getRestAdapter()
                .updateTicketStatus(objectID, new GSONMock.UpdateTicketStatusRequest(status, not))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.UpdateObj>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.UpdateObj updateObj) {
//                        getActivity().onBackPressed();
                        Snackbar.make(ticketView, "Ticket Deleted", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRefresh() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("HelpDeskTickets");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                swipeRefreshLayout.setRefreshing(false);

                if (e == null) {
                    tickets = new ArrayList<Ticket>();
                    for (ParseObject object : objects) {
                        tickets.add(0, new Ticket(object.getString("subject"), object.getString("description"), object.getString("status"), object.getObjectId(), object.getString("subCategory"), object.getString("location")));
                    }
                }

                // setup the recyclerview with the member var tickets
                setRecyclerViewSections();

                // after refresh check if it was about an action and do work on it
                if (args != null) {
                    String action = args.getString(PushNotificationReceiver.ACTION);
                    String id = args.getString(PushNotificationReceiver.OBJECT_ID);

                    if (id == null || action == null) return;

                    if (action.equals(PushNotificationReceiver.EXTEND)) {
                        refreshTicket(id, "Open", false);
                        for (Ticket t: tickets) {
                            if (t.getId().equals(id)){
                                EventBus.getDefault().post(new MainActivity.StartViewTicketActivity(t));
                            }
                        }
                    } else if (action.equals(PushNotificationReceiver.CLOSE)) {
                        // close the ticket
                        refreshTicket(id, "Closed", true);

                    } else if (action.equals(PushNotificationReceiver.DISPLAY)){
                        // user clicked on the ticket so open that shit
                        for (Ticket t: tickets) {
                            if (t.getId().equals(id)){
                                EventBus.getDefault().post(new MainActivity.StartViewTicketActivity(t));
                            }
                        }
                    }

                    // clear out the args that were passed in
                    args = null;

                }
            }
        });
    }
}
