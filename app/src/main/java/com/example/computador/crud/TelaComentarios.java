package com.example.computador.crud;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static com.example.computador.crud.SelectedPlaceDetails.setListViewHeightBasedOnChildren;


public class TelaComentarios extends Activity {

    private static final String PREF_NAME = "String";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comentarios);
        List<String> list;
        List<String> concatena = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String placeName = sp.getString("lugar", "");

        ListView lista = (ListView) findViewById(R.id.listComentario);

        DbHelper dbHelper = new DbHelper(this);

        list = dbHelper.selectUsuarioAvaliacaoPlace(placeName);

        for(int i=0; i<list.size(); i++){
            concatena.add(list.get(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, concatena );
        lista.setAdapter(adapter);
        setListViewHeightBasedOnChildren(lista);

    }


}


