package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.View.AnnouncementView;

import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 * Unit tests for {@link AnnouncementPresenter}
 */
public class AnnouncementPresenterTest extends BaseUnitTest {

    @Mock private AnnouncementView view;
    private AnnouncementPresenter presenter;

    @Override public void before() throws Exception {
        super.before();

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
        rhsPin.setPinned(1);
        Announcement lhsPin = new Announcement();
        lhsPin.setPinned(1);
        Announcement rhsNoPin = new Announcement();
        rhsNoPin.setPinned(0);
        Announcement lhsNoPin = new Announcement();
        lhsNoPin.setPinned(0);

        assertEquals(1, presenter.compare(lhsNoPin, rhsPin));
        assertEquals(-1, presenter.compare(lhsPin, rhsNoPin));

        // test case where both are not pinned and same time
        lhsNoPin.setCreatedAt("2016-02-28T22:15:00");
        rhsNoPin.setCreatedAt("2016-02-28T22:15:00");
        assertEquals(0, presenter.compare(lhsNoPin, rhsNoPin));

        // both pinned and left first
        lhsNoPin.setCreatedAt("2016-02-28T21:15:00");
        assertEquals(-1, presenter.compare(rhsNoPin, lhsNoPin));

        // both pinned right first
        rhsNoPin.setCreatedAt("2016-02-28T20:15:00");
        assertEquals(1, presenter.compare(rhsNoPin, lhsNoPin));
    }

    @Test public void testOnNext() throws Exception {
        // create the announcements
        ArrayList<Announcement> announcements = new ArrayList<>();
        announcements.add(new Announcement());

        presenter.onNext(announcements);
        verify(view).showAnnouncements(announcements);
    }

    @Test public void testOnError() throws Exception {
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }
}