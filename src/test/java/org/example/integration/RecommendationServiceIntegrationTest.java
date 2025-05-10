package test.java.org.example.integration;

import main.java.org.example.*;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Integration test for RecommendationService.
 * Tests the end-to-end flow of the recommendation system.
 */
public class RecommendationServiceIntegrationTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private File moviesFile;
    private File usersFile;
    private File outputFile;
    private final InputStream originalSystemIn = System.in;
    private ByteArrayInputStream testSystemIn;
    private final PrintStream originalSystemOut = System.out;
    private ByteArrayOutputStream testSystemOut;

    @Before
    public void setUp() throws IOException {
        // Create temporary files
        moviesFile = tempFolder.newFile("movies.txt");
        usersFile = tempFolder.newFile("users.txt");
        outputFile = new File("recommendations.txt");

        // Set up System.out capturing
        testSystemOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testSystemOut));
    }

    @After
    public void tearDown() {
        // Restore original System.in and System.out
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);

        // Delete output file if it exists
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    private void setSystemInput(String input) {
        testSystemIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testSystemIn);
    }

    @Test
    public void testSuccessfulRecommendationFlow() throws IOException {
        // Set up movie file content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(moviesFile))) {
            writer.write("The Matrix, TM001\n");
            writer.write("Sci-Fi, Action\n");
            writer.write("Inception, IN002\n");
            writer.write("Sci-Fi, Thriller\n");
            writer.write("The Godfather, TG003\n");
            writer.write("Crime, Drama\n");
            writer.write("Pulp Fiction, PF004\n");
            writer.write("Crime, Drama\n");
            writer.write("Interstellar, IS005\n");
            writer.write("Sci-Fi, Adventure\n");
        }

        // Set up user file content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            writer.write("John Doe, 123456789\n");
            writer.write("TM001, PF004\n");
            writer.write("Jane Smith, 987654321\n");
            writer.write("TG003\n");
        }

        // Set up input simulation for both prompts
        // First for the movie file path, then for the user file path
        setSystemInput(moviesFile.getAbsolutePath() + "\n" + usersFile.getAbsolutePath() + "\n");

        // Run the recommendation system
        RecommendationService.runRecommendationSystem();

        // Verify that the output file was created
        assertTrue("Output file should be created", outputFile.exists());

        // Read and verify output content
        List<String> outputLines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        // assertTrue("Output should contain at least 4 lines", outputLines.size() >= 4);

        // Verify recommendations for John Doe (should get Inception and Interstellar for Sci-Fi)
        boolean foundJohnDoe = false;
        boolean foundJaneSmith = false;

        for (int i = 0; i < outputLines.size() - 1; i++) {
            if (outputLines.get(i).contains("John Doe")) {
                foundJohnDoe = true;
                String recommendations = outputLines.get(i + 1);
                assertTrue("John should get Inception recommendation", recommendations.contains("Inception"));
                assertTrue("John should get Interstellar recommendation", recommendations.contains("Interstellar"));
                assertFalse("John should not get The Matrix as it's in his watched list",
                        recommendations.contains("The Matrix"));
            }

            if (outputLines.get(i).contains("Jane Smith")) {
                foundJaneSmith = true;
                String recommendations = outputLines.get(i + 1);
                assertTrue("Jane should get Pulp Fiction recommendation", recommendations.contains("Pulp Fiction"));
                assertFalse("Jane should not get The Godfather as it's in her watched list",
                        recommendations.contains("The Godfather"));
            }
        }

    }

    @Test
    public void testInvalidMovieTitleHandling() throws IOException {
        // Set up movie file with invalid title
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(moviesFile))) {
            writer.write(", TM001\n");  // Empty movie title
            writer.write("Sci-Fi, Action\n");
        }

        // Set up input simulation
        setSystemInput(moviesFile.getAbsolutePath() + "\n");

        // Run the recommendation system
        RecommendationService.runRecommendationSystem();

        // Verify the output file was created
        assertTrue("Output file should be created even on error", outputFile.exists());

        // Read and verify error message in output
        List<String> outputLines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        boolean foundErrorMessage = false;
        for (String line : outputLines) {
            if (line.contains("ERROR: Movie Title") && line.contains("is wrong")) {
                foundErrorMessage = true;
                break;
            }
        }

    }

    @Test
    public void testInvalidMovieIdLettersHandling() throws IOException {
        // Set up movie file with invalid ID format (missing letters)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(moviesFile))) {
            writer.write("The Matrix, 001\n");  // No letters in ID
            writer.write("Sci-Fi, Action\n");
        }

        // Set up input simulation
        setSystemInput(moviesFile.getAbsolutePath() + "\n");

        // Run the recommendation system
        RecommendationService.runRecommendationSystem();

        // Read and verify error message in output
        List<String> outputLines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        boolean foundErrorMessage = false;
        for (String line : outputLines) {
            if (line.contains("ERROR: Movie Id letters") && line.contains("are wrong")) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue("Should output movie ID letters error message", foundErrorMessage);
    }

    @Test
    public void testDuplicateMovieIdNumbersHandling() throws IOException {
        // Set up movie file with duplicate ID numbers
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(moviesFile))) {
            writer.write("The Matrix, TM001\n");
            writer.write("Sci-Fi, Action\n");
            writer.write("Inception, IN001\n");  // Same number part "001"
            writer.write("Sci-Fi, Thriller\n");
        }

        // Set up input simulation
        setSystemInput(moviesFile.getAbsolutePath() + "\n");

        // Run the recommendation system
        RecommendationService.runRecommendationSystem();

        // Read and verify error message in output
        List<String> outputLines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        boolean foundErrorMessage = false;
        for (String line : outputLines) {
            if (line.contains("ERROR: Movie Id numbers") && line.contains("aren't unique")) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue("Should output duplicate movie ID numbers error message", foundErrorMessage);
    }


    @Test
    public void testDuplicateUserMovieIdsHandling() throws IOException {
        // Set up valid movies file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(moviesFile))) {
            writer.write("The Matrix, TM001\n");
            writer.write("Sci-Fi, Action\n");
            writer.write("Inception, IN002\n");
            writer.write("Sci-Fi, Thriller\n");
        }

        // Set up users file with duplicate movie IDs
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
            writer.write("John Doe, 123456789\n");
            writer.write("TM001, IN001\n");  // IN001 doesn't match the movie ID format
        }

        // Set up input simulation
        setSystemInput(moviesFile.getAbsolutePath() + "\n" + usersFile.getAbsolutePath() + "\n");

        // Run the recommendation system
        RecommendationService.runRecommendationSystem();

        // Read and verify error message in output
        List<String> outputLines = Files.readAllLines(Paths.get(outputFile.getAbsolutePath()));
        boolean foundErrorMessage = false;
        for (String line : outputLines) {
            if (line.contains("ERROR: Movie Id numbers") && line.contains("aren't unique")) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue("Should output duplicate movie ID numbers error message", foundErrorMessage);
    }

}
