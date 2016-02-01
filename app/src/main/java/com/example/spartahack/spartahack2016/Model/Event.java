package com.example.spartahack.spartahack2016.Model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by ryancasler on 1/5/16.
 */
public class Event {
    @SerializedName("eventTitle")
    private String title;
    @SerializedName("eventDescription")
    private String description;
    @SerializedName("eventTime")
    private Time eventTime;

    private String eventLocation;

    public String getEventLocation() {
        return eventLocation;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateString() {
        return eventTime.date;
    }

    public DateTime getTime(){
        return new DateTime(eventTime.date);
    }

    private class Time{
        @SerializedName("iso")
        public String date;
    }
}
