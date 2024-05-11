package model;

import model.utils.DataFilePaths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlphabetTests {
    private Alphabet _alphabet;

    public AlphabetTests() {
    }

    @BeforeEach
    public void testSetup() {
        _alphabet = new Alphabet(DataFilePaths.ALPHABET_FILE_PATH);
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
