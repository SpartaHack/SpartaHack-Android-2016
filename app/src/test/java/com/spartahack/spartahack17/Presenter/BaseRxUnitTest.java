package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.View.BaseView;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public class BaseRxUnitTest<V extends BaseView, P extends RxPresenter> extends BaseUnitTest {

    @Mock V view;
    P presenter;

    private Throwable throwable;

    @Override public void before() throws Exception {
        super.before();

        throwable = new Throwable("Test throwable");
    }

    @Test public void testOnError() throws Exception {
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }
}
