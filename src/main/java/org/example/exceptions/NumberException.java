package main.java.org.example.exceptions;

public class NumberException extends Exception {
    public String id;

    public NumberException(String id) {
        this.id = id;
    }
}
