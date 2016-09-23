package com.spartahack.spartahack17.View;

import com.spartahack.spartahack17.Model.Announcement;

import java.util.ArrayList;

/**
 * Created by ryancasler on 9/23/16.
 * SpartaHack2016-Android
 */

public interface AnnouncementView extends BaseView{
    void showAnnouncements(ArrayList<Announcement> announcements);
}
