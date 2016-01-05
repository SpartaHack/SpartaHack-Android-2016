package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Model.Event;
import com.example.spartahack.spartahack2016.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends BaseFragment {

    /** Recycler view that displays all objects */
    @Bind(android.R.id.list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_schedule, container, false);

        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ParseAPIService.INSTANCE.getRestAdapter().getSchedule()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.Schedules>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GSONMock.Schedules schedules) {
                        ArrayList<Event> events = schedules.events;

                        Collections.sort(events, new Comparator<Event>() {
                            @Override
                            public int compare(Event lhs, Event rhs) {
                                return DateTimeComparator.getInstance().compare(lhs.getTime(), rhs.getTime());
                            }
                         });
                    }
                });
//                .subscribe(new Subscriber<GSONMock.Companies>() {
//                    @Override
//                    public void onCompleted() {
//
//
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e("CompanyFragment", e.toString());
//                    }
//
//                    @Override
//                    public void onNext(GSONMock.Companies company) {
//
//                        // get results as array list
//                        ArrayList<Company> companies = company.companies;
//
//                        // TODO: 1/5/16 handle if list of companies returns empty/ null
//                        if (companies == null || companies.isEmpty())
//                            return;
//                        Collections.sort(company.companies, new Comparator<Company>() {
//                            @Override
//                            public int compare(Company lhs, Company rhs) {
//                                if (lhs.getLevel() - rhs.getLevel() != 0)
//                                    return lhs.getLevel() - rhs.getLevel();
//                                return lhs.getName().compareTo(rhs.getName());
//                            }
//                        });
//
//                        SimpleCompanyAdapter simpleCompanyAdapter = new SimpleCompanyAdapter(getActivity(), companies);
//
//                        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
//
//                        // add section header for first tier that has sponsors
////                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, companies.get(0).getLevelName()));
//
//                        // the level from the last header used to track what level needs to add a header next
//                        int companyLevelTemp = -1;
//
//                        // location the header should go at
//                        int sectionLoc = 0;
//                        for (Company c : companies){
//                            if (c.getLevel() != companyLevelTemp){
//                                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(sectionLoc, c.getLevelName()));
//                                companyLevelTemp = c.getLevel();
//                            }
//                            sectionLoc++;
//                        }
//
//                        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
//
//                        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section, R.id.section_text, simpleCompanyAdapter);
//
//                        adapter.setSections(sections.toArray(dummy));
//
//                        recyclerView.setAdapter(adapter);
//                    }
//                });

        return v;
    }

}
