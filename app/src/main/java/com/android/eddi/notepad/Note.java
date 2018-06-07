package com.android.eddi.notepad;

public class Note {
    private int id;
    private String titolo;
    private String contenuto;

    public Note(String titolo, String contenuto, int id) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }
}