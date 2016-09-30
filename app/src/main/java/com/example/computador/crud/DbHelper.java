package com.example.computador.crud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class DbHelper extends SQLiteOpenHelper {

    private static final String NOME_BASE = "CadastroUsuario";
    /* private static final String NOME_USUARIO = "nome";
     private static final String USUARIO_PASSWORD = "senha";
     private static final String IDADE_USUARIO = "idade";
     private static final String UID = "id";
     */
    private static final int VERSAO_BASE = 11;
    private SQLiteDatabase db;

    public DbHelper(Context context) {
        super(context, NOME_BASE, null, 11);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //inserindo no banco
        String sqlCreateTabelaUsuario = "CREATE TABLE Usuario("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "email TEXT,"
                + "nome TEXT,"
                + "sobrenome TEXT,"
                + "idade INTEGER,"
                + "senha INTEGER"
                + ")";

        db.execSQL(sqlCreateTabelaUsuario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //excluindo tabelas
        String sqlDropTableUsuario = "DROP TABLE Usuario";
        db.execSQL(sqlDropTableUsuario);


        //criando novamente
        onCreate(db);

    }

    public void insertUsuario(Usuario usu) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("email", usu.getEmail());
        cv.put("nome", usu.getNome());
        cv.put("sobrenome", usu.getSobrenome());
        cv.put("idade", usu.getIdade());
        cv.put("senha", usu.getSenha());

        db.insert("Usuario", null, cv);
        db.close();
    }

    public void atualizarUsuario(Usuario usu, int position) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", usu.getEmail());
        cv.put("nome", usu.getNome());
        cv.put("sobrenome", usu.getSobrenome());
        cv.put("idade", usu.getIdade());
        cv.put("senha", usu.getSenha());

        db.update("Usuario", cv, "id = ?", new String[]{"" + position});
    }

    public void deletarUsuario(Usuario usu) {

        db.delete("Usuario", " id = " + usu.getId(), null);

    }

    public boolean logar(String email, String senha) {
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = {email, senha};
        Cursor cursor = db.query("Usuario", null, "email= ? and senha= ?", campos, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;


    }

    public int busca(String email, String senha) {
        int position = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = {email, senha};
        Cursor cursor = db.query("Usuario", null, "email= ? and senha= ?", campos, null, null, null, null);
        if (cursor.moveToFirst()) {
            position = cursor.getInt(0);
        }
        return position;
    }

    public List<Usuario> selectTodosOsUsuarios() {

        List<Usuario> ListUsuario = new ArrayList<Usuario>();

        SQLiteDatabase db = getReadableDatabase();

        String sqlSelectTodoUsuarios = "SELECT * FROM Usuario";

        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if (c.moveToFirst()) {
            do {
                Usuario usu = new Usuario();
                usu.setId(c.getInt(0));
                usu.setEmail(c.getString(1));
                usu.setNome(c.getString(2));
                usu.setSobrenome(c.getString(3));
                usu.setIdade(c.getInt(4));
                usu.setSenha(c.getString(5));

                ListUsuario.add(usu);
            } while (c.moveToNext());
        }

        db.close();
        return ListUsuario;
    }

    public Usuario SelectUsuario(int position) {

        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT * FROM Usuario where id = " + position;

        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if (c.moveToFirst()) {
            do {
                Usuario usu = new Usuario();
                usu.setId(c.getInt(0));
                usu.setEmail(c.getString(1));
                usu.setNome(c.getString(2));
                usu.setSobrenome(c.getString(3));
                usu.setIdade(c.getInt(4));
                usu.setSenha(c.getString(5));
                return usu;
            } while (c.moveToNext());
        } else {
            db.close();
            return null;
        }
    }


    public boolean buscaEmail(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = {email};
        Cursor cursor = db.query("Usuario", null, "email= ?", campos, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        else{
            return false;
        }
    }
    public int retornaEmail(String email) {
        int position = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = {email};
        Cursor cursor = db.query("Usuario", null, "email= ?", campos, null, null, null, null);
        if (cursor.moveToFirst()) {
            position = cursor.getInt(0);
        }
        return position;
    }
}
