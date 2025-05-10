package test.java.org.example.UnitTesting;

import main.java.org.example.*;
import main.java.org.example.Movie;
import main.java.org.example.RecommendationService;
import main.java.org.example.User;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RecommendationServiceTest {

    @Mock
    private BufferedWriter mockWriter;
    @TempDir
    Path tempDir;

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //Redirect System.out for testing
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testRunRecommendationSystem_SuccessfulExecution() throws IOException {
        // Create test files
        Path moviesFile = tempDir.resolve("movies.txt");
        Path usersFile = tempDir.resolve("users.txt");
        Files.writeString(moviesFile, "The Matrix, TMX123\nAction, Sci-Fi\n");
        Files.writeString(usersFile, "John Doe, 123456789\nTMX123\n");
        // Set up scanner to return the test file name
        System.setIn(new ByteArrayInputStream(usersFile.toString().getBytes()));
        // Create test movies and users
        Vector<Movie> testMovies = createTestMovies();
        List<User> testUsers = createTestUsers();
        try (MockedStatic<RecommendationService> mockedStatic = mockStatic(RecommendationService.class)) {
            // Allow the original method to be called
            mockedStatic.when(() -> RecommendationService.runRecommendationSystem()).thenCallRealMethod();
            // Mock the loadMovies and loadUsers methods
            mockedStatic.when(() -> RecommendationService.loadMovies(any())).thenReturn(testMovies);
            mockedStatic.when(() -> RecommendationService.loadUsers(anyString(), any())).thenReturn(testUsers);
            // Execute the method
            RecommendationService.runRecommendationSystem();
            // Verify that the output contains the prompt
            assertTrue(outputStream.toString().contains("Enter File Name:"));
            // Verify that loadMovies and loadUsers were called
            mockedStatic.verify(() -> RecommendationService.loadMovies(any()));
            mockedStatic.verify(() -> RecommendationService.loadUsers(anyString(), any()));
        } finally {
            System.setIn(System.in);
            System.setOut(originalOut);
        }
    }
    @Test
    void testLoadMovies_Success() throws IOException, TitleException, LetterException, NumberException {
        Vector<Movie> expectedMovies = createTestMovies();
        try (MockedStatic<CodesCracker> mockedStatic = mockStatic(CodesCracker.class)) {
            mockedStatic.when(CodesCracker::scanner).thenReturn(expectedMovies);
            // Execute the method
            Vector<Movie> result = RecommendationService.loadMovies(mockWriter);
            // Verify the result
            assertEquals(expectedMovies, result);
            verify(mockWriter, never()).write(anyString()); // No errors should be written
        }
    }

    @Test
    void testLoadMovies_TitleException() throws IOException, TitleException, LetterException, NumberException {
        String invalidTitle = "invalid title";
        TitleException exception = new TitleException(invalidTitle);
        try (MockedStatic<CodesCracker> mockedStatic = mockStatic(CodesCracker.class)) {
            mockedStatic.when(CodesCracker::scanner).thenThrow(exception);

            // Execute the method
            Vector<Movie> result = RecommendationService.loadMovies(mockWriter);

            // Verify the result
            assertNull(result);
            verify(mockWriter).write("ERROR: Movie Title {" + invalidTitle + "} is wrong\n");
        }
    }

    @Test
    void testLoadMovies_LetterException() throws IOException, TitleException, LetterException, NumberException {
        String invalidId = "Ab123";
        LetterException exception = new LetterException(invalidId);

        try (MockedStatic<CodesCracker> mockedStatic = mockStatic(CodesCracker.class)) {
            // Mock the static method to throw the exception
            mockedStatic.when(CodesCracker::scanner).thenThrow(exception);

            // Execute the method
            Vector<Movie> result = RecommendationService.loadMovies(mockWriter);

            // Verify that the result is null
            assertNull(result);

            // Verify that the writer received the correct error message
            verify(mockWriter).write("ERROR: Movie Id letters {" + invalidId + "} are wrong\n");
        }
    }

    @Test
    void testLoadMovies_NumberException() throws IOException, TitleException, LetterException, NumberException {
        String invalidId = "TM12";
        NumberException exception = new NumberException(invalidId);

        try (MockedStatic<CodesCracker> mockedStatic = mockStatic(CodesCracker.class)) {
            mockedStatic.when(CodesCracker::scanner).thenThrow(exception);

            // Execute the method
            Vector<Movie> result = RecommendationService.loadMovies(mockWriter);

            // Verify the result
            assertNull(result);
            verify(mockWriter).write("ERROR: Movie Id numbers {" + invalidId + "} aren't unique\n");
        }
    }

    @Test
    void testLoadUsers_Success() throws IOException {
        // Create a temporary file with valid user data
        Path usersFile = tempDir.resolve("testUsers.txt");
        Files.writeString(usersFile, "John Doe, 123456789\nTMX123, SHR456\n");

        // Execute the method
        List<User> result = RecommendationService.loadUsers(usersFile.toString(), mockWriter);

        // Verify the result is not null (detailed verification is complex due to User object)
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getUserName());
        assertEquals("123456789", result.get(0).getUserID());
        assertEquals(Arrays.asList("TMX123", "SHR456"), result.get(0).getMovieIDs());
    }

    @Test
    void testLoadUsers_EmptyFile() throws IOException {
        // Create an empty file
        Path emptyFile = tempDir.resolve("emptyUsers.txt");
        Files.createFile(emptyFile);

        // Execute the method
        List<User> result = RecommendationService.loadUsers(emptyFile.toString(), mockWriter);

        // Verify the result
        assertNull(result);
    }

    @Test
    void testLoadUsers_InvalidUserName() throws IOException {
        // Create a temporary file with invalid user name
        Path usersFile = tempDir.resolve("invalidUserName.txt");
        Files.writeString(usersFile, "123 Invalid, 123456789\nTMX123\n");

        // Execute the method
        List<User> result = RecommendationService.loadUsers(usersFile.toString(), mockWriter);

        // Verify the result
        assertNull(result);
        verify(mockWriter).write("ERROR: User Name {123 Invalid} is wrong\n");
    }

    @Test
    void testLoadUsers_InvalidUserId() throws IOException {
        // Create a temporary file with invalid user ID
        Path usersFile = tempDir.resolve("invalidUserId.txt");
        Files.writeString(usersFile, "Valid Name, 123458X\nTMX123\n");

        // Execute the method
        List<User> result = RecommendationService.loadUsers(usersFile.toString(), mockWriter);

        // Verify the result
        assertNull(result);
        verify(mockWriter).write("ERROR: User Id {123458X} is wrong\n");
    }

    @Test
    void testLoadUsers_InvalidMovieIds() throws IOException {
        // Create a temporary file with duplicate movie ID numbers
        Path usersFile = tempDir.resolve("invalidMovieIds.txt");
        Files.writeString(usersFile, "Valid Name, 123456789\nTMX123, TMY123\n");

        // Execute the method
        List<User> result = RecommendationService.loadUsers(usersFile.toString(), mockWriter);

        // Verify the result
        assertNull(result);
        verify(mockWriter).write("ERROR: Movie Id numbers {TMY123} aren't unique\n");
    }

    // Helper methods
    private Vector<Movie> createTestMovies() {
        Vector<Movie> movies = new Vector<>();

        Movie movie1 = new Movie();
        movie1.setTitle("The Matrix");
        movie1.setID("TMX123");
        Vector<String> genres1 = new Vector<>();
        genres1.add("Action");
        genres1.add("Sci-Fi");
        movie1.setGenre(genres1);

        Movie movie2 = new Movie();
        movie2.setTitle("Inception");
        movie2.setID("INC456");
        Vector<String> genres2 = new Vector<>();
        genres2.add("Sci-Fi");
        genres2.add("Thriller");
        movie2.setGenre(genres2);

        movies.add(movie1);
        movies.add(movie2);

        return movies;
    }

    private List<User> createTestUsers() {
        List<User> users = new ArrayList<>();

        List<String> movieIDs1 = new ArrayList<>();
        movieIDs1.add("TMX123");
        User user1 = new User("John Doe", "123456789", movieIDs1);

        List<String> movieIDs2 = new ArrayList<>();
        movieIDs2.add("INC456");
        User user2 = new User("Jane Smith", "987654321", movieIDs2);

        users.add(user1);
        users.add(user2);

        return users;
    }
}