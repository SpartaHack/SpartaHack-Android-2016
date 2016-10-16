package com.spartahack.spartahack17.Presenter;

import com.spartahack.spartahack17.Model.Session;
import com.spartahack.spartahack17.View.ProfileView;

import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public class ProfilePresenterTest extends BaseUnitTest {
    @Mock private ProfileView view;
    private ProfilePresenter presenter;
    private Session session;

    @Override public void before() throws Exception {
        super.before();

        presenter = new ProfilePresenter();
        presenter.attachView(view);

        session = new Session();
    }

    @Test public void onError() throws Exception {
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }

    @Test public void onNextNull() throws Exception {
        presenter.onNext(null);
        verify(view).onError("Invalid Credentials");
    }

    @Test public void onNextNotNull() throws Exception {
        presenter.onNext(session);
        verify(view).loginSuccess(session);
    }

}