package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.Model.Company;
import com.spartahack.spartahack17.Model.Prize;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.View.PrizeView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by memuyskens on 10/5/16.
 * SpartaHack-Android
 * Unit tests for {@link PrizePresenter}
 */
public class PrizePresenterTest extends BaseUnitTest {

    @Mock private PrizeView view;
    private PrizePresenter presenter;

    @Before public void before() throws Exception {
        super.before();

        // create the presenter and attach the view
        presenter = new PrizePresenter();
        presenter.attachView(view);
    }

    @Test public void testCompare() throws Exception {
        Company rhsCompany = mock(Company.class);
        Company lhsCompany = mock(Company.class);

        Prize rhsPrize = mock(Prize.class);
        Prize lhsPrize = mock(Prize.class);

        // set each prize to return their respective company sponsors
        when(rhsPrize.getSponsor()).thenReturn(rhsCompany);
        when(lhsPrize.getSponsor()).thenReturn(lhsCompany);

        // test prizes by sponsors on the same tier
        when(rhsCompany.getLevel()).thenReturn(2); // rhsPrize has a "commander" sponsor
        when(rhsPrize.getName()).thenReturn("rhsPrize"); // give prize a name

        when(lhsCompany.getLevel()).thenReturn(2); // lhsPrize has a "commander" sponsor
        when(lhsPrize.getName()).thenReturn("lhsPrize"); // give prize a name

        // comparison should sort alphabetically when sponsors are same tier
        assertEquals(lhsPrize.getName().compareTo(rhsPrize.getName()), presenter.compare(lhsPrize, rhsPrize));

        // test prizes by sponsors on different tiers
        when(rhsCompany.getLevel()).thenReturn(5); // rhsPrize has a "partner" sponsor
        when(lhsCompany.getLevel()).thenReturn(4); // lhsPrize has a "trainee" sponsor

        // comparison should sort alphabetically when sponsors are same tier
        assertEquals(lhsPrize.getSponsor().getLevel() - rhsPrize.getSponsor().getLevel(), presenter.compare(lhsPrize, rhsPrize));
    }

    @Test public void testOnNext() throws Exception {
        GSONMock.Prizes prizes = new GSONMock.Prizes();
        prizes.prizes = new ArrayList<>();
        prizes.prizes.add(new Prize());
        ArrayList<Prize> prizeArrayList = prizes.prizes;

        presenter.onNext(prizes);
        verify(view).showPrizes(prizeArrayList);
    }

    @Test public void testOnError() throws Exception {
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }
}
