package com.spartahack.spartahack17.View;

import com.spartahack.spartahack17.Model.Session;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
public interface ProfileView extends BaseView {
    void logOutSuccess();
    void logOutError();
    void loginSuccess(Session data);
}
