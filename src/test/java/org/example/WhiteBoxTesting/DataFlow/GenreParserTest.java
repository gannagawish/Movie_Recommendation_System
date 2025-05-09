package test.java.org.example.WhiteBoxTesting.DataFlow;

import main.java.org.example.CodesCracker;
import org.junit.jupiter.api.Test;


import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

class GenreParserTest {

        @Test
        public void testMultipleGenres() {
            Vector<String> result = CodesCracker.genreParser("Action, Drama, Comedy");
            Vector<String> expected = new Vector<>();
            expected.add("Action");
            expected.add("Drama");
            expected.add("Comedy");
            assertEquals(expected, result);
        }

        @Test
        public void testGenresWithSpaces() {
            Vector<String> result = CodesCracker.genreParser("  Horror  ,  Sci-Fi ");
            Vector<String> expected = new Vector<>();
            expected.add("Horror");
            expected.add("Sci-Fi");
            assertEquals(expected, result);
        }

        @Test
        public void testEmptyLine() {
            Vector<String> result = CodesCracker.genreParser("");
            Vector<String> expected = new Vector<>();
            expected.add("");
            assertEquals(expected, result);
        }

        @Test
        public void testSingleGenre() {
            Vector<String> result = CodesCracker.genreParser("Thriller");
            Vector<String> expected = new Vector<>();
            expected.add("Thriller");
            assertEquals(expected, result);
        }
    }
