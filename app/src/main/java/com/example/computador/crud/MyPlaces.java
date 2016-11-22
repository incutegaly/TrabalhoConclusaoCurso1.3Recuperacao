package com.example.computador.crud;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MyPlaces extends Activity {
    private static final String PREF_NAME = "LoginActivityPreference";
    private static final String String = "String";
    DbHelper dbH = new DbHelper(this);
    ListView listView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lugares);


        listView = (ListView) findViewById(R.id.listViewLugares);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (MyPlaces.this, TelaComentarios.class);
                SharedPreferences so = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        String name;
                        int  pos = so.getInt("codigo", 0);
                        List<String> lista;
                        lista = dbH.selectPlaces(pos);
                        name = lista.get(position);
                        SharedPreferences sp = getSharedPreferences(String, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("lugar", name);
                        editor.commit();
                        startActivity(intent);
                return true;
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        int  position = sp.getInt("codigo", 0);
        List<String> lista;
        try{
            lista = dbH.selectPlaces(position);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista );
            listView.setAdapter(adapter);
        }catch (Exception e ){
            Log.i("Error:  ", e.getMessage());
        }
    }
}
