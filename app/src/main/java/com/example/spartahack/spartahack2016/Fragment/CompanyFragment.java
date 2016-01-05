package com.example.spartahack.spartahack2016.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spartahack.spartahack2016.Adapters.SimpleCompanyAdapter;
import com.example.spartahack.spartahack2016.Adapters.SimpleSectionedRecyclerViewAdapter;
import com.example.spartahack.spartahack2016.Model.Company;
import com.example.spartahack.spartahack2016.R;
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
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends BaseFragment {

    @Bind(android.R.id.list) RecyclerView recyclerView;
    SimpleCompanyAdapter simpleCompanyAdapter;

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

                    }

                    @Override
                    public void onNext(GSONMock.Companies company) {

                        Collections.sort(company.companies, new Comparator<Company>() {
                                    @Override
                                    public int compare(Company lhs, Company rhs) {
                                        if (lhs.getLevel() - rhs.getLevel() != 0)
                                            return lhs.getLevel() - rhs.getLevel();
                                        return lhs.getName().compareTo(rhs.getName());
                                    }
                                });

                        simpleCompanyAdapter = new SimpleCompanyAdapter(getActivity(), company.companies);

                        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Warrior"));
                        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(4, "Trainee"));

                        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];

                        SimpleSectionedRecyclerViewAdapter adapter = new SimpleSectionedRecyclerViewAdapter(getActivity(), R.layout.section, R.id.section_text, simpleCompanyAdapter);

                        adapter.setSections(sections.toArray(dummy));

                        recyclerView.setAdapter(adapter);

//                                listView.setAdapter(new CompanyListAdapter((MainActivity) getActivity(), company.companies));
                    }
                });

        return view;
    }

}
