package com.spartahack.spartahack17.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.spartahack.spartahack17.Activity.CreateTicketActivity;
import com.spartahack.spartahack17.Activity.MainActivity;
import com.spartahack.spartahack17.Adapters.TicketAdapter;
import com.spartahack.spartahack17.Model.Ticket;
import com.spartahack.spartahack17.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyTicketsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recycler) RecyclerView ticketView;
    @Bind(R.id.user) RelativeLayout userExists;
    @Bind(R.id.no_tix) TextView noTix;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Ticket> tickets;

    ParseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        registerEventBus = true;

        tickets = new ArrayList<>();

        ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.background);

        user = ParseUser.getCurrentUser();

        userExists.setVisibility(View.VISIBLE);
        noTix.setVisibility(View.GONE);

        //RecyclerView
        ticketView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ticketView.setLayoutManager(mLayoutManager);

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
        Collections.sort(tickets, (lhs, rhs) -> {
            if (lhs.getStatus().equals("Expired") && !rhs.getStatus().equals("Expired")) return 1;
            return -1;
        });

        if (ticketView!= null)ticketView.setAdapter(new TicketAdapter(tickets));
    }

    @Override
    public void onRefresh() {
        ParseQuery<ParseObject> query = new ParseQuery<>("HelpDeskTickets");
        query.whereEqualTo("user", user);
        query.whereNotEqualTo("status", "Deleted");
        query.include("user");
        query.findInBackground((objects, e) -> {
            if (swipeRefreshLayout != null)swipeRefreshLayout.setRefreshing(false);

            Collections.sort(objects, new Comparator<ParseObject>() {
                @Override
                public int compare(ParseObject lhs, ParseObject rhs) {
                    int lhsI = getStatusInt(lhs.getString("status"));
                    int rhsI = getStatusInt(rhs.getString("status"));
                    if (rhsI == lhsI) return rhs.getUpdatedAt().compareTo(lhs.getUpdatedAt());
                    else return lhsI-rhsI;
                }
            });
            if (e == null) {
                tickets = new ArrayList<>();
                for (ParseObject object : objects) {
                    tickets.add(0, new Ticket(object.getString("subject"), object.getString("description"), object.getString("status"), object.getObjectId(), object.getString("subCategory"), object.getString("location")));
                }
            }

            // setup the recyclerview with the member var tickets
            setRecyclerViewSections();
        });
    }

    private int getStatusInt(String s){
        if (s.equals("Open")) return 0;
        if (s.equals("Expired")) return 1;
        if (s.equals("Accepted")) return 2;
        return 3;
    }
}
