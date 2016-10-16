package com.spartahack.spartahack17.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.spartahack.spartahack17.Adapters.CompanyListAdapter;
import com.spartahack.spartahack17.Adapters.SimpleSectionedRecyclerViewAdapter;
import com.spartahack.spartahack17.Model.Company;
import com.spartahack.spartahack17.Presenter.CompanyPresenter;
import com.spartahack.spartahack17.R;
import com.spartahack.spartahack17.View.CompanyView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Fragments that displays the companies that are sponsors of our great event
 */
public class CompanyFragment extends MVPFragment<CompanyView, CompanyPresenter> implements CompanyView {

    private static final String TAG = "CompanyFragment";

    /** Recycler view that displays all objects */
    @BindView(android.R.id.list) RecyclerView recyclerView;

    @Override int getLayout() {
        return R.layout.fragment_company;
    }

    @Override boolean registerEventbus() {
        return false;
    }

    @NonNull @Override public CompanyPresenter createPresenter() {
        return new CompanyPresenter();
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override public void onResume() {
        super.onResume();
        getMVPPresenter().loadCompanies();
    }

    @Override public void showLoading() {
    }

    @Override public void onError(String error) {
    }

    @Override public void showCompanies(ArrayList<Company> companies) {
        // setup the sectioned list adapter
        CompanyListAdapter simpleCompanyAdapter = new CompanyListAdapter(getActivity(), companies);

        ArrayList<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

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
}
