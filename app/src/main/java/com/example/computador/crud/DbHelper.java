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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //excluindo tabelas
        String sqlDropTableUsuario = "DROP TABLE Usuario";
        db.execSQL("drop table " + AVALIACAO);
        db.execSQL("drop table " + LUGARES);
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


    public List<String> selectUsuarioAvaliacao (String name_place){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT u.nome, a.Comentario FROM Usuario u JOIN Avaliacao a ON a.id_usuario = u.id AND a.Name_Place = '" +name_place+ "'";
        List<String> comentarios = new ArrayList<String>();
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if(c.moveToFirst()){
            do{
                comentarios.add (c.getString(0));
                comentarios.add (c.getString(1));
            }while (c.moveToNext());
        }
        db.close();
        return comentarios;
    }

    public List<String> selectEstabelecimentos (int position){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT Name_Place FROM Avaliacao, Usuario where Avaliacao.id_usuario AND Usuario.id = '" + position + "'";
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

    public List<Double> selectNotas (String name_place){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT Notas FROM Avaliacao WHERE Name_Place = '" +name_place+ "'";
        List<Double> notas = new ArrayList<Double>();
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if(c.moveToFirst()){
            do{
                notas.add(c.getDouble(0));
            }while (c.moveToNext());
        }
        db.close();
        return notas;
    }


    public List<String> selectLugares (){
        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT * FROM Lugares";
        List<String> lugares = new ArrayList<String>();
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if(c.moveToFirst()){
            do{
                lugares.add (c.getString(1));
            }while (c.moveToNext());
        }
        db.close();
        return lugares;
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

    public Usuario SelectUsuarioa(int position) {

        SQLiteDatabase db = getReadableDatabase();
        String sqlSelectTodoUsuarios = "SELECT nome FROM Usuario where id = " + position;
        String nome;
        Cursor c = db.rawQuery(sqlSelectTodoUsuarios, null);

        if (c.moveToFirst()) {
            do {
                Usuario usu = new Usuario();

                usu.setNome(c.getString(2));

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

