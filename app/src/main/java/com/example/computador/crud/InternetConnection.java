package com.example.computador.crud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetConnection extends BroadcastReceiver {
    public InternetConnection() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            boolean isVisible = ActivityVisibleConnection.isActivityVisible();// Check if
            // activity
            // is
            // visible
            // or not
            Log.i("Activity is Visible ", "Is activity visible : " + isVisible);

            // If it is visible then trigger the task else do nothing
            if (isVisible == true) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager
                        .getActiveNetworkInfo();

                // Check internet connection and accrding to state change the
                // text of activity by calling method
                if (networkInfo != null && networkInfo.isConnected()) {

                    new ViewPrincipalActivity().changeStatus(true);
                } else {
                    new ViewPrincipalActivity().changeStatus(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

