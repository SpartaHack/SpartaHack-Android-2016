package com.example.spartahack.spartahack16;

import android.content.Context;

/**
 * Created by ryan on 10/22/15.
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
}
