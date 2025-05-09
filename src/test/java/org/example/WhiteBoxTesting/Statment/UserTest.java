package test.java.org.example.WhiteBoxTesting.Statment;

import main.java.org.example.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {// this confirms 100 coverage for verify userid

    @BeforeEach
    void clearSet() {
        User.userIDSet.clear();
    }

    @Test
    void verifyUserName() {
        User user = new User("farha", "123456789", null);
        User user1 = new User("farha raafat", "123456789", null);
        assertTrue(user.verifyUserName("farha"));
        assertTrue(user1.verifyUserName("farha raafat"));
    }

    @Test
    void verifyUserNameFalse() {
        User user = new User("farha987raafat", "123456789", null);
        User user1 = new User(" farha","123456789",null);
        User user2 = new User("gana$","123456789",null);
        assertFalse(user.verifyUserName("farha987raafat"));
        assertFalse(user1.verifyUserName(" farha"));
        assertFalse(user2.verifyUserName("gana$"));
    }

    @Test
    void verifyUserID() {
        User user = new User("farha raafat", "123456789", null);
        User user1 = new User("farha raafat", "12345678F", null);
        User user2 = new User("farha raafat", "1fagjtfte", null);
        assertTrue(user.verifyUserID("123456789"));
        assertTrue(user1.verifyUserID("12345678F"));
        assertTrue(user2.verifyUserID("1fagjtfte"));
    }

    @Test
    void verifyUserIDFalse() {
        User user = new User("farha raafat", "12345678", null);
        User user1 = new User("farha raafat", "A23456789", null);
        User user2 = new User("farha raafat", "123ofg78", null);
        assertFalse(user.verifyUserID("12345678"));
        assertFalse(user1.verifyUserID("A23456789"));
        assertFalse(user2.verifyUserID("123ofg78"));
    }

    @Test
    void verifyUniqueID() {
        User user = new User("farha", "123456789", null);
        User user1 = new User("gana", "123456789", null);
        assertTrue(user.verifyUniqueID("123456789"));
        assertFalse(user1.verifyUniqueID("123456789"));
    }
    @Test
    void testVerifyUniqueID_invalidFormat() {
        User u = new User("Test", "123"); // Invalid: length != 9
        assertFalse(u.verifyUniqueID("123")); // Case 1
    }

    @Test
    void testVerifyUniqueID_validAndUnique() {
        User u = new User("Test", "12345678A"); // Valid format
        assertTrue(u.verifyUniqueID("12345678A")); // Case 2
    }

    @Test
    void testVerifyUniqueID_validButDuplicate() {
        User u = new User("Test", "12345678B"); // Valid format
        assertTrue(u.verifyUniqueID("12345678B")); // first time → true
        assertFalse(u.verifyUniqueID("12345678B")); // second time → false (already exists) — Case 3
    }
}
