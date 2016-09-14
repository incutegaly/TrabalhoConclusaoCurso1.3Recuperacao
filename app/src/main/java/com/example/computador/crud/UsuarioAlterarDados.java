package com.example.computador.crud;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;

public class UsuarioAlterarDados extends AppCompatActivity {
    private static final String PREF_NAME = "LoginActivityPreference";

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
        edEmail =  (EditText) findViewById(R.id.editText7);
        edNome = (EditText)findViewById(R.id.editText8);
        edSobrenome = (EditText) findViewById(R.id.editText6);
        edIdade = (EditText) findViewById(R.id.editText12);
        edSenha = (EditText) findViewById(R.id.editText13);
        btAlterar = (Button) findViewById(R.id.btAlterar);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        edEmail.setHint(sharedPreferences.getString("email",""));


    }

    public void alterarUsuario(View view){
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String email = edEmail.getText().toString();
        String nome = edNome.getText().toString();
        String sobrenome = edSobrenome.getText().toString();
        String idade = edIdade.getText().toString();
        String senha = edSenha.getText().toString();

        boolean validacao = true;

        if(email==null || email.equals("")) {

            validacao = false;
            edEmail.setError("Email Obrigatório");
        }
        if(nome==null || nome.equals("")) {
            validacao = false;
            edNome.setError("Nome Obrigatório");
        }

        if(sobrenome==null || sobrenome.equals("")) {
            validacao = false;
            edSobrenome.setError("Sobrenome Obrigatório");
        }

         if(idade==null || idade.equals("")){
             validacao = false;
             edIdade.setError("Idade Obrigatória");
        }

         if(senha==null || senha.equals("")){
            validacao = false;
            edSenha.setError("Senha Obrigatória");
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
            finish();
        }
    }


}
