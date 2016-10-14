package com.spartahack.spartahack17;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ryan on 10/22/15
 * SpartaHack2016-Android
 */
public class Utility {
    /**
     * Returns the height of the statusbar at the top of the screen
     * @param c context to get resources
     * @return height of the statusbar
     */
    public static int getStatusBarHeight(Context c) {
        int result = 0;
        int resourceId = c.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = c.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Hide the soft keyboard
     * @param view to get window token
     */
    protected void hideKeyboard(View view){
        // hide keyboard!!! fuck android
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
