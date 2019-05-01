package com.hakemy.linkedin_webservices.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

    public static boolean hasNetworkAcces(Context context)
    {
        // cast return to connectivity  manager
        // all in package android.net
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        try {
            NetworkInfo  networkInfo =connectivityManager.getActiveNetworkInfo();
            return networkInfo!=null && networkInfo.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
