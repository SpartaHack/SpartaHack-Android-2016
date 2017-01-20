package com.spartahack.spartahack17.Model;

import java.util.ArrayList;

/**
 * Created by ryancasler on 9/19/16.
 * SpartaHack2016-Android
 */
public class Session {
    private int id;
    private String email;
    private String first_name;
    private String last_name;
    private String created_at;
    private String updated_at;
    private ArrayList<String> roles;
//    private Object application;
//    private Object rsvp;
    private String auth_token;
    private int role;
    private int checked_in;

    public int getChecked_in() {
        return checked_in;
    }

    public Session(int id, String first, String last, String email, String auth, int role) {
        this.id = id;
        this.email = email;
        this.auth_token = auth;
        this.role = role;
        this.first_name = first;
        this.last_name = last;
    }

    public String getFullName() {
        return first_name + " " + last_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

//    public Object getApplication() {
//        return application;
//    }
//
//    public void setApplication(Object application) {
//        this.application = application;
//    }
//
//    public Object getRsvp() {
//        return rsvp;
//    }
//
//    public void setRsvp(Object rsvp) {
//        this.rsvp = rsvp;
//    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public void setRole(int role) {
        this.role = role;
    }

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
