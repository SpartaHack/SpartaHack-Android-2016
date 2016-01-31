package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.spartahack.spartahack2016.Activity.MainActivity;
import com.example.spartahack.spartahack2016.Adapters.SimpleSectionedRecyclerViewAdapter;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class HelpFragment extends BaseFragment {

    @Bind(R.id.recycler) RecyclerView ticketView;
    @Bind(R.id.no_user) LinearLayout noUser;
    @Bind(R.id.user) RelativeLayout userExists;
    @Bind(R.id.no_tix) TextView noTix;

    private TicketAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private final String I_EXTRA_FROM = "from help";

    ParseUser user;
    List<ParseObject> categoryList;
    private ArrayList<Ticket> tickets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        registerEventBus = true;

        tickets = new ArrayList<>();

        user = ParseUser.getCurrentUser();

        view = inflater.inflate(R.layout.fragment_help, container, false);

        ButterKnife.bind(this, view);


        if (user == null) {
            noUser.setVisibility(View.VISIBLE);
            userExists.setVisibility(View.GONE);
            noTix.setVisibility(View.GONE);
        } else {
            noUser.setVisibility(View.GONE);
            userExists.setVisibility(View.VISIBLE);
            noTix.setVisibility(View.GONE);


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

                            // pick the status
                            String status = "None";
                            if (object.containsKey("status"))
                                status = object.get("status").toString();

                            if (user_id.equals(obj_id) && !status.equals("Deleted")) {
                                // pick the title
                                String title = "No Subject";
                                if (object.containsKey("subject"))
                                    title = object.get("subject").toString();

                                try {
                                    tickets.add(new Ticket(title,
                                            ((ParseObject) object.get("category")).fetchIfNeeded().get("category").toString(),
                                            object.get("description").toString(), status, object.getObjectId()));
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

    public void onEvent(Ticket ticket) {
        // add ticket and update recyclerview
        tickets.add(0, ticket);
        setRecyclerViewSections();
    }

    public void onEvent(HelpDeskFragment.ModTix t){
        if (t.action.equals(PushNotificationReceiver.EXTEND)){
            refreshTicket(t.oid, "Open", false);
        } else if (t.action.equals(PushNotificationReceiver.CLOSE)){
            refreshTicket(t.oid, "Closed", true);
        } else {

        }

    }

    @OnClick(R.id.fab)
    void showDialog() {

        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//        if (prev != null) {
//            ft.remove(prev);
//        }
//        ft.addToBackStack(null);
//
//        // Create and show the dialog.
//        DialogFragment newFragment = new CreateTicketDialogFragment();
//        newFragment.show(ft, "dialog");

        MainActivity activity = ((MainActivity) getActivity());
        CreateTicketDialogFragment fragment = new CreateTicketDialogFragment();
        activity.switchContent(R.id.container, fragment);
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
    private void setRecyclerViewSections(){
        if (tickets.isEmpty()){
            noTix.setVisibility(View.VISIBLE);
            return;
        }
        
        if (noTix!=null) noTix.setVisibility(View.GONE);
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

        mAdapter = new TicketAdapter(tickets);

        // add first section for either expired or current
        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, tickets.get(0).getStatus().equals("Expired") ?  "Expired Tickets" : "Current Tickets"));

        // find where the tix turn from current to expired
        int loc2 = 0;
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getStatus().equals("Expired")){
                loc2 = i;
                break;
            }
        }

        // add another section if needed
        if (loc2 > 0) sections.add(new SimpleSectionedRecyclerViewAdapter.Section(loc2, "Expired Tickets"));

        // setup adapter with the sections
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section_ticketz, R.id.section_text, mAdapter);
        adapter.setSections(sections.toArray(dummy));

        ticketView.setAdapter(adapter);
    }

    public void refreshTicket(String objectID, String status, boolean not){
        ParseAPIService.INSTANCE.getRestAdapter()
                .updateTicketStatus(objectID, new GSONMock.UpdateTicketStatusRequest(status, not))
                .subscribeOn(AndroidSchedulers.mainThread())
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
}
