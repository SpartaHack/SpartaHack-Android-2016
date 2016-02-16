package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.CompanyListAdapter;
import com.example.spartahack.spartahack2016.Adapters.SimpleSectionedRecyclerViewAdapter;
import com.example.spartahack.spartahack2016.Model.Company;
import com.example.spartahack.spartahack16.R;
import com.example.spartahack.spartahack2016.Retrofit.GSONMock;
import com.example.spartahack.spartahack2016.Retrofit.ParseAPIService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Fragments that displays the companies that are sponsors of our great event
 */
public class CompanyFragment extends BaseFragment {

    /** Recycler view that displays all objects */
    @Bind(android.R.id.list) RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ParseAPIService.INSTANCE.getRestAdapter().getCompany()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GSONMock.Companies>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("CompanyFragment", e.toString());
                    }

                    @Override
                    public void onNext(GSONMock.Companies company) {
                        
                        // get results as array list 
                        ArrayList<Company> companies = company.companies;
                        
                        // TODO: 1/5/16 handle if list of companies returns empty/ null
                        if (companies == null || companies.isEmpty()) 
                            return;
                        Collections.sort(companies, new Comparator<Company>() {
                                    @Override
                                    public int compare(Company lhs, Company rhs) {
                                        if (lhs.getLevel() - rhs.getLevel() != 0)
                                            return lhs.getLevel() - rhs.getLevel();
                                        return lhs.getName().compareTo(rhs.getName());
                                    }
                                });

                        CompanyListAdapter simpleCompanyAdapter = new CompanyListAdapter(getActivity(), companies);

                        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

                        // add section header for first tier that has sponsors
//                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, companies.get(0).getLevelName()));

                        // the level from the last header used to track what level needs to add a header next
                        int companyLevelTemp = -1;

                        // location the header should go at
                        int sectionLoc = 0;
                        for (Company c : companies){
                            if (c.getLevel() != companyLevelTemp){
                                sections.add(new SimpleSectionedRecyclerViewAdapter.Section(sectionLoc, c.getLevelName()));
                                companyLevelTemp = c.getLevel();
                            }
                            sectionLoc++;
                        }

                        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

                        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section, R.id.section_text, simpleCompanyAdapter);

                        adapter.setSections(sections.toArray(dummy));

                        recyclerView.setAdapter(adapter);
                    }
                });

        return view;
    }

}
