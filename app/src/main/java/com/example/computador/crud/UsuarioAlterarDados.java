package com.example.computador.crud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class UsuarioAlterarDados extends AppCompatActivity {
    private static final String PREF_NAME = "LoginActivityPreference";

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    EditText edEmail;
    EditText edNome;
    EditText edSobrenome;
    EditText edIdade;
    EditText edSenha;
    Button btAlterar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_usuario_alterar_dados);
        edEmail = (EditText) findViewById(R.id.editText7);
        edNome = (EditText) findViewById(R.id.editText8);
        edSobrenome = (EditText) findViewById(R.id.editText6);
        edIdade = (EditText) findViewById(R.id.editText12);
        edSenha = (EditText) findViewById(R.id.editText13);
        btAlterar = (Button) findViewById(R.id.btAlterar);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("codigo", 0);


            DbHelper dbHelper = new DbHelper(this);
            Usuario usu = new Usuario();
            usu = dbHelper.SelectUsuario(position);

            edIdade.toString();


            edEmail.setText(usu.getEmail());
            edNome.setText(usu.getNome());
            edSobrenome.setText(usu.getSobrenome());
            edIdade.setText(Integer.toString(usu.getIdade()));
            edSenha.setText(usu.getSenha());


    }

    public void alterarUsuario(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String email = edEmail.getText().toString();
        String nome = edNome.getText().toString();
        String sobrenome = edSobrenome.getText().toString();
        String idade = edIdade.getText().toString();
        String senha = edSenha.getText().toString();

        boolean validacao = true;

        if (email == null || email.equals("")) {

            validacao = false;
            edEmail.setError("Email Obrigatório");
        }

        if (nome == null || nome.equals("")) {
            validacao = false;
            edNome.setError("Nome Obrigatório");
        }

        if (sobrenome == null || sobrenome.equals("")) {
            validacao = false;
            edSobrenome.setError("Sobrenome Obrigatório");
        }

        if (idade == null || idade.equals("")) {
            validacao = false;
            edIdade.setError("Idade Obrigatória");
        }

        if (senha == null || senha.equals("")) {
            validacao = false;
            edSenha.setError("Senha Obrigatória");
        }
        if(checkEmail(email)==false) {
            validacao = false;
            edEmail.setError("Email Invalido");
        }

            if(validacao){
            Usuario usuario = new Usuario();
            usuario.setEmail(edEmail.getText().toString());
            usuario.setNome(edNome.getText().toString());
            usuario.setSobrenome(edSobrenome.getText().toString());
            usuario.setIdade(Integer.parseInt(edIdade.getText().toString()));
            usuario.setSenha(edSenha.getText().toString());
            DbHelper db = new DbHelper(this);
            int position = sharedPreferences.getInt("codigo", 0);
            db.atualizarUsuario(usuario, position);
            Toast.makeText(getApplicationContext(),"Usuario Alterado Com Sucesso", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void voltarJanela(View view){
        finish();
    }


    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
}
