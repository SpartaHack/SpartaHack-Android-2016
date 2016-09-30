package com.spartahack.spartahack17.Presenter;


import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.View.AnnouncementView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;


/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 * Unit tests for {@link AnnouncementPresenter}
 */
public class AnnouncementPresenterTest {

    @Mock private AnnouncementView view;

    private AnnouncementPresenter presenter;

    @Before public void before(){
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        presenter = new AnnouncementPresenter();
        presenter.attachView(view);
    }

    @Test public void testUpdateAnnouncements() throws Exception {
        // TODO: 9/29/16 work on rx stuff so its testable 
//        verify(view).showAnnouncements(any());
    }

    @Test public void testCompare() {
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
    }
}