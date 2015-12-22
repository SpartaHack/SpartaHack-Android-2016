package com.example.spartahack.spartahack2016.Model;

/**
 * Created by ryancasler on 12/22/15.
 */
public class PushNotification {
    public String message;
    public int type;
    public int id;
    public int pinned;
    public boolean isPinned(){return pinned != 0;}
}
