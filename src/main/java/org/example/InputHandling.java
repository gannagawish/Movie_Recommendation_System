package main.java.org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputHandling {
    // Method to parse two lines: one for username,userid and one for a comma-separated list of movieids

    public Object[] parseInput(String userLine, String movieLine) {
        // Validate the user line (username,userid)
        if (userLine == null || userLine.trim().isEmpty()) {
            System.out.println("Invalid user line: empty or null.");
            return null;
        }

        // Split the user line into username and userid
        String[] userParts = userLine.split(",");
        if (userParts.length != 2) {
            System.out.println("Invalid format in user line: '" + userLine + "'. Expected format: username,userid");
            return null;
        }

        String name = userParts[0].trim();
        String id = userParts[1].trim();

        // Basic validation: ensure neither field is empty
        if (name.isEmpty()) {
            System.out.println("Invalid name in user line: '" + userLine + "'. Name cannot be empty.");
            return null;
        }
        if (id.isEmpty()) {
            System.out.println("Invalid id in user line: '" + userLine + "'. Id cannot be empty.");
            return null;
        }

        // Validate the movie line (comma-separated movieids)
        if (movieLine == null || movieLine.trim().isEmpty()) {
            System.out.println("Invalid movie id line: empty or null.");
            return null;
        }

        // Split the movie line into individual movie IDs
        String[] movieIDArray = movieLine.split(",");
        List<String> movieIDs = new ArrayList<>();
        for (String movieID : movieIDArray) {
            movieID = movieID.trim();
            if (movieID.isEmpty()) {
                System.out.println("Warning: Empty movie ID found in movie line: '" + movieLine + "'. Skipping this movie ID.");
                continue;
            }
            movieIDs.add(movieID);
        }

        // Ensure at least one valid movie ID was provided
        if (movieIDs.isEmpty()) {
            System.out.println("Error: No valid movie IDs found in movie line: '" + movieLine + "'.");
            return null;
        }

        return new Object[]{name, id, movieIDs};
    }
}