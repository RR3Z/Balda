package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlphabetTests {
    private Alphabet _alphabet;

    public AlphabetTests() {
    }

    @BeforeEach
    public void testSetup() {
        List<Character> letters = new ArrayList<>();
        letters.add('А');
        letters.add('б');
        letters.add('х');
        letters.add('Э');

        _alphabet = new Alphabet(letters);
    }

    @Test
    public void test_isLetterAvailable_Available() {
        Character letter = 'а';

        assertTrue(_alphabet.isLetterAvailable(letter));
    }

    @Test
    public void test_isLetterAvailable_NotAvailable() {
        Character letter = 'Ф';

        assertFalse(_alphabet.isLetterAvailable(letter));
    }

    @Test
    public void test_isLetterAvailable_Null() {
        assertThrows(IllegalArgumentException.class, () -> _alphabet.isLetterAvailable(null));
    }
}
