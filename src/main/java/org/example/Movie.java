package main.java.org.example;

import java.util.*;

public class Movie {
    String title;
    String ID;
    Vector<String>genre;
public Movie(){};
    public Movie(String title, String ID, Vector<String> genre) {
        this.title = title;
        this.ID = ID;
        this.genre = genre;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Vector<String> getGenre() {
        return genre;
    }

    public void setGenre(Vector<String> genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}


