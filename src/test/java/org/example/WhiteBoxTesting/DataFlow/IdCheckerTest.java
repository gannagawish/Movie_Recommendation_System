package test.java.org.example.WhiteBoxTesting.DataFlow;
import main.java.org.example.CodesCracker;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
class IdCheckerTest {
        @Test
        public void testValidID() throws Exception {
            Set<String> ids = new HashSet<>();
            CodesCracker.IDChecker("ABC123", "ABC", ids);
            assertTrue(ids.contains("123"));
        }

        @Test
        public void testWrongPrefix() {
            Set<String> ids = new HashSet<>();
            assertThrows(LetterException.class, () -> {
                CodesCracker.IDChecker("ABC123", "DEF", ids);
            });
        }

        @Test
        public void testDuplicateNumber() {
            Set<String> ids = new HashSet<>();
            ids.add("123");
            assertThrows(NumberException.class, () -> {
                CodesCracker.IDChecker("ABC123", "ABC", ids);
            });
        }

        @Test
        public void testWrongLength() {
            Set<String> ids = new HashSet<>();
            assertThrows(NumberException.class, () -> {
                CodesCracker.IDChecker("ABC12", "ABC", ids);
            });
        }
    }
