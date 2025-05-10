package test.java.org.example.integration;
import main.java.org.example.InputHandling;
import main.java.org.example.User;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration test for InputHandling and User classes.
 * Tests the parsing of user input and creation of User objects.
 */
public class InputHandlingUserIntegrationTest {

    private InputHandling inputHandler;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        inputHandler = new InputHandling();
        // Clear the user ID set before each test
        User.userIDSet.clear();

        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testValidUserInputParsing() {
        // Test input
        String userLine = "Ganna, 123456789";
        String movieLine = "TM001, PF002, T003";

        // Parse the input
        Object[] result = inputHandler.parseInput(userLine, movieLine);

        // Verify result
        assertNotNull(result);
        assertEquals("Ganna", result[0]);
        assertEquals("123456789", result[1]);

        @SuppressWarnings("unchecked")
        List<String> movieIDs = (List<String>) result[2];
        assertEquals(3, movieIDs.size());
        assertEquals("TM001", movieIDs.get(0));
        assertEquals("PF002", movieIDs.get(1));
        assertEquals("T003", movieIDs.get(2));

        // Create a User object from the parsed data
        User user = new User((String) result[0], (String) result[1], (List<String>) result[2]);

        // Verify the User object
        assertEquals("Ganna", user.getUserName());
        assertEquals("123456789", user.getUserID());
        assertEquals(3, user.getMovieIDs().size());
        assertTrue(user.getMovieIDs().contains("TM001"));
        assertFalse(user.getMovieIDs().contains("QP004"));
    }

    @Test
    public void testUserCreationWithParsedInput() {
        // Test input
        String userLine1 = "Alice Smith, 987654321";

        String movieLine = "M001, A002";

        // Parse the input
        Object[] result = inputHandler.parseInput(userLine1, movieLine);

        // Create a User with the parsed data
        User user1 = new User((String) result[0], (String) result[1], (List<String>) result[2]);
        User user2 = new User("Farha Raafat", (String) result[1], (List<String>) result[2]);

        // Verify user properties
        assertEquals("Alice Smith", user1.getUserName());
        assertEquals("987654321", user1.getUserID());
        assertEquals(2, user1.getMovieIDs().size());

        // Test User validation methods with the parsed data
        assertTrue(user1.verifyUserName((String) result[0]));
        assertTrue(user1.verifyUserID((String) result[1]));
        assertTrue(user1.verifyUniqueID("123456789"));
        assertFalse(user2.verifyUniqueID("123456789")); // Same ID should fail uniqueness check

    }

    @Test
    public void testInvalidUserInput() {
        // Test with empty username
        String userLine = ", 123456789";
        String movieLine = "TM001, PF002";

        Object[] result = inputHandler.parseInput(userLine, movieLine);
        assertNull(result);
        assertTrue(outContent.toString().contains("Invalid name in user line"));

        // Reset output capture
        outContent.reset();

        // Test with invalid movie line
        userLine = "Bob Johnson, 123456789";
        movieLine = "";

        result = inputHandler.parseInput(userLine, movieLine);
        assertNull(result);
        assertTrue(outContent.toString().contains("Invalid movie id line"));
    }

    @Test
    public void testEmptyMovieIDsFiltering() {
        // Test with some empty movie IDs
        String userLine = "Jana, 123456789";
        String movieLine = "TM001, , PF002, ";

        Object[] result = inputHandler.parseInput(userLine, movieLine);

        assertNotNull(result);

        @SuppressWarnings("unchecked")
        List<String> movieIDs = (List<String>) result[2];
        assertEquals(2, movieIDs.size()); // Only non-empty IDs should be included
        assertEquals("TM001", movieIDs.get(0));
        assertEquals("PF002", movieIDs.get(1));

        // Verify warning was printed
        assertTrue(outContent.toString().contains("Warning: Empty movie ID found"));
    }

    @Test
    public void testUserIDValidation() {
        // Create a User with parsed input
        String userLine = "David Lee, 123456789";
        String movieLine = "TM001";

        Object[] result = inputHandler.parseInput(userLine, movieLine);
        User user = new User((String) result[0], (String) result[1], (List<String>) result[2]);

        // Test the ID validation methods
        assertFalse(user.verifyUserID("12345")); // Too short
        assertFalse(user.verifyUserID("ABCDEFGHI")); // First char must be a digit
        assertTrue(user.verifyUserID("123456789")); // Valid ID

        // Test name validation
        assertTrue(user.verifyUserName("David Lee"));
        assertFalse(user.verifyUserName("David123")); // Contains digits
    }
}
