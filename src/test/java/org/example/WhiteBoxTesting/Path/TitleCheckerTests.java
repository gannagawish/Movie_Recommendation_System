package test.java.org.example.WhiteBoxTesting.Path;

import main.java.org.example.CodesCracker;
import main.java.org.example.exceptions.TitleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleCheckerTests {

        // TC1 - Valid title, all words start with uppercase, no internal caps
        @Test
        public void testValidTitle_TC1_P1() throws TitleException {
            String title = "The Godfather";
            String result = CodesCracker.titleChecker(title);
            assertEquals("TG", result);
        }

        // TC2 - First word starts with lowercase letter
        @Test
        public void testFirstWordLowercase_TC2_P2() {
            String title = "the Godfather";
            assertThrows(TitleException.class, () -> CodesCracker.titleChecker(title));
        }

        // TC3 - A word contains uppercase letter in the middle
        @Test
        public void testUppercaseInsideWord_TC3_P3() {
            String title = "The godFather";
            assertThrows(TitleException.class, () -> CodesCracker.titleChecker(title));
        }

        // TC4 - Single lowercase character word
        @Test
        public void testSingleLowercaseChar_TC4_P4() {
            String title = "g";
            assertThrows(TitleException.class, () -> CodesCracker.titleChecker(title));
        }

        // TC5 - First word valid, second word invalid (uppercase inside)
        @Test
        public void testMixedWords_TC5_P5() {
            String title = "The GodFather";
            assertThrows(TitleException.class, () -> CodesCracker.titleChecker(title));
        }


    }
