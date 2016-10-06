package com.spartahack.spartahack17.Fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spartahack.spartahack17.Adapters.MentorTicketAdapter;
import com.spartahack.spartahack17.Adapters.SimpleSectionedRecyclerViewAdapter;
import com.spartahack.spartahack17.Model.Ticket;
import com.spartahack.spartahack17.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MentorFragment extends BaseFragment  implements SwipeRefreshLayout.OnRefreshListener{

//    @Bind(R.id.notMentor) TextView notMentorView;
//    @Bind(R.id.Mentor) TextView mentorView;
    @Bind(R.id.auth) TextView authView;
    @Bind(R.id.recyclers) RecyclerView ticketView;
    @Bind(R.id.no_tixs) TextView noTix;
    @Bind(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;

    List<String> mentorCategories;
    private ArrayList<Ticket> tickets;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mentor, container, false);

        ButterKnife.bind(this, view);

        //RecyclerView setup
        ticketView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        ticketView.setLayoutManager(mLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.background);


//        if (ParseUser.getCurrentUser() == null) {
//            authView.setVisibility(View.VISIBLE);
//            ticketView.setVisibility(View.GONE);
//            noTix.setVisibility(View.GONE);
//        } else {
//            authView.setVisibility(View.GONE);
//            onRefresh();
//        }

        return view;
    }

    /**
     * Setup the recyclerview with the proper sections
     */
    private void setRecyclerViewSections() {
        Realm realm = Realm.getInstance(getActivity());
        RealmQuery<Ticket> query = realm.where(Ticket.class);
        RealmResults<Ticket> result1 = query.findAll();
        //noinspection Convert2streamapi
        for (Ticket tix: result1) {
            tickets.add(new Ticket(tix.getSubject(), tix.getDescription(),tix.getStatus(), tix.getId(), tix.getSubcategory(), tix.getLocation(), true, tix.getName()));
        }

        if (tickets.isEmpty()) {
            ticketView.setAdapter(new MentorTicketAdapter(tickets));
            noTix.setVisibility(View.VISIBLE);
            return;
        }

        noTix.setVisibility(View.GONE);

        // sort tix first on expired or not, then by created date
        Collections.sort(tickets, (lhs, rhs) -> {
            if (lhs.getMine() && rhs.getMine()) return 0;
            else if (rhs.getMine()) return 1;
            return -1;
        });


        MentorTicketAdapter mentorTicketAdapter = new MentorTicketAdapter(tickets);

        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

        int sectionLoc = 0;
        if (tickets.get(0).getMine()){
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(sectionLoc, "Tickets I Mentor"));
            for (Ticket t : tickets){
                if (!t.getMine()) break;
                sectionLoc++;
            }
        }

        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(sectionLoc, "Tickets needing a Mentor"));

        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.date_section, R.id.section_text, mentorTicketAdapter);

        adapter.setSections(sections.toArray(dummy));

        ticketView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
//        tickets = new ArrayList<>();
//        ParseQuery<ParseObject> query = new ParseQuery<>("Mentors");
//        query.whereEqualTo("mentor", ParseUser.getCurrentUser());
//        query.findInBackground((mentorList, e) -> {
//            if (e == null) {
//                    mentorCategories = mentorList.get(0).getList("categories");
//
//                    ParseQuery<ParseObject> query1 = new ParseQuery<>("HelpDeskTickets");
//                    query1.whereNotEqualTo("user", ParseUser.getCurrentUser());
//                    query1.whereContainedIn("subCategory", mentorCategories);
//                    query1.whereEqualTo("status", "Open");
//                    query1.include("user");
//                    query1.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> objects, ParseException e) {
//                            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
//                            if (e == null) {
//                                Collections.sort(objects, new Comparator<ParseObject>() {
//                                    @Override
//                                    public int compare(ParseObject lhs, ParseObject rhs) {
//                                        return rhs.getUpdatedAt().compareTo(lhs.getUpdatedAt());
//                                    }
//                                });
//                                for (ParseObject object : objects) {
//                                    String name = object.getParseObject("user").getString("name");
//                                    tickets.add(0, new Ticket(object.getString("subject"), object.getString("description"), object.getString("status"), object.getObjectId(), object.getString("subCategory"), object.getString("location"), name));
//                                }
//                                setRecyclerViewSections();
//                            }
//                        }
//                    });
//
//
//            } else {
//                Log.e("Mentor", "Error: " + e.getMessage());
//            }
//        });

    }
}
