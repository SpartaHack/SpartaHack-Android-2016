package com.spartahack.spartahack17.Model;

import java.util.Locale;

/**
 * Created by ryancasler on 1/4/16
 * SpartaHack2016-Android
 */
public class Company {
    private int id;
    private final String level;
    private final String name;
    private String url;
    private String logo_png_light;
    private String logo_svg_light;


    public Company(String level, String name) {
        this.level = level;
        this.name = name;
    }

    // used for sorting
    public int getLevel() {
        if (level.toLowerCase(Locale.US).equals("partner")) return 5;
        if (level.toLowerCase(Locale.US).equals("trainee")) return 4;
        if (level.toLowerCase(Locale.US).equals("warrior")) return 3;
        if (level.toLowerCase(Locale.US).equals("commander")) return 2;
        return 1;
    }

    public String getLevelName() { return level; }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getLogo_svg_light() {
        return logo_svg_light;
    }

    public String getLogo_png_light() {
        return logo_png_light;
    }
}
