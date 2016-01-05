package com.example.spartahack.spartahack2016.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ryancasler on 1/4/16.
 */
public class Company {
    private String level;
    private String name;
    private String url;
    @SerializedName("img")
    private pic pic;


    // used for sorting
    public int getLevel() {
        if (level.equals("partner")) return 5;
        if (level.equals("trainee")) return 4;
        if (level.equals("warrior")) return 3;
        if (level.equals("commander")) return 2;
        return 1;
    }

    public String getLevelName() { return level; }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPicUrl() {
        return pic.url;
    }

    private class pic{
        public String url;
    }
}
