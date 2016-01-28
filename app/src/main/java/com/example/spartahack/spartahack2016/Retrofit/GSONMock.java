package com.example.spartahack.spartahack2016.Retrofit;

import com.example.spartahack.spartahack2016.Model.Announcement;
import com.example.spartahack.spartahack2016.Model.Company;
import com.example.spartahack.spartahack2016.Model.Event;
import com.example.spartahack.spartahack2016.Model.Prize;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ryancasler on 1/4/16.
 */
public class GSONMock {
    public static class Companies {
        @SerializedName("results")
        public ArrayList<Company> companies;
    }

    public static class Schedules {
        @SerializedName("results")
        public ArrayList<Event> events;
    }

    public static class Prizes {
        @SerializedName("results")
        public ArrayList<Prize> prizes;
    }

    public static class Announcements {
        @SerializedName("results")
        public ArrayList<Announcement> announcements;
    }

    public static class UpdateObj {
        public String updatedAt;
    }

    public static class DeleteObjRequest {
        String status = "Deleted";
    }

}
