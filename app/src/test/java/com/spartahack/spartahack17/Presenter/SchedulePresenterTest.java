package com.spartahack.spartahack17.Presenter;

import android.util.Log;

import com.spartahack.spartahack17.Model.Event;
import com.spartahack.spartahack17.Retrofit.GSONMock;
import com.spartahack.spartahack17.View.ScheduleView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by memuyskens on 10/16/16.
 * SpartaHack-Android
 * Unit tests for {@link SchedulePresenter}
 */
public class SchedulePresenterTest extends BaseUnitTest {

    @Mock private ScheduleView view;

    private SchedulePresenter presenter;

    @Before public void before() throws Exception {
        super.before();

        // create the presenter and attach the view
        presenter = new SchedulePresenter();
        presenter.attachView(view);
    }

    @Test public void testCompare() throws Exception {
        Event rhsEvent = mock(Event.class);
        Event lhsEvent = mock(Event.class);

        DateTimeComparator dateTimeComparator = DateTimeComparator.getInstance();

        DateTime rhsDateTime = new DateTime(2017, 1, 20, 0, 0, 0, 0);
        DateTime lhsDateTime = new DateTime(2017, 1, 21, 0, 0, 0, 0);

        // set each event to return their respective DateTime
        when(rhsEvent.getTime()).thenReturn(rhsDateTime);
        when(lhsEvent.getTime()).thenReturn(lhsDateTime);

        // comparison test on DateTimes where lhs > rhs
        assertEquals(dateTimeComparator.compare(lhsEvent.getTime(), rhsEvent.getTime()), presenter.compare(lhsEvent, rhsEvent));

        rhsDateTime = new DateTime(2017, 1, 21, 0, 0, 0, 0);
        lhsDateTime = new DateTime(2017, 1, 20, 0, 0, 0, 0);

        // set each event to return their respective DateTime
        when(rhsEvent.getTime()).thenReturn(rhsDateTime);
        when(lhsEvent.getTime()).thenReturn(lhsDateTime);

        // comparison test on DateTimes where lhs < rhs
        assertEquals(dateTimeComparator.compare(lhsEvent.getTime(), rhsEvent.getTime()), presenter.compare(lhsEvent, rhsEvent));
    }

    @Test public void testOnNext() throws Exception {
        GSONMock.Events events = new GSONMock.Events();
        events.events = new ArrayList<>();
        events.events.add(new Event());
        ArrayList<Event> eventArrayList = events.events;

        presenter.onNext(events);
        verify(view).showEvents(eventArrayList);
    }

    @Test public void testOnError() throws Exception {
        presenter.onError(throwable);
        verify(view).onError(throwable.toString());
    }
}