package com.example.computador.crud;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.content.SharedPreferences;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by Jaqueline on 25/09/2016.
 */

public class EsqueceuSenha extends Activity implements OnClickListener {

    Session session = null;
    ProgressDialog pdialog = null;
    Context context = null;

    EditText EdEmail;
    EditText EdConfirmaEmail;
    String senha, rec, subject, textMessage;;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esqueceu_sua_senha);

        context = this;
        Button enviarSenha = (Button)  findViewById(R.id.btSenha);
        EdEmail = (EditText) findViewById(R.id.editText14);
        EdConfirmaEmail = (EditText) findViewById(R.id.editText15);

        enviarSenha.setOnClickListener(this);

    }


    public void onClick(View v) {



        String email = EdEmail.getText().toString();
        String confirmaEmail = EdConfirmaEmail.getText().toString();

        boolean validacao = true;

        if (email == null || email.equals("")) {

            validacao = false;
            EdEmail.setError("Email Obrigatório");
        }

        if (confirmaEmail == null || confirmaEmail.equals("")) {
            validacao = false;
            EdConfirmaEmail.setError("Confirme seu Email");
        }


        if (validacao) {

            if (email.equals(confirmaEmail)){
                DbHelper dbH = new DbHelper(this);
                if (dbH.buscaEmail(email) == true) {
                    int position = dbH.retornaEmail(email);
                    Usuario usu = new Usuario();
                   usu = dbH.SelectUsuario(position);
                    senha = usu.getSenha();

                    if (!email.equals("")) {

                        rec = EdEmail.getText().toString();
                        subject = "RECUPERAÇÃO DE SENHA";
                        textMessage = senha;

                        Properties props = new Properties();
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.socketFactory.port", "465");
                        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.port", "465");

                        session = Session.getDefaultInstance(props, new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication("jaquelinesilvaoliveira2012@gmail.com", "524783421");
                            }
                        });

                        pdialog = ProgressDialog.show(context, "", "Enviando email...", true);

                        RetreiveFeedTask task = new RetreiveFeedTask();
                        task.execute();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Este email não corresponde ao de login", Toast.LENGTH_SHORT).show();

                }

            } else {
                Toast.makeText(getApplicationContext(), "Erro ao digitar os e-mails", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("testfrom354@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Messagem enviada", Toast.LENGTH_LONG).show();
        }
    }


    public void voltarJanela(View view){
        finish();
    }
}


