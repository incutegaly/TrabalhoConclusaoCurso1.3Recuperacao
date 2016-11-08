package com.example.computador.crud;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MyPlaces extends Activity {
    private static final String PREF_NAME = "LoginActivityPreference";


    ListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lugares);


        listView = (ListView) findViewById(R.id.listViewLugares);

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int  position = sp.getInt("codigo", 0);
        List<String> lista;
        DbHelper dbH = new DbHelper(this);
        try{
            lista = dbH.selectEstabelecimentos(position);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista );
            listView.setAdapter(adapter);
        }catch (Exception e ){
            Log.i("Error:  ", e.getMessage());
        }
    }
}
