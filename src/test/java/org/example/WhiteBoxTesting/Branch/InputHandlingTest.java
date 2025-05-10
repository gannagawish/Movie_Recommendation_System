package test.java.org.example.WhiteBoxTesting.Branch;

import main.java.org.example.InputHandling;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlingTest {

   InputHandling inputHandling;

   @BeforeEach
   void setUp() {
      inputHandling = new InputHandling();
   }

   @Test
   void testUserLineNull() {
      assertNull(inputHandling.parseInput(null, "m1,m2,m3"));
   }

   @Test
   void testUserLineEmpty() {
      assertNull(inputHandling.parseInput("", "m1,m2,m3"));
   }

   @Test
   void testUserLineInvalidFormat() {
      assertNull(inputHandling.parseInput("username", "m1,m2,m3"));
   }

   @Test
   void testUserLineNameEmpty() {
      assertNull(inputHandling.parseInput(" ,123", "m1,m2,m3"));
   }

   @Test
   void testMovieLineNull() {
      assertNull(inputHandling.parseInput("Alice,123", null));
   }

   @Test
   void testMovieLineEmpty() {
      assertNull(inputHandling.parseInput("Alice,123", ""));
   }

   @Test
   void testValidMovieIDsWithEmpty() {
      Object[] result = inputHandling.parseInput("Alice,123", "m1, ,m2");
      assertNotNull(result);
      assertEquals(2, ((java.util.List) result[2]).size());  // Ensures "m1, m2" are added, empty one is skipped
   }

   @Test
   void testNoValidMovieIDs() {
      assertNull(inputHandling.parseInput("Alice,123", "  ,  "));
   }

   @Test
   void testValidInput() {
      Object[] result = inputHandling.parseInput("Alice,123", "m1,m2,m3");
      assertNotNull(result);
      assertEquals("Alice", result[0]);
      assertEquals("123", result[1]);
      assertEquals(3, ((java.util.List) result[2]).size());
   }
}
