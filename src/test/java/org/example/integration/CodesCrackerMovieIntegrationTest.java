package test.java.org.example.integration;


import main.java.org.example.CodesCracker;
import main.java.org.example.Movie;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * Integration test for CodesCracker and Movie classes.
 * Tests the parsing of movie data from files into Movie objects.
 */
public class CodesCrackerMovieIntegrationTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    private File testFile;
    private String originalSystemIn;
    private InputStream testSystemIn;

    @Before
    public void setUp() throws IOException {
        // Create a temporary file for testing
        testFile = tempFolder.newFile("test_movies.txt");

        // Write test data to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("The Matrix, TM001\n");
            writer.write("Sci-Fi, Action, Thriller\n");
            writer.write("Pulp Fiction, PF002\n");
            writer.write("Crime, Drama\n");
            writer.write("Titanic, T003\n");
            writer.write("Romance, Drama, Disaster\n");
        }

        // Save the original System.in
        originalSystemIn = System.in.toString();
    }

    private void setSystemInput(String input) {
        testSystemIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testSystemIn);
    }

    @Test
    public void testMovieParsing() throws TitleException, LetterException, NumberException {
        // Set the test input to provide the test file name
        setSystemInput(testFile.getAbsolutePath() + "\n");

        try {
            // Call the scanner method which will read from our test file
            Vector<Movie> movies = CodesCracker.scanner();

            // Verify the correct number of movies were parsed
            assertEquals(3, movies.size());

            // Verify first movie details
            Movie firstMovie = movies.get(0);
            assertEquals("The Matrix", firstMovie.getTitle());
            assertEquals("TM001", firstMovie.getID());

            Vector<String> expectedGenres = new Vector<>();
            expectedGenres.add("sci-fi");
            expectedGenres.add("action");
            expectedGenres.add("thriller");

            assertEquals(expectedGenres.size(), firstMovie.getGenre().size());
            assertTrue(firstMovie.getGenre().containsAll(expectedGenres));

            // Check third movie
            Movie thirdMovie = movies.get(2);
            assertEquals("Titanic", thirdMovie.getTitle());
            assertEquals("T003", thirdMovie.getID());
            assertEquals(3, thirdMovie.getGenre().size());
            assertTrue(thirdMovie.getGenre().contains("disaster"));
        } finally {
            // Restore the original System.in
            if (testSystemIn != null) {
                try {
                    testSystemIn.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

}