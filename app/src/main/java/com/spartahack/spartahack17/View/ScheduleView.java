package com.spartahack.spartahack17.View;

import com.spartahack.spartahack17.Model.Event;

import java.util.ArrayList;

/**
 * Created by memuyskens on 10/16/16.
 * SpartaHack-Android
 */
public interface ScheduleView extends BaseView {
    void showEvents(ArrayList<Event> events);
}
