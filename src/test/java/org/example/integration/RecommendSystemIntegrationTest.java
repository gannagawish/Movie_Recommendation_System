package test.java.org.example.integration;


import main.java.org.example.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Integration test for the Recommend class with all other components.
 * Tests the complete recommendation system flow.
 */
public class RecommendSystemIntegrationTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Vector<Movie> movies;
    private List<User> users;
    private Recommend recommender;
    private File outputFile;

    @Before
    public void setUp() throws IOException {
        // Create test movies
        movies = new Vector<>();

        Movie movie1 = new Movie();
        movie1.setTitle("The Matrix");
        movie1.setID("TM001");
        Vector<String> genres1 = new Vector<>();
        genres1.add("sci-fi");
        genres1.add("action");
        movie1.setGenre(genres1);
        movies.add(movie1);

        Movie movie2 = new Movie();
        movie2.setTitle("Inception");
        movie2.setID("I002");
        Vector<String> genres2 = new Vector<>();
        genres2.add("sci-fi");
        genres2.add("thriller");
        movie2.setGenre(genres2);
        movies.add(movie2);

        Movie movie3 = new Movie();
        movie3.setTitle("Pulp Fiction");
        movie3.setID("PF003");
        Vector<String> genres3 = new Vector<>();
        genres3.add("crime");
        genres3.add("drama");
        movie3.setGenre(genres3);
        movies.add(movie3);

        Movie movie4 = new Movie();
        movie4.setTitle("The Godfather");
        movie4.setID("TG004");
        Vector<String> genres4 = new Vector<>();
        genres4.add("crime");
        genres4.add("drama");
        movie4.setGenre(genres4);
        movies.add(movie4);

        Movie movie5 = new Movie();
        movie5.setTitle("Interstellar");
        movie5.setID("I005");
        Vector<String> genres5 = new Vector<>();
        genres5.add("sci-fi");
        genres5.add("adventure");
        movie5.setGenre(genres5);
        movies.add(movie5);

        // Create test users
        users = new ArrayList<>();

        // User 1 likes sci-fi (The Matrix)
        User user1 = new User("John Doe", "123456789");
        user1.setMovieIDs(Arrays.asList("TM001"));
        users.add(user1);

        // User 2 likes crime (Pulp Fiction)
        User user2 = new User("Jane Smith", "987654321");
        user2.setMovieIDs(Arrays.asList("PF003"));
        users.add(user2);

        // User 3 likes multiple genres
        User user3 = new User("Bob Johnson", "123123123");
        user3.setMovieIDs(Arrays.asList("TM001", "PF003"));
        users.add(user3);

        // Initialize the recommender
        recommender = new Recommend(users, movies);

        // Create output file
        outputFile = tempFolder.newFile("recommendations.txt");
    }

    @Test
    public void testOutputFile() throws IOException {
        // Set up the genre map and recommendations
        recommender.createGenres();
        recommender.recommendToUsers();

        // Write recommendations to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            recommender.outputFile(writer);
        }

        // Read the output file and verify contents
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (line.startsWith("John Doe")) {
                    String recommendationsLine = reader.readLine();
                    lineCount++;
                    assertTrue(recommendationsLine.contains("Inception"));
                    assertTrue(recommendationsLine.contains("Interstellar"));
                } else if (line.startsWith("Jane Smith")) {
                    String recommendationsLine = reader.readLine();
                    lineCount++;
                    assertTrue(recommendationsLine.contains("The Godfather"));
                }
            }

            // We should have 6 lines (2 per user: username line + recommendations line)
            assertEquals(6, lineCount);
        }
    }

    @Test
    public void testCompleteIntegrationFlow() throws IOException {
        // Test the entire flow from input parsing to recommendation generation

        // 1. Mock input data
        String userLine = "Test User, 555555555";
        String movieLine = "TM001, I002";

        // 2. Use InputHandling to parse input
        InputHandling inputHandler = new InputHandling();
        Object[] parsed = inputHandler.parseInput(userLine, movieLine);

        assertNotNull(parsed);

        // 3. Create user from parsed input
        User user = new User((String)parsed[0], (String)parsed[1], (List<String>)parsed[2]);

        // 4. Add to users list
        List<User> testUsers = new ArrayList<>();
        testUsers.add(user);

        // 5. Create a recommender with our test data
        Recommend testRecommender = new Recommend(testUsers, movies);

        // 6. Generate recommendations
        testRecommender.createGenres();
        testRecommender.recommendToUsers();

        // 7. Check recommendations for our test user
        Map<User, HashSet<String>> recommendsMap = testRecommender.getRecommendsMap();
        HashSet<String> recommendations = recommendsMap.get(user);

        // User likes The Matrix and Inception (both Sci-Fi),
        // so should get Interstellar recommendation but not the original movies
        assertTrue(recommendations.contains("Interstellar"));
        assertFalse(recommendations.contains("The Matrix"));
        assertFalse(recommendations.contains("Inception"));

        // Should not get recommendations for unrelated genres
        assertFalse(recommendations.contains("Pulp Fiction"));
        assertFalse(recommendations.contains("The Godfather"));
    }
}
