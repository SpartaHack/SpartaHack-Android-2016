package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Adapters.MentorTicketAdapter;
import com.example.spartahack.spartahack2016.Adapters.TicketAdapter;
import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
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

public class MentorFragment extends BaseFragment {

    @Bind(R.id.notMentor) TextView notMentorView;
    @Bind(R.id.Mentor) TextView mentorView;
    @Bind(R.id.auth) TextView authView;
    @Bind(R.id.recyclers) RecyclerView ticketView;
    @Bind(R.id.no_tixs) TextView noTix;

    private TicketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ParseUser user;
    ParseObject mentor;
    List<String> mentorCategories;
    private ArrayList<Ticket> tickets;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mentor, container, false);

        ButterKnife.bind(this, view);

        user = ParseUser.getCurrentUser();
        tickets = new ArrayList<>();

        if (user == null) {
            authView.setVisibility(View.VISIBLE);
            notMentorView.setVisibility(View.GONE);
            mentorView.setVisibility(View.GONE);
            ticketView.setVisibility(View.GONE);
        } else {

            authView.setVisibility(View.GONE);


            //RecyclerView setup
            ticketView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            ticketView.setLayoutManager(mLayoutManager);


            ParseQuery<ParseObject> query = new ParseQuery<>("Mentors");
            query.whereEqualTo("mentor", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> mentorList, ParseException e) {
                    if (e == null) {
                        if (mentorList.isEmpty()) {
                            notMentorView.setVisibility(View.VISIBLE);
                            mentorView.setVisibility(View.GONE);
                        } else {
                            mentorCategories = mentorList.get(0).getList("categories");

                            ParseQuery<ParseObject> query1 = new ParseQuery<>("HelpDeskTickets");
                            query1.whereNotEqualTo("user", user);
                            query1.whereContainedIn("subCategory", mentorCategories);
                            query1.whereEqualTo("status", "Open");
                            query1.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if (e == null) {
                                        for (ParseObject object : objects) {
                                            tickets.add(0, new Ticket(object.getString("subject"), object.getString("description"), object.getString("status"), object.getObjectId(), object.getString("subCategory"), object.getString("location")));
                                        }
                                    }

                                    // setup the recyclerview with the member var tickets
                                    setRecyclerViewSections();
                                }
                            });

                        }

                    } else {
                        Log.e("Mentor", "Error: " + e.getMessage());
                    }
                }
            });
        }

        return view;
    }

    /**
     * Setup the recyclerview with the proper sections
     */
    private void setRecyclerViewSections() {
        if (tickets.isEmpty()) {
            noTix.setVisibility(View.VISIBLE);
            return;
        }

        noTix.setVisibility(View.GONE);
        // sort tix first on expired or not, then by created date
        Collections.sort(tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket lhs, Ticket rhs) {
                if (mentorCategories.contains(lhs.getSubcategory()) && !mentorCategories.contains(rhs.getSubcategory()))
                    return 1;
//                            else if (!lhs.getStatus().equals("Expired") && rhs.getStatus().equals("Expired"))
                else
                    return -1;
                // TODO: 1/27/16 Sort by time if neither
            }
        });

        ticketView.setAdapter(new MentorTicketAdapter(tickets));
    }
}
