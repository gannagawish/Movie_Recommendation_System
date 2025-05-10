package main.java.org.example.exceptions;

public class TitleException extends Exception {
    public String title;

    public TitleException(String title) {
        this.title = title;
    }
}
