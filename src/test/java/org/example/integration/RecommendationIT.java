package test.java.org.example.integration;


import main.java.org.example.Movie;
import main.java.org.example.Recommend;
import main.java.org.example.User;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecommendationIT {
    @Test
    void Recommendation_Integration_Testing(){
        List<User> users = List.of(new User("Ganna","210077398",List.of("TSR001","TG002")),
                new User("Farouha","210077391",List.of("TDK003")));

        Vector<Movie> movies = new Vector<>();
        Vector<String> generes1 = new Vector<>();
        Vector<String> generes2 = new Vector<>();
        Vector<String> generes3 = new Vector<>();
        generes1.add("Drama");
        generes2.add("Crime");
        generes2.add("Drama");
        generes3.add("Crime");
        generes3.add("Drama");
        generes3.add("Action");


        movies.add(new Movie("The Shawshank Redemption","TSR001",generes1));
        movies.add(new Movie("The Godfather", "TG002",generes2));
        movies.add(new Movie("The Dark Knight", "TDK003",generes3));

        Recommend recommend = new Recommend(users, movies);
        recommend.createGenres();
        recommend.recommendToUsers();

        Map<User, HashSet<String>> recs = recommend.getRecommendsMap();
        HashSet<String> user1 = recs.get(users.get(0));
        HashSet<String> user2 = recs.get(users.get(1));


        assertTrue(user1 .contains("The Dark Knight"));
        assertFalse(user1 .contains("The Godfather"));
        assertTrue(user2 .contains("The Shawshank Redemption"));
        assertTrue(user2 .contains("The Godfather"));
        assertFalse(user2 .contains("The Dark Knight"));

      //  assertFalse(user1 .contains("Matrix"));


    }

}
