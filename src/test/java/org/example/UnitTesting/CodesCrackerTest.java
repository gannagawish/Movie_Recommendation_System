package test.java.org.example.UnitTesting;

import main.java.org.example.*;
import main.java.org.example.exceptions.LetterException;
import main.java.org.example.exceptions.NumberException;
import main.java.org.example.exceptions.TitleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CodesCrackerTest {
    private Set<String> idNumbers;

    @BeforeEach
    void setUp() {
        idNumbers = new HashSet<>();
    }

    @Test
    void testTitleParser_ValidInput() throws Exception {
        Movie movie = CodesCracker.titleParser("The Godfather, TG002", idNumbers);
        assertEquals("The Godfather", movie.getTitle());
        assertEquals("TG002", movie.getID());
    }

    @Test
    void testTitleParser_InvalidTitle() {
        assertThrows(TitleException.class, () -> {
            CodesCracker.titleParser("the Godfather, TG002", idNumbers);
        });
    }

    @Test
    void testTitleParser_InvalidIDLetters() {
        assertThrows(LetterException.class, () -> {
            CodesCracker.titleParser("The Godfather, Tg002", idNumbers);
        });
    }

    @Test
    void testTitleParser_InvalidIDNumbers() throws Exception {
        idNumbers.add("002");
        assertThrows(NumberException.class, () -> {
            CodesCracker.titleParser("The Godfather, TG002", idNumbers);
        });
    }

    @Test
    void testTitleParser_MalformedInput() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            CodesCracker.titleParser("The Godfather", idNumbers);
        });
    }

    @Test
    void testGenreParser_SingleGenre() {
        Vector<String> result = CodesCracker.genreParser("Drama");
        assertEquals(1, result.size());
        assertEquals("drama", result.get(0));
    }

    @Test
    void testGenreParser_MultipleGenres() {
        Vector<String> result = CodesCracker.genreParser("Crime, Drama, Thriller");
        assertEquals(3, result.size());
        assertEquals("crime", result.get(0));
        assertEquals("drama", result.get(1));
        assertEquals("thriller", result.get(2));
    }

    @Test
    void testGenreParser_GenresWithSpaces() {
        Vector<String> result = CodesCracker.genreParser("Sci-Fi,  Action , Adventure");
        assertEquals(3, result.size());
        assertEquals("sci-fi", result.get(0));
        assertEquals("action", result.get(1));
        assertEquals("adventure", result.get(2));
    }

    @Test
    void testTitleChecker_ValidTitle() throws TitleException {
        String result = CodesCracker.titleChecker("The Shawshank Redemption");
        assertEquals("TSR", result);
    }

    @Test
    void testTitleChecker_ValidTitleWithNumbers() throws TitleException {
        String result = CodesCracker.titleChecker("Star Wars Episode 3");
        assertEquals("SWE3", result);
    }

    @Test
    void testTitleChecker_InvalidTitle_LowercaseFirstLetter() {
        assertThrows(TitleException.class, () -> {
            CodesCracker.titleChecker("the Godfather");
        });
    }

    @Test
    void testTitleChecker_InvalidTitle_MidWordCapital() {
        assertThrows(TitleException.class, () -> {
            CodesCracker.titleChecker("Shawshank redemption");
        });
    }


    @Test
    void testIDChecker_ValidID() throws LetterException, NumberException {
        CodesCracker.IDChecker("TSR123", "TSR", idNumbers);
        assertTrue(idNumbers.contains("123"));
    }

    @Test
    void testIDChecker_InvalidID_WrongLetters() {
        assertThrows(LetterException.class, () -> {
            CodesCracker.IDChecker("ABC123", "TSR", idNumbers);
        });
    }

    @Test
    void testIDChecker_InvalidID_DuplicateNumber() throws NumberException, LetterException {
        idNumbers.add("123");
        assertThrows(NumberException.class, () -> {
            CodesCracker.IDChecker("TSR123", "TSR", idNumbers);
        });
    }

    @Test
    void testIDChecker_InvalidID_WrongLength() {
        assertThrows(NumberException.class, () -> {
            CodesCracker.IDChecker("TSR12", "TSR", idNumbers);
        });
        assertThrows(NumberException.class, () -> {
            CodesCracker.IDChecker("TSR1234", "TSR", idNumbers);
        });
    }
}