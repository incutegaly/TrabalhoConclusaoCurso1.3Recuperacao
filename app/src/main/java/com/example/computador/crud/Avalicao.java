package com.example.computador.crud;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Avalicao extends Activity {
    private static final String PREF_NAME = "LoginActivityPreference";
    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;

    String placeName, placeVicinity, placeLat, placeLng, placePhotoRef, placeDistance, placeType;


    public static String KEY_REFERENCE = "Reference";
    public static String KEY_NAME = "Name";
    public static String KEY_ADDRESS = "Address";
    public static String KEY_TYPE = "Type";
    public static String KEY_PHOTOREFERENCE = "PhotoReference";
    public static String KEY_DISTANCE = "Distance";
    public static String KEY_LATITUDE = "Latitude";
    public static String KEY_LONGITUDE = "Longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);

        editText1 = (EditText) findViewById(R.id.editText16);
        editText2 = (EditText) findViewById(R.id.editText17);
        editText3 = (EditText) findViewById(R.id.editText18);
        editText4 = (EditText) findViewById(R.id.editText19);


    }

    public void avaliar(View view) {
        DbHelper db = new DbHelper(this);

        int nota1 = Integer.parseInt(editText1.getText().toString());
        int nota2 = Integer.parseInt(editText2.getText().toString());
        int nota3 = Integer.parseInt(editText3.getText().toString());
        String comentario = editText4.getText().toString();

        double media = (nota1 + nota2 + nota3) / 3;

        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String data = sp.getString("Name", "");
        int position = sp.getInt("codigo", 0);

        try{

            db.insertAvaliacao(data, position, media, comentario, Avalicao.this);
        }catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }

        Intent fromMain = getIntent();
        placeVicinity = fromMain.getStringExtra(KEY_ADDRESS);
        placeName = fromMain.getStringExtra(KEY_NAME);
        setTitle(placeName);


        try{
            db.insertLugares(placeName,placeVicinity, Avalicao.this);
        }catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

    }
}
