package com.example.spartahack.spartahack2016.Fragment;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.TicketAdapter;
import com.example.spartahack.spartahack2016.Model.Ticket;
import com.example.spartahack.spartahack2016.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpFragment extends BaseFragment {

    @Bind(R.id.recycler) RecyclerView ticketView;

    private TicketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ParseUser user;
    List<ParseObject> categoryList;
    private ArrayList<Ticket> tickets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        registerEventBus = true;

        tickets = new ArrayList<>() ;

        user = ParseUser.getCurrentUser();
        if (user == null) {
            return inflater.inflate(R.layout.layout_notloggedin, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_help, container, false);

            ButterKnife.bind(this, view);

            //RecyclerView
            ticketView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(getActivity());
            ticketView.setLayoutManager(mLayoutManager);

//            final ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("HelpDeskTickets");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        String user_id = ParseUser.getCurrentUser().getObjectId();
                        for (ParseObject object : objects) {
                            String obj_id = ((ParseObject) object.get("user")).getObjectId().toString();
                            if (user_id.equals(obj_id)) {
                                // CHANGE WHEN DB IS FIXED
                                if (object.get("subject") != null) {
                                    try {
                                        tickets.add(new Ticket(object.get("subject").toString(), ((ParseObject) object.get("category")).fetchIfNeeded().get("category").toString(), object.get("description").toString()));
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                else {
                                    tickets.add(new Ticket("No Subject", ((ParseObject) object.get("category")).get("category").toString(), object.get("description").toString()));
                                }
                            }
                        }
                        mAdapter = new TicketAdapter(tickets);
                        ticketView.setAdapter(mAdapter);
                    } else {
                        // handle Parse Exception here
                    }
                }
            });

        }

        return view;
    }

    public void onEvent(Ticket ticket){
        mAdapter.add(mAdapter.getItemCount(), ticket);
    }

    @OnClick(R.id.fab)
    void showDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = new CreateTicketDialogFragment();
        newFragment.show(ft, "dialog");
    }


}
