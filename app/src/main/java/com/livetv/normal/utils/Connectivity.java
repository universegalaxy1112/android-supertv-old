package com.livetv.normal.utils;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.livetv.normal.LiveTvApplication;

public class Connectivity {
    public static boolean isConnected() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) LiveTvApplication.getAppContext().getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
