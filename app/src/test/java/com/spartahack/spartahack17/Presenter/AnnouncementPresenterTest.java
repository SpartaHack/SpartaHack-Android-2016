package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.View.AnnouncementView;

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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 * Unit tests for {@link AnnouncementPresenter}
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Collections.class})
public class AnnouncementPresenterTest {

    @Mock private AnnouncementView view;

    private AnnouncementPresenter presenter;

    @Before public void before() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // mock all the static methods
        PowerMockito.mockStatic(Collections.class);
        PowerMockito.mockStatic(Log.class);

        // create the presenter and attach the view
        presenter = new AnnouncementPresenter();
        presenter.attachView(view);
    }

    // TODO: 9/30/16 figure out how to test stuff with netowrk calls 
//    @Test public void testUpdateAnnouncements() throws Exception {
//        presenter.updateAnnouncements();
//        verify(view).showLoading();
//    }

    @Test public void testCompare() throws Exception {
        Announcement rhsPin = new Announcement();
        rhsPin.setPinned(true);
        Announcement lhsPin = new Announcement();
        lhsPin.setPinned(true);
        Announcement rhsNoPin = new Announcement();
        rhsNoPin.setPinned(false);
        Announcement lhsNoPin = new Announcement();
        lhsNoPin.setPinned(false);

        assertEquals(1, presenter.compare(lhsNoPin, rhsPin));
        assertEquals(-1, presenter.compare(lhsPin, rhsNoPin));

        // test case where both are not pinned and same time
        lhsNoPin.setTime("2016-02-28T22:15:00");
        rhsNoPin.setTime("2016-02-28T22:15:00");
        assertEquals(0, presenter.compare(lhsNoPin, rhsNoPin));

        // both pinned and left first
        lhsNoPin.setTime("2016-02-28T21:15:00");
        assertEquals(-1, presenter.compare(rhsNoPin, lhsNoPin));

        // both pinned right first
        rhsNoPin.setTime("2016-02-28T20:15:00");
        assertEquals(1, presenter.compare(rhsNoPin, lhsNoPin));
    }

    @Test public void testOnNext() throws Exception {
        // create the announcements
        GSONMock.Announcements announcements = new GSONMock.Announcements();
        announcements.announcements = new ArrayList<>();
        announcements.announcements.add(new Announcement());
        ArrayList<Announcement> announcementArrayList = announcements.announcements;

        presenter.onNext(announcements);
        verify(view).showAnnouncements(announcementArrayList);
    }

    @Test public void testOnError() throws Exception {
        // call on error with the error stirng from the throwable
        String errorMessage = "ERROR NULLPTR";
        Throwable throwable = new Throwable(errorMessage);
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }
}