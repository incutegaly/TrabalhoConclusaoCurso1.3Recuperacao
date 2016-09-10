package com.example.computador.crud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelaCadastro extends Activity {

        EditText EdEmail;
        EditText EdConfirmaSenha;
        EditText EdNome;
        EditText EdSobrenome;
        EditText EdIdade;
        EditText EdSenha;
        Button BtCadastrar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrarctivity);
        EdEmail = (EditText) findViewById(R.id.editText3);
        EdConfirmaSenha = (EditText) findViewById(R.id.editText4);
        EdNome = (EditText) findViewById(R.id.editText5);
        EdSobrenome = (EditText) findViewById(R.id.editText9);
        EdIdade = (EditText) findViewById(R.id.editText10);
        EdSenha = (EditText) findViewById(R.id.editText11) ;
        BtCadastrar = (Button) findViewById(R.id.btCadastrar);
    }

    public void cadastrarUsuario(View view) {

        String email = EdEmail.getText().toString();
        String nome = EdNome.getText().toString();
        String sobrenome = EdSobrenome.getText().toString();
        String idade = EdIdade.getText().toString();
        String senha = EdSenha.getText().toString();
        String confirmaSenha = EdConfirmaSenha.getText().toString();

        boolean validacao = true;

        if (email == null || email.equals("")) {

            validacao = false;
            EdEmail.setError("Email Obrigatório");
        }

        if (nome == null || nome.equals("")) {
            validacao = false;
            EdNome.setError("Nome Obrigatório");
        }

        if (sobrenome == null || sobrenome.equals("")) {
            validacao = false;
            EdSobrenome.setError("Sobrenome Obrigatório");
        }

        if (idade == null || idade.equals("")) {
            validacao = false;
            EdIdade.setError("Idade Obrigatória");
        }

        if (senha == null || senha.equals("")) {
            validacao = false;
            EdSenha.setError("Senha Obrigatória");
        }
        if (confirmaSenha == null || confirmaSenha.equals("")) {
            validacao = false;
            EdConfirmaSenha.setError("Confirme sua Senha");
        }


        if(validacao){
            Usuario usu = new Usuario();
            usu.setEmail(EdEmail.getText().toString());
            usu.setSobrenome(EdSobrenome.getText().toString());
            usu.setNome(EdNome.getText().toString());
            usu.setIdade(Integer.parseInt(EdIdade.getText().toString()));
            usu.setSenha(EdSenha.getText().toString());

            DbHelper dbH = new DbHelper(this);
            dbH.insertUsuario(usu);
            finish();
        }
    }

    public void voltarJanela(View view){
        finish();
    }

    }
