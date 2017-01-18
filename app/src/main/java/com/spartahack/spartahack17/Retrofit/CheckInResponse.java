package com.spartahack.spartahack17.Retrofit;

import com.spartahack.spartahack17.Model.Session;

import java.util.ArrayList;

/**
 * Created by ryancasler on 1/16/17
 * Spartahack-Android
 */
public class CheckInResponse extends Session{
    private ArrayList<String> errors;

    public CheckInResponse(int id, String first, String last, String email, String auth, int role) {
        super(id, first, last, email, auth, role);
    }
}
