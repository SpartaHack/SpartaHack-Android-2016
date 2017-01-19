package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.Model.Company;
import com.spartahack.spartahack17.View.CompanyView;

import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public class CompanyPresenterTest extends BaseUnitTest {

    @Mock private CompanyView view;
    private CompanyPresenter presenter;
    private ArrayList<Company> companyArrayList;

    @Override public void before() throws Exception {
        super.before();

        // mock all the static methods
        PowerMockito.mockStatic(Collections.class);

        // create the presenter and attach the view
        presenter = new CompanyPresenter();
        presenter.attachView(view);

        companyArrayList = new ArrayList<>();
        companyArrayList.add(new Company("1", "a"));
        companyArrayList.add(new Company("1", "b"));
    }

//    @Test public void loadCompanies() throws Exception {
//        presenter.loadCompanies();
//        verify(view).showLoading();
//    }

    @Test public void onNext() throws Exception {
        presenter.onNext(companyArrayList);
        verify(view).showCompanies(companyArrayList);
    }

    @Test public void testOnError() throws Exception {
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }

    @Test public void testCompare() throws Exception {
        Company one = new Company("champion", "a");
        Company two = new Company("commander", "b");
        Company three = new Company("warrior", "c");
        Company four = new Company("warrior", "b");

        assertEquals(-1, presenter.compare(one, two));
        assertEquals(1, presenter.compare(three, two));

        assertEquals(1, presenter.compare(three, four));
        assertEquals(-1, presenter.compare(four, three));

        assertEquals(0, presenter.compare(four, four));
        assertEquals(0, presenter.compare(four, four));
    }
}