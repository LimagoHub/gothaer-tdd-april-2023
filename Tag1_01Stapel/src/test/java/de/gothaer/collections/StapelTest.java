package de.gothaer.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StapelTest {
    public static final int MAX_SIZE = 10;

    //Arrange Given empty stack
    //Act when isEmpty Called
    //Assertion then ist should return true


    private Stapel objectUnderTest;

    @BeforeEach
    void setUp() {
        objectUnderTest = new Stapel();
    }

    @Test
    @DisplayName("should return true when isEmpty called on empty Stapel")
    void isEmpty_emptyStack_returnsTrue () {
        // Arrange
      // Action
       // Assert
        assertTrue(objectUnderTest.isEmpty());
    }
    @Test

    void isEmpty_NotEmptyStack_returnsFalse () throws Exception{
        // Arrange
       objectUnderTest.push(1);
        // Action
       // Assert
        assertFalse(objectUnderTest.isEmpty());
    }
    @Test
    void push_fillUpToLimit_noExceptionIsThrown() throws Exception{
        fillUpToLimit();
    }

    @Test
    void push_overflow_throwsStapelException() throws Exception{
        fillUpToLimit();
        StapelException ex = assertThrows(StapelException.class, ()->objectUnderTest.push(1));
        assertEquals("Overflow", ex.getMessage());
    }

    private void fillUpToLimit() {
        for (int i = 0; i < MAX_SIZE; i++) {
            assertDoesNotThrow(()->objectUnderTest.push(1));
        }
    }
}