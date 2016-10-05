package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Model.Prize;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.View.PrizeView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.verify;

/**
 * Created by memuyskens on 10/5/16.
 * SpartaHack-Android
 * Unit tests for {@link PrizePresenter}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Collections.class})
public class PrizePresenterTest {

    @Mock private PrizeView view;

    private PrizePresenter presenter;

    @Before public void before() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // mock all the static methods
        PowerMockito.mockStatic(Collections.class);
        PowerMockito.mockStatic(Log.class);

        // create the presenter and attach the view
        presenter = new PrizePresenter();
        presenter.attachView(view);
    }

    @Test public void testOnNext() throws Exception {
        // create the announcements
        GSONMock.Prizes prizes = new GSONMock.Prizes();
        prizes.prizes = new ArrayList<>();
        prizes.prizes.add(new Prize());
        ArrayList<Prize> prizeArrayList = prizes.prizes;

        presenter.onNext(prizes);
        verify(view).showPrizes(prizeArrayList);
    }

    @Test public void testOnError() throws Exception {
        // call on error with the error stirng from the throwable
        String errorMessage = "ERROR NULLPTR";
        Throwable throwable = new Throwable(errorMessage);
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }
}
