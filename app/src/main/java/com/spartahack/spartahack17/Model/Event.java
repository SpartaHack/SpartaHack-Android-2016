package com.spartahack.spartahack17.Model;

import org.joda.time.DateTime;

/**
 * Created by ryancasler on 1/5/16
 * SpartaHack2016-Android
 */
public class Event {
    private int id;
    private String title;
    private String description;
    private String time;
    private String updatedAt;
    private String location;

    public String getEventLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateString() {
        return time;
    }

    public DateTime getTime(){
        return new DateTime(time);
    }
}
