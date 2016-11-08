package com.example.computador.crud;

import android.app.Activity;



public class AvaliacaoItems extends Activity {

    String nome, comentario;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AvaliacaoItems(String nome, String comentario) {
        this.nome = nome;
        this.comentario = comentario;
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public AvaliacaoItems(){

    }
}
