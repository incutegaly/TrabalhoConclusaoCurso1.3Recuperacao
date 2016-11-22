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
    private static final String AVALIACAO = "Avaliacao";
    private static final String AVALIACAO_NOVO = "AvaliacaoLugares";
    private static final String LUGARES = "Lugares";
    /* private static final String NOME_USUARIO = "nome";
     private static final String USUARIO_PASSWORD = "senha";
     private static final String IDADE_USUARIO = "idade";
     private static final String UID = "id";
     */
    private static final String AVALIACAO_LUGARES = "CREATE TABLE " + AVALIACAO +
            " (_ID integer primary key autoincrement, id_usuario INTEGER, Name_Place TEXT, Notas Double, Comentario TEXT, FOREIGN KEY (id_usuario) REFERENCES Usuario(id))";

    private static final String AVALIACAO_CRIA_TABELA = "CREATE TABLE " + LUGARES +
            " (_ID integer primary key autoincrement, place_name TEXT, place_vinicity TEXT)";

    private static final String AVALIACAO_LUGARES_NOVO = "CREATE TABLE " + AVALIACAO_NOVO +
            " (_ID integer primary key autoincrement, id_usuario INTEGER, Name_Place TEXT, Notas Float, Comentario TEXT, Vagas Text, Banheiro Text, Sinalizacao Text, Calcada Text,  FOREIGN KEY (id_usuario) REFERENCES Usuario(id))";

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
        db.execSQL(AVALIACAO_CRIA_TABELA);
        db.execSQL(AVALIACAO_LUGARES);
        db.execSQL(AVALIACAO_LUGARES_NOVO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //excluindo tabelas
        String sqlDropTableUsuario = "DROP TABLE Usuario";
        db.execSQL("drop table " + AVALIACAO);
        db.execSQL("drop table " + LUGARES);
        db.execSQL("drop table " + AVALIACAO_NOVO);
        db.execSQL(sqlDropTableUsuario);


        //criando novamente
        onCreate(db);

    }

    public DbHelper insertLugares (String name_place, String place_vinicity, Context context) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();


        cv.put("place_name", name_place);
        cv.put("place_vinicity", place_vinicity);
        try {
            db.insert("Lugares", null, cv);
            db.close();
            Toast.makeText(context, "Cadastrado!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
        return null;

    }

    public DbHelper insertLugaresNovo (int id, String name_place, float notas, String comentario, String item1, String item2, String item3, String item4, Context context) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id_usuario" , id);
        cv.put("Name_Place", name_place);
        cv.put("Notas", notas);
        cv.put("Comentario", comentario);
        cv.put("Vagas", item1);
        cv.put("Banheiro", item2);
        cv.put("Sinalizacao", item3);
        cv.put("Calcada", item4);
        try {
            db.insert("AvaliacaoLugares", null, cv);
            db.close();
            Toast.makeText(context, "Avaliação Feita!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
        return null;

    }

    public List<Float> selectNotasPlace (String name_place){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT Notas FROM AvaliacaoLugares WHERE Name_Place = '" +name_place+ "'";
        List<Float> notas = new ArrayList<Float>();
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if(c.moveToFirst()){
            do{
                notas.add(c.getFloat(0));
            }while (c.moveToNext());
        }
        db.close();
        return notas;
    }


    public List<String> selectUsuarioAvaliacaoPlace (String name_place){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT u.nome, a.Comentario, a.Vagas, a.Banheiro, a.Sinalizacao, a.Calcada FROM Usuario u JOIN AvaliacaoLugares a ON a.id_usuario = u.id AND a.Name_Place = '" +name_place+ "'";
        List<String> comentarios = new ArrayList<String>();
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if(c.moveToFirst()){
            do{
                comentarios.add ("Usuario:  " + c.getString(0) + "\n"
                        +  "Comentario: " + c.getString(1) + "\n"
                        +  "Possui vaga para deficiente? "  + c.getString(2) + "\n"
                        +  "Banheiro acessivel: " + c.getString(3) + "\n"
                        +  "Possui sinalizacão interna? " + c.getString(4) + "\n"
                        +  "Calçada acessivel  " + c.getString(5) + "\n" );
            }while (c.moveToNext());
        }
        db.close();
        return comentarios;
    }

    public List<String> selectPlaces (int position){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT DISTINCT(Name_Place) FROM AvaliacaoLugares where id_usuario = '" + position + "'";
        List<String> comentarios = new ArrayList<String>();
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if(c.moveToFirst()){
            do{
                comentarios.add (c.getString(0));
            }while (c.moveToNext());
        }
        db.close();
        return comentarios;
    }

   /* public boolean SelectPlaces1(int position) {
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = {position};
        Cursor cursor = db.query("Usuario", null, "email= ? and senha= ?", campos, null, null, null, null);
        if (cursor.moveToFirst()) {
            return true;
        }
        return false;
    }*/
    public DbHelper insertAvaliacao (String name_place, int id, double media, String comentario, Context context){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("id_usuario", id);
        cv.put("Name_Place", name_place);
        cv.put("Notas" , media) ;
        cv.put("Comentario", comentario);
        try{
            db.insert("Avaliacao", null, cv);
            db.close();
            Toast.makeText(context, "Avaliação feita!", Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }
        return null;
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
        } else {
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

    public boolean senha(String email, String senha) {
        int position = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[] campos = {email};
        Cursor c = db.query("Usuario", null, "email= ?", campos, null, null, null, null);
        if (c.moveToFirst()) {
            position = busca(email, senha);
            if (position != 0)
                return true;
        }
        return false;
    }
}

