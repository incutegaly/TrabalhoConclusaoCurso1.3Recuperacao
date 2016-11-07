package com.example.computador.crud;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.widget.ProgressBar;


public class SplashScreen extends Activity {

    protected boolean mbActive;
    protected static final int TIMER_RUNTIME = 10000;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        if(!isNetworkAvailable()){
            AlertDialog.Builder CheckBuilder = new AlertDialog.Builder(SplashScreen.this);
            CheckBuilder.setIcon(R.mipmap.ic_launcher);
            CheckBuilder.setTitle("Error");
            CheckBuilder.setMessage("Sem conexão com Internet");
            CheckBuilder.setPositiveButton("Tente Novamente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                   //Restartando Activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            CheckBuilder.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = CheckBuilder.create();
            alertDialog.show();
        }

        else{
            if(isNetworkAvailable()){
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                    mbActive = true;
                        int counter = 0;
                        try{
                            while(mbActive && (counter < TIMER_RUNTIME)){

                                Thread.sleep(200);
                                if(mbActive){
                                    counter+=200;
                                    updateProgress(counter);
                                }
                            }
                        }       catch (InterruptedException e) {
                                e.printStackTrace();
                        }finally {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }).start();
            }

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void updateProgress (final int timePassed){
        if(null!= progressBar){
            final int progress = progressBar.getMax() * timePassed / TIMER_RUNTIME;
            progressBar.setProgress(progress);
        }
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
