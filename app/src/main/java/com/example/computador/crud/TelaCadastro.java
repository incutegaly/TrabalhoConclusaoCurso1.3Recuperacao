package com.example.computador.crud;

import android.app.Activity;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class TelaCadastro extends Activity {
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );


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
        boolean senhaValidacao = false;

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
        if (!checkEmail(email)) {
            validacao = false;
            EdEmail.setError("Email Inválido");
        }

        if (senha.equals(confirmaSenha)) {
            senhaValidacao = true;
        } else {
            validacao = false;
            EdSenha.setError("Senhas diferentes");
        }


        if (validacao & senhaValidacao) {


            Usuario usu = new Usuario();
            usu.setEmail(EdEmail.getText().toString());
            usu.setSobrenome(EdSobrenome.getText().toString());
            usu.setNome(EdNome.getText().toString());
            usu.setIdade(Integer.parseInt(EdIdade.getText().toString()));
            usu.setSenha(EdSenha.getText().toString());
            DbHelper dbH = new DbHelper(this);

            if(dbH.buscaEmail(email)==true){
                Toast.makeText(getApplicationContext(),"Usuario Já Possui Conta", Toast.LENGTH_SHORT).show();
            }else{
                dbH.insertUsuario(usu);
                Toast.makeText(getApplicationContext(),"Usuario Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    public void voltarJanela(View view){
        finish();
    }



    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }}
