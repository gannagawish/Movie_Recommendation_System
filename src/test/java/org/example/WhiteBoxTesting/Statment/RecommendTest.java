package test.java.org.example.WhiteBoxTesting.Statment;

import main.java.org.example.Movie;
import main.java.org.example.Recommend;
import main.java.org.example.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RecommendTest {

    private Recommend recommend;
    private Vector<Movie> testMovies;
    private List<User> testUsers;

    @BeforeEach
    void setUp() {
        testMovies = new Vector<>();
        testUsers = new Vector<>();

        //setup test movies
        testMovies.add(new Movie("Movie1", "1", new Vector<>(Arrays.asList("Action", "Adventure","Comedy"))));
        testMovies.add(new Movie("Movie2", "2", new Vector<>(Arrays.asList("Comedy", "Romance"))));
        testMovies.add(new Movie("Movie3", "3", new Vector<>(Arrays.asList("Action", "Drama"))));
        testMovies.add(new Movie("Movie4", "4", new Vector<>(Arrays.asList("Comedy"))));

        //setup test users
        User user1 = new User("User1", "U1");
        user1.setMovieIDs(Arrays.asList("1", "3")); //likes a movies
        testUsers.add(user1);

        User user2 = new User("User2", "U2");
        user2.setMovieIDs(Arrays.asList("2")); //likes Comedy
        testUsers.add(user2);

        recommend = new Recommend(testUsers, testMovies);
    }

    @Test
    void testCreateGenres() {
        recommend.createGenres();
        Map<String, Vector<String>> genreMap = recommend.getGenreMap();

        //verify genre map was created correctly
        assertEquals(5, genreMap.size()); //{action, adventure, comedy, romance, drama}

        //check specific genres
        assertTrue(genreMap.containsKey("Action"));
        assertEquals(2, genreMap.get("Action").size());
        assertTrue(genreMap.get("Action").contains("Movie1"));
        assertTrue(genreMap.get("Action").contains("Movie3"));

        assertTrue(genreMap.containsKey("Comedy"));
        assertEquals(3, genreMap.get("Comedy").size());
        assertTrue(genreMap.get("Comedy").contains("Movie2"));
        assertTrue(genreMap.get("Comedy").contains("Movie4"));
    }

    @Test
    void testRecommendToUsers() {
        recommend.createGenres();
        recommend.recommendToUsers();
        Map<User, HashSet<String>> recommendsMap = recommend.getRecommendsMap();

        User user1 = testUsers.get(0);
        HashSet<String> user1Recs = recommendsMap.get(user1);
        assertFalse(user1Recs.contains("Movie3"));
        assertTrue(user1Recs.contains("Movie2")); //shouldn't recommend what they already have

        User user2 = testUsers.get(1);
        HashSet<String> user2Recs = recommendsMap.get(user2);
        assertTrue(user2Recs.contains("Movie4"));
        assertFalse(user2Recs.contains("Movie2"));

        assertTrue(user1Recs.contains("Movie4"));
        assertFalse(user2Recs.contains("Movie3"));
    }
    @Test
    void testNormalOutput() throws IOException {
        Recommend r = new Recommend();

        // Add test data
        User u1 = new User("Alice", "001");
        User u2 = new User("Bob", "002");
        r.getRecommendsMap().put(u1, new LinkedHashSet<>(Arrays.asList("m1", "m2")));
        r.getRecommendsMap().put(u2, new LinkedHashSet<>(Collections.singletonList("m3")));

        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);

        r.outputFile(bw);
        bw.flush();

        String expected =
                "Alice 001\nm1, m2\n" +
                        "Bob 002\nm3\n";

        assertEquals(expected, sw.toString());
    }

    @Test
    void testIOExceptionHandling() {
        Recommend r = new Recommend();

        User u1 = new User("Charlie", "003");
        r.getRecommendsMap().put(u1, new HashSet<>(Collections.singletonList("m5")));

        // BufferedWriter that always throws IOException
        BufferedWriter throwingWriter = new BufferedWriter(new Writer() {
            @Override public void write(char[] cbuf, int off, int len) throws IOException { throw new IOException("Fake IO"); }
            @Override public void flush() throws IOException {}
            @Override public void close() throws IOException {}
        });

        assertDoesNotThrow(() -> r.outputFile(throwingWriter));
    }



}