package com.example.computador.crud;



public class Comentarios {

    private double notas;

    @Override
    public String toString() {
        return "Comentarios{" +
                "comentario='" + comentario + '\'' +
                '}';
    }



    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String nome_place , comentario;


    public Comentarios(){}


    public double getNotas() {
        return notas;
    }

    public void setNotas(double notas) {
        this.notas = notas;
    }

    public String getNome_place() {
        return nome_place;
    }

    public void setNome_place(String nome_place) {
        this.nome_place = nome_place;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
