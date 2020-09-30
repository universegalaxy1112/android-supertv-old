package com.livetv.normal.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Screen {
    public static Orientation orientation;
    public static int screenHeight = 0;
    public static int screenWidth = 0;

    public enum Orientation {
        NONE,
        LANDSCAPE,
        PORTRAIT
    }

    public static int GetScreenWidth() {
        return screenWidth;
    }

    public static int GetScreenHeight() {
        return screenHeight;
    }

    public static void SetScreenDimensions(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        if (screenHeight > screenWidth) {
            setOrientation(Orientation.PORTRAIT);
        } else {
            setOrientation(Orientation.LANDSCAPE);
        }
    }

    public static Orientation getOrientation() {
        return orientation;
    }

    public static void setOrientation(Orientation orientation2) {
        orientation = orientation2;
    }
}
