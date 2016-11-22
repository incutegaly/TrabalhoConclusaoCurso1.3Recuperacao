package com.example.computador.crud;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class AvaliacaoPlaces extends Activity {

    private static final String PREF_NAME = "LoginActivityPreference";
    String placeName, placeVicinity, comentario;
    public static String KEY_NAME = "Name";
    public static String KEY_ADDRESS = "Address";
    Float notas;
    boolean validacao;
    int position;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliacao_item);

        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
         data = sp.getString("Name", "");
         position = sp.getInt("codigo", 0);

        final Spinner spinnerVagas = (Spinner) findViewById(R.id.spinner);
        final Spinner spinnerBanheiro = (Spinner) findViewById(R.id.spinner1);
        final Spinner spinnerSinalizacao = (Spinner) findViewById(R.id.spinner2);
        final Spinner spinnerCalcada = (Spinner) findViewById(R.id.spinner3);
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        final TextView textView = (TextView) findViewById(R.id.textView19);
        Button button = (Button) findViewById(R.id.button5);
        final EditText editText = (EditText) findViewById(R.id.editText20);


        ArrayAdapter adapterVagas  = ArrayAdapter.createFromResource(this, R.array.string_array_vagas, android.R.layout.simple_spinner_item);
        spinnerVagas.setAdapter(adapterVagas);
        spinnerSinalizacao.setAdapter(adapterVagas);


        ArrayAdapter adapterAvaliacao  = ArrayAdapter.createFromResource(this, R.array.string_array_avaliacao, android.R.layout.simple_spinner_item);
        spinnerBanheiro.setAdapter(adapterAvaliacao);
        spinnerCalcada.setAdapter(adapterAvaliacao);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validacao = true;

                comentario = editText.getText().toString();

                if(comentario.equals("")){
                    editText.setError("Comentário Obrigatório");
                    validacao = false;
                }
                else if(notas==null){
                    Toast.makeText(getApplicationContext(), "Avaliação Obrigatória", Toast.LENGTH_SHORT).show();
                    validacao = false;
                }

                else if(validacao){
                DbHelper dbHelper = new DbHelper(getApplicationContext());

                String item1 = spinnerVagas.getSelectedItem().toString();
                String item2 = spinnerBanheiro.getSelectedItem().toString();
                String item3 = spinnerSinalizacao.getSelectedItem().toString();
                String item4 = spinnerCalcada.getSelectedItem().toString();

                    try{
                    dbHelper.insertLugaresNovo(position, data, notas, comentario, item1, item2, item3, item4, AvaliacaoPlaces.this );
                    Toast.makeText(getApplicationContext(), "Avaliação feita!", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Log.e("Error: ", e.getMessage());
                }
            finish();
                }}
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    textView.setText("Valor = " +rating);
                    notas = rating;
            }
        });

    }

}
