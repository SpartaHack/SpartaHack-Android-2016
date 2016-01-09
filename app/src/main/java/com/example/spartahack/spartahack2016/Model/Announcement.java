package com.example.spartahack.spartahack2016.Model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by ryancasler on 1/9/16.
 */
public class Announcement {
    /** Full message of the push */
    @SerializedName("Description")
    private String message;

    /** Title of the push */
    @SerializedName("Title")
    private String title;

    /** if the push should be pinned or not */
    @SerializedName("Pinned")
    private boolean pinned;

    @SerializedName("createdAt")
    private String time;

    public DateTime getTime(){
        return new DateTime(time);
    }


    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }

    public boolean getPinned() {
        return pinned;
    }
}


