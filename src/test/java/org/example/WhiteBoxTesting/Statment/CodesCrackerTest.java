package test.java.org.example.WhiteBoxTesting.Statment;

import main.java.org.example.CodesCracker;
import main.java.org.example.Movie;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CodesCrackerTest {


    // Simulate standard input
    private final InputStream originalIn = System.in;

    @AfterEach
    void restoreSystemInput() {
        System.setIn(originalIn);
    }

    @Test
    public void Test_89percent_valid_input() throws Exception {
        String fileContent = "The Shawshank Redemption, TSR001\n" +
                "Drama\n" +
                "The Godfather, TG002\n" +
                "Crime, Drama\n" +
                "The Dark Knight, TDK003\n" +
                "Action, Crime, Drama";
        File tempFile = File.createTempFile("test_movies", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }

        // Simulate user input for filename
        ByteArrayInputStream in = new ByteArrayInputStream(tempFile.getAbsolutePath().getBytes());
        System.setIn(in);

        Vector<Movie> result = CodesCracker.scanner();

        assertEquals(3, result.size());
        assertEquals("The Shawshank Redemption", result.get(0).getTitle());
    }

    @Test
    public void Test_14percent_FileNotFound() throws Exception {
        // Simulate user input with non-existent file
        ByteArrayInputStream in = new ByteArrayInputStream("non_existent.txt".getBytes());
        System.setIn(in);

        // scanner() should handle the exception and return empty list
        Vector<Movie> result = CodesCracker.scanner();
        assertTrue(result.isEmpty());

    }
    @Test
    public void Test_46percent_lowercaseinstartoftitle() throws Exception {
        String fileContent =
                "The shawshank Redemption, TSR001\n" +
                "Drama\n" +
                "The Godfather, TG002\n" +
                "Crime, Drama\n" +
                "The Dark Knight, TDK003\n" +
                "Action, Crime, Drama";
        File tempFile = File.createTempFile("test_movies", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }

        // Simulate user input for filename
        ByteArrayInputStream in = new ByteArrayInputStream(tempFile.getAbsolutePath().getBytes());
        System.setIn(in);


        assertThrows(TitleException.class, () -> {
            CodesCracker.scanner();
        });
    }

    @Test
    public void Test_46percent_uppercaseinmiddleoftitle() throws Exception {
        String fileContent =
                "The SHawshank Redemption, TSR001\n" +
                        "Drama\n" +
                        "The Godfather, TG002\n" +
                        "Crime, Drama\n" +
                        "The Dark Knight, TDK003\n" +
                        "Action, Crime, Drama";
        File tempFile = File.createTempFile("test_movies", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }

        // Simulate user input for filename
        ByteArrayInputStream in = new ByteArrayInputStream(tempFile.getAbsolutePath().getBytes());
        System.setIn(in);


        assertThrows(TitleException.class, () -> {
            CodesCracker.scanner();
        });
    }

    @Test
    public void Test_57percent_invalidtitlecode() throws Exception {
        String fileContent =
                "The Shawshank Redemption, TRR001\n" +
                        "Drama\n" +
                        "The Godfather, TG002\n" +
                        "Crime, Drama\n" +
                        "The Dark Knight, TDK003\n" +
                        "Action, Crime, Drama";
        File tempFile = File.createTempFile("test_movies", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }

        // Simulate user input for filename
        ByteArrayInputStream in = new ByteArrayInputStream(tempFile.getAbsolutePath().getBytes());
        System.setIn(in);


        assertThrows(LetterException.class, () -> {
            CodesCracker.scanner();
        });
    }

    @Test
    public void Test_89percent_duplicateid() throws Exception {
        String fileContent =
                "The Shawshank Redemption, TSR001\n" +
                        "Drama\n" +
                        "The Godfather, TG001\n" +
                        "Crime, Drama\n" +
                        "The Dark Knight, TDK003\n" +
                        "Action, Crime, Drama";
        File tempFile = File.createTempFile("test_movies", ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(fileContent);
        }

        // Simulate user input for filename
        ByteArrayInputStream in = new ByteArrayInputStream(tempFile.getAbsolutePath().getBytes());
        System.setIn(in);


        assertThrows(NumberException.class, () -> {
            CodesCracker.scanner();
        });
    }

}
