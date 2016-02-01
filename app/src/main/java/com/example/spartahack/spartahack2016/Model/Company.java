package com.example.spartahack.spartahack2016.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ryancasler on 1/4/16.
 */
public class Company {
    private String level;
    private String name;
    private String url;
    @SerializedName("png")
    private pic pic;


    // used for sorting
    public int getLevel() {
        if (level.toLowerCase().equals("partner")) return 5;
        if (level.toLowerCase().equals("trainee")) return 4;
        if (level.toLowerCase().equals("warrior")) return 3;
        if (level.toLowerCase().equals("commander")) return 2;
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
