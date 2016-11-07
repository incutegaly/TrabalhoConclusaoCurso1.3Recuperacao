package com.example.computador.crud;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;

class InternetCheck extends AsyncTask<Activity, Void, Boolean> {


    protected Boolean doInBackground(Activity... activitys) {
            return isNetworkAvailable(activitys[0]);
        }

    protected void onProgressUpdate(Integer... progress) {

    }


    protected void onPostExecute(String result) {

    }

    public static boolean isNetworkAvailable(Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}