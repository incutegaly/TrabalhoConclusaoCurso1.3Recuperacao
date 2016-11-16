package com.example.computador.crud;



public class StringParametro {

    private String nome;
    private int photo;

    public StringParametro(String nome, int photo) {
        this.nome = nome;
        this.photo = photo;
    }

    public StringParametro(){
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
