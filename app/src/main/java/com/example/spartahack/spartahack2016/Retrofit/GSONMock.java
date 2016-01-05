package com.example.spartahack.spartahack2016.Retrofit;

import com.example.spartahack.spartahack2016.Model.Company;
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
}
