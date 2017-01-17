package com.spartahack.spartahack17.Model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by ryancasler on 1/9/16
 * SpartaHack2016-Android
 */
public class Announcement {

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("pinned")
    private Integer pinned;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPinned() {
        return (pinned == 1);
    }

    public void setPinned(Integer pinned) {
        this.pinned = pinned;
    }

    public DateTime getCreatedAt() {
        return new DateTime(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public DateTime getUpdatedAt() {
        return new DateTime(updatedAt);
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}


