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

        if (user == null){
            authView.setVisibility(View.VISIBLE);
            notMentorView.setVisibility(View.GONE);
            mentorView.setVisibility(View.GONE);
            ticketView.setVisibility(View.GONE);
        }
        else{

            authView.setVisibility(View.GONE);

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Mentors");
            query.whereEqualTo("mentor", user);
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> mentorList, ParseException e) {
                    if (e == null) {
                        if(mentorList.isEmpty()){
                            notMentorView.setVisibility(View.VISIBLE);
                            mentorView.setVisibility(View.GONE);
                        }
                        else{
                            mentorCategories = mentorList.get(0).getList("categories");
                        }

                    } else {
                        Log.e("Mentor", "Error: " + e.getMessage());
                    }
                }
            });



            //RecyclerView
            ticketView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            ticketView.setLayoutManager(mLayoutManager);



            query = ParseQuery.getQuery("HelpDesk");
            query.whereEqualTo("category", "Mentorship");
            try {
                mentor = query.find().get(0);
                Log.d("Mentor", mentor.get("Description").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            query = ParseQuery.getQuery("HelpDeskTickets");
            query.whereNotEqualTo("user", user);
            query.whereEqualTo("category", mentor);
            query.whereExists("subCategory");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject object : objects) {

                            // pick the status
                            String status = "None";
                            if (object.containsKey("status"))
                                status = object.get("status").toString();

                            if (!status.equals("Deleted") && !status.equals("Expired")) {
                                // pick the title
                                String title = "No Subject";
                                if (object.containsKey("subject"))
                                    title = object.get("subject").toString();

                                try {
                                    String category = ((ParseObject) object.get("category")).fetchIfNeeded().get("category").toString();
                                    String subCategory = category.equals("Mentorship")? object.get("subCategory").toString() : category;
                                    String location = object.get("location").toString();
                                    tickets.add(new Ticket(title, category,
                                            object.get("description").toString(), status, object.getObjectId(), subCategory, location));
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }

                    // setup the recyclerview with the member var tickets
                    setRecyclerViewSections();
                }
            });

        }

        return view;
    }

    /**
     * Setup the recyclerview with the proper sections
     */
    private void setRecyclerViewSections(){
        if (tickets.isEmpty()){
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

//        // add first section for either expired or current
//        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
//
//        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, !mentorCategories.contains(tickets.get(0).getSubcategory()) ?  tickets.get(0).getSubcategory() : "Other Tickets"));
//
//        // find where the tix turn from current to expired
//        int loc2 = 0;
//        for (int i = 0; i < tickets.size(); i++) {
//            if (mentorCategories.contains(tickets.get(i).getSubcategory())){
//                loc2 = i;
//                break;
//            }
//        }
//
//        // add another section if needed
//        if (loc2 > 0) sections.add(new SimpleSectionedRecyclerViewAdapter.Section(loc2, "Other Tickets"));
//
//        // setup adapter with the sections
//        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
//        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section_ticketz, R.id.section_text, mAdapter);
//        adapter.setSections(sections.toArray(dummy));

        ticketView.setAdapter(new MentorTicketAdapter(tickets));
    }

}
