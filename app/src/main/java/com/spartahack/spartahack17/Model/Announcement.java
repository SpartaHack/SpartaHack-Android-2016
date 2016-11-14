package com.spartahack.spartahack17.Model;

import org.joda.time.DateTime;

/**
 * Created by ryancasler on 1/9/16
 * SpartaHack2016-Android
 */
public class Announcement {
    private String description;
    private String title;
    private int pinned;
    private String createdAt;
    private String updatedAt;

    public void setPinned(int pinned) {
        this.pinned = pinned;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DateTime getTime(){
        return new DateTime(createdAt);
    }


    public String getMessage() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public boolean getPinned() {
        return pinned == 1;
    }

    public boolean isPinned() {
        return pinned == 1;
    }

    public void setMessage(String message) {
        this.description = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned ? 1 : 0;
    }

    public void setTime(String time) {
        this.createdAt = time;
    }
}


