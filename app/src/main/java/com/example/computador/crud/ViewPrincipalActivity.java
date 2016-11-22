package com.example.computador.crud;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ViewPrincipalActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener, TesteFragment.OnFragmentInteractionListener {
    private static final String PREF_NAME = "LoginActivityPreference";
    protected boolean mbActive = false;
    protected static  int TIMER_RUNTIME = 10000;
    private ListView listPlaces;
    private AlertDialog alerta; //atributo da classe
    GPSManager userCoordinates;
    AlertDialogManager alert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_principal);
        setTitle("ACESSA");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

            android.app.FragmentManager fragmentManager = getFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.rl_fragment_container, new RecyclerViewAdapter()).commit();
            fragmentManager.beginTransaction().replace(R.id.content_frame1, new TesteFragment()).commit();


    }

    @Override
   public void onStart(){
       super.onStart();
            InternetCheck internetCheck = new InternetCheck();
            if (internetCheck.isNetworkAvailable(this)) {
                changeStatus(true);
            } else {
                changeStatus(false);
            }
        }

    public boolean check(){
        InternetCheck internetCheck = new InternetCheck();
        if(internetCheck.isNetworkAvailable(this)){
            changeStatus(true);
            return true;
        } else {
            changeStatus(false);
            return false;
        }

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.app.FragmentManager fm = getFragmentManager();
        // Handle navigation view item clicks here.;
        int id = item.getItemId();


            // Handle the camera action
          if (id == R.id.nav_gallery) {
            android.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame1)).commit();
            //fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.rl_fragment_container)).commit();
            fragmentManager.beginTransaction().replace(R.id.fragment, new FiltroFragment()).commit();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, MyPlaces.class);
              startActivity(intent);

          } else if (id == R.id.nav_usuario) {
            Intent intent = new Intent(this, UsuarioAlterarDados.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Mensagem");
        //define a mensagem
        builder.setMessage("Deseja Realmente sair?");
        //define um botão como positivo
        builder.setPositiveButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                finish();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void changeStatus(boolean isConnected) {

        // Change status according to boolean value
        if (!isConnected && mbActive==false) {
            AlertDialog.Builder CheckBuilder = new AlertDialog.Builder(ViewPrincipalActivity.this);
            mbActive = true;
            CheckBuilder.setIcon(R.mipmap.ic_launcher);
            CheckBuilder.setTitle("Error");
            CheckBuilder.setMessage("Sem conexão com Internet");

            CheckBuilder.setNegativeButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alerta  = CheckBuilder.create();
            alerta.show();

        }

    }

    @Override
    protected void onPause() {

        super.onPause();
        ActivityVisibleConnection.activityPaused();// On Pause notify the Application
    }

    @Override
    protected void onResume() {

        super.onResume();
       ActivityVisibleConnection.activityResumed();// On Resume notify the Application
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                check();
                ha.postDelayed(this, 10000);
            }
        }, 10000);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //alerta.isShowing();
        if (alerta!= null) {
            alerta.dismiss();
            alerta = null;
        }
    }
}



