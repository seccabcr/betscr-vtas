package com.betscr.modulovendedor.metodos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class Internet {

    public static boolean isOnLine(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();

    }

}
