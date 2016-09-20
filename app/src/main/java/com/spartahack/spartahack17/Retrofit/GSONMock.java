package com.spartahack.spartahack17.Retrofit;

import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Model.Company;
import com.spartahack.spartahack17.Model.Event;
import com.spartahack.spartahack17.Model.Prize;
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

    public static class UpdateTicketStatusRequest {
        public UpdateTicketStatusRequest(String status, boolean notifiedFlag) {
            this.status = status;
            this.notifiedFlag = notifiedFlag;
        }

        public String status;
        public boolean notifiedFlag;
    }

    /**
     * Class for gson to parse out the json push object
     */
    public static class PushInfo {
        public String alert;
        public String description;
        public String category;
        public ArrayList<String> action;
        public String ticketId;
    }

    public static class Ticket {
        public String description;
        public String location;
        public String subject;
        public String subCategory;
        public String status;
        public String objectId;
    }

}
