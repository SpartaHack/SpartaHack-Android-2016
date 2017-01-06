package com.spartahack.spartahack17.Retrofit;

import com.google.gson.annotations.SerializedName;
import com.spartahack.spartahack17.Model.Announcement;
import com.spartahack.spartahack17.Model.Event;
import com.spartahack.spartahack17.Model.Prize;
import com.spartahack.spartahack17.Model.Session;

import java.util.ArrayList;

/**
 * Created by ryancasler on 1/4/16
 * SpartaHack2016-Android
 */
public class GSONMock {
    public static class Events {
        @SerializedName("schedule")
        public ArrayList<Event> events;
    }

    public static class Prizes {
        @SerializedName("prizes")
        public ArrayList<Prize> prizes;
    }

    public static class Announcements {
        @SerializedName("announcements")
        public ArrayList<Announcement> announcements;
    }

    public static class Login {
        public String password;
        public String email;
    }

    public static class AddInstillationRequest {
        public String device_type;
        public String token;

        public AddInstillationRequest(String token) {
            this.device_type = "android";
            this.token = token;
        }
    }

    public static class AddInstillationResponse {
        public int id;
        public String device_type;
        public String token;
        public Session user;
    }
}
