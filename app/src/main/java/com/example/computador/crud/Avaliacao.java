package com.example.computador.crud;

import android.app.Activity;


public class Avaliacao extends Activity {

    String comentario, nome_place , notas;
    int id, nome_usuario;

    public int getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(int nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getNome_place() {
        return nome_place;
    }

    public void setNome_place(String nome_place) {
        this.nome_place = nome_place;
    }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "comentario='" + comentario + '\'' +
                ", nome_place='" + nome_place + '\'' +
                ", notas='" + notas + '\'' +
                ", id=" + id +
                ", nome_usuario=" + nome_usuario +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Avaliacao(String comentario, int nome_usuario, String nome_place,String notas) {
        this.comentario = comentario;
        this.nome_usuario = nome_usuario;
        this.nome_place = nome_place;
        this.notas = notas;
    }

    public Avaliacao(){

    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
