package main.java.org.example;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;
import java.util.*;
import java.io.*;
import java.util.Vector;
public class RecommendationService {

    public static void runRecommendationSystem() throws IOException {
        try (
                BufferedWriter writer = new BufferedWriter(new FileWriter("recommendations.txt"));
                Scanner scan = new Scanner(System.in)
        ) {
            Vector<Movie> movies = loadMovies(writer);
            if (movies == null) return;

            System.out.print("Enter File Name: ");
            String fileName = scan.nextLine();

            List<User> users = loadUsers(fileName, writer);
            if (users == null) return;

            Recommend recommender = new Recommend(users, movies);
            recommender.createGenres();
            recommender.recommendToUsers();
            recommender.outputFile(writer);
        }
    }
    public static Vector<Movie> loadMovies(BufferedWriter writer) throws IOException {
        CodesCracker scan2 = new CodesCracker();
        try {
            return scan2.scanner();
        } catch (TitleException title) {
            writer.write("ERROR: Movie Title {" + title.title + "} is wrong\n");
        } catch (LetterException idLetter) {
            writer.write("ERROR: Movie Id letters {" + idLetter.id + "} are wrong\n");
        } catch (NumberException idNumber) {
            writer.write("ERROR: Movie Id numbers {" + idNumber.id + "} aren't unique\n");
        }
        return null;
    }
    public static List<User> loadUsers(String fileName, BufferedWriter writer) throws IOException {
        File file = new File(fileName);
        if (file.length() == 0) return null;

        List<User> users = new ArrayList<>();
        InputHandling inputHandler = new InputHandling();

        try (BufferedReader bufread = new BufferedReader(new FileReader(file))) {
            String userLine, movieLine;

            while ((userLine = bufread.readLine()) != null) {
                movieLine = bufread.readLine();
                Object[] parsedInput = inputHandler.parseInput(userLine, movieLine);
                if (parsedInput == null) continue;

                String name = (String) parsedInput[0];
                String id = (String) parsedInput[1];
                List<String> movieIDs = (List<String>) parsedInput[2];

                Set<String> idNumbersSeen = new HashSet<>();
                for (String movieId : movieIDs) {
                    String digits = extractDigits(movieId);
                    if (!isValidMovieId(movieId, digits, idNumbersSeen)) {
                        writer.write("ERROR: Movie Id numbers {" + movieId + "} aren't unique\n");
                        return null;
                    }
                    idNumbersSeen.add(digits);
                }

                User user = new User(name, id, movieIDs);
                if (!user.verifyUserName(name)) {
                    writer.write("ERROR: User Name {" + name + "} is wrong\n");
                    return null;
                }
                if (!user.verifyUniqueID(id)) {
                    writer.write("ERROR: User Id {" + id + "} is wrong\n");
                    return null;
                }
                users.add(user);
            }
        } catch (IOException e) {
            writer.write("Exception: " + e + "\n");
        }
        return users;
    }
    public static String extractDigits(String id) {
        StringBuilder digits = new StringBuilder();
        for (char c : id.toCharArray()) {
            if (Character.isLowerCase(c)) return null;
            if (Character.isDigit(c)) digits.append(c);
        }
        return digits.toString();
    }
    public static boolean isValidMovieId(String id, String digits, Set<String> seen) {
        return digits != null && digits.length() == 3 && !seen.contains(digits);
    }
}
