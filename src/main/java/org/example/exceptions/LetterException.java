package main.java.org.example.exceptions;

public class LetterException extends Exception {
    public String id;

    public LetterException(String id) {
        this.id = id;
    }
}
