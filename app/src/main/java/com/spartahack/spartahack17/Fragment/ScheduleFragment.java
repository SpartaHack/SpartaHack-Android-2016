package com.spartahack.spartahack17.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spartahack.spartahack17.Adapters.EventListAdapter;
import com.spartahack.spartahack17.Adapters.SimpleSectionedRecyclerViewAdapter;
import com.spartahack.spartahack17.Model.Event;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.Retrofit.ParseAPIService;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends BaseFragment {

    /** Recycler view that displays all objects */
    @BindView(android.R.id.list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ParseAPIService.INSTANCE.getRestAdapter().getSchedule()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.Schedules>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("ScheduleFragment", e.toString());
                    }

                    @Override
                    public void onNext(GSONMock.Schedules schedules) {
                        ArrayList<Event> events = schedules.events;
                        // TODO: 1/5/16 fix this being empty
                        if (events == null || events.isEmpty())
                            return;
                        Collections.sort(events, (lhs, rhs) -> DateTimeComparator.getInstance().compare(lhs.getTime(), rhs.getTime()));


                        EventListAdapter eventListAdapter = new EventListAdapter(getActivity(), events);

                        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

                        DateTime date = events.get(0).getTime();
                        SimpleDateFormat formatDate = new SimpleDateFormat("EEEE", Locale.US);
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, formatDate.format(events.get(0).getTime().toDate())));
                        // location the header should go at
                        int sectionLoc = 0;
                        for (Event e : events){
                            if (DateTimeComparator.getDateOnlyInstance().compare(e.getTime(), date) == 1){
                                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(sectionLoc, formatDate.format(e.getTime().toDate())));
                                date = e.getTime();
                            }
                            sectionLoc++;
                        }

                        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

                        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.date_section, R.id.section_text, eventListAdapter);

                        adapter.setSections(sections.toArray(dummy));

                        recyclerView.setAdapter(adapter);

                    }
                });

        return v;
    }

}
