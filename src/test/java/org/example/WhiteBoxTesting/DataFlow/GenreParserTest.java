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
            expected.add("action");
            expected.add("drama");
            expected.add("comedy");
            assertEquals(expected, result);
        }

        @Test
        public void testGenresWithSpaces() {
            Vector<String> result = CodesCracker.genreParser("  Horror  ,  Sci-Fi ");
            Vector<String> expected = new Vector<>();
            expected.add("horror");
            expected.add("sci-fi");
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
            expected.add("thriller");
            assertEquals(expected, result);
        }
    }
