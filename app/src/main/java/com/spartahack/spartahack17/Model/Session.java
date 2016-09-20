package com.spartahack.spartahack17.Model;

/**
 * Created by ryancasler on 9/19/16.
 * SpartaHack2016-Android
 */
public class Session {
    private int id;
    private String email;
    private String created_at;
    private String updated_at;
    private String auth_token;
    // TODO: 9/19/16 handle the array option too
    private int role;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public int getRole() {
        return role;
    }
}
