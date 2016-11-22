package com.example.computador.crud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private static final String PREF_NAME = "LoginActivityPreference";

        EditText EdEmail;
        EditText EdSenha;
        Button BtEntrar;
        CheckBox SaveLogin;
        private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdEmail = (EditText) findViewById(R.id.editText);
        EdSenha = (EditText) findViewById(R.id.editText2);
        BtEntrar = (Button) findViewById(R.id.button);
        SaveLogin = (CheckBox) findViewById(R.id.checkBox);
        TextView view = (TextView) findViewById(R.id.textView14);
        view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        setTitle("ACESSA");

        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String email = sp.getString("email", "");
        String senha = sp.getString("senha", "");



        db = new DbHelper(this);

            if (db.logar(email, senha) == true) {
                int position = db.busca(email, senha);
                Intent intent = new Intent(this, ViewPrincipalActivity.class);
                startActivity(intent);
                this.finish();
            }

    }


    public void novaJanela(View view) {

        Intent intent = new Intent(this,TelaCadastro.class );
        startActivity(intent);
    }

    public void novaJanela2(View view) {

        Intent intent = new Intent(this,EsqueceuSenha.class );
        startActivity(intent);
    }




    public void logar(View view){

        String email = EdEmail.getText().toString();
        String senha = EdSenha.getText().toString();


            boolean validacao = true;

                if(email == null || email.equals("")){
                    validacao = false;
                    EdEmail.setError(getString(R.string.login_valUsuario));
                }

                if(senha == null || senha.equals("")){
                    validacao = false;
                    EdSenha.setError(getString(R.string.login_valSenha));
                }

                if(db.buscaEmail(email)== true){

                    if(!db.senha(email,senha)) {
                    validacao = false;
                    Toast.makeText(getApplicationContext(), "Senha incorreta", Toast.LENGTH_SHORT).show();
                     }
                }

                 if(validacao) {
                    if (db.logar(email, senha) == true) {
                        int position =  db.busca(email,senha);
                        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("codigo", position);
                        editor.commit();
                            if(SaveLogin.isChecked()){
                                editor.putString("email", email);
                                editor.putString("senha", senha);
                                editor.commit();
                            }


                    Intent intent = new Intent(this, ViewPrincipalActivity.class);
                    this.finish();
                    startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Usuario não cadastrado", Toast.LENGTH_SHORT).show();
                    }
                }
    }



    }


