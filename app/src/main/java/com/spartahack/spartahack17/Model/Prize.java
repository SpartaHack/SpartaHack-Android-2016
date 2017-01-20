package com.spartahack.spartahack17.Model;

/**
 * Created by ryancasler on 1/5/16
 * SpartaHack2016-Android
 */
public class Prize {
    private int id;
    private String description;
    private String name;
    private Company sponsor;

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Company getSponsor() {
        if (sponsor == null){
            // this is a spartahack prize, put a bandaid on for now
            sponsor = new Company("", "SpartaHack");
        }
        return sponsor;
    }
}
