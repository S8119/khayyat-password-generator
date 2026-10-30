package khayyat.passwordgenerator;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordGeneratorTest
{
    @Test
    void generateUsesRequestedLength()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(1));
        PasswordRequest request = new PasswordRequest(
                16, true, true, true, "!@#".toCharArray());

        // when
        PasswordResponse response = generator.generate(request);

        // then
        assertEquals(16, response.password().length());
    }

    @Test
    void generateIncludesAtLeastOneOfEachSelectedType()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(2));
        char[] symbols = "!@#$".toCharArray();
        PasswordRequest request = new PasswordRequest(12, true, true, true, symbols);

        // when
        String password = generator.generate(request).password();

        // then
        assertTrue(containsAny(password, "ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertTrue(containsAny(password, "abcdefghijklmnopqrstuvwxyz"));
        assertTrue(containsAny(password, "0123456789"));
        assertTrue(containsAny(password, "!@#$"));
    }

    @Test
    void generateOmitsUnselectedTypes()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(3));
        PasswordRequest request = new PasswordRequest(
                20, false, true, false, new char[0]);

        // when
        String password = generator.generate(request).password();

        // then
        assertEquals(20, password.length());
        assertTrue(password.matches("[a-z]+"));
    }

    @Test
    void generateUsesOnlyAllowedSymbols()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(4));
        PasswordRequest request = new PasswordRequest(
                10, false, false, false, "*+-".toCharArray());

        // when
        String password = generator.generate(request).password();

        // then
        assertTrue(password.matches("[*+-]+"));
    }

    @Test
    void generateIsDeterministicWithSeededRandom()
    {
        // given
        PasswordRequest request = new PasswordRequest(
                8, true, true, true, "!".toCharArray());

        // when
        String first = new PasswordGenerator(new Random(99)).generate(request).password();
        String second = new PasswordGenerator(new Random(99)).generate(request).password();

        // then
        assertEquals(first, second);
    }

    @Test
    void generateThrowsWhenNoCharacterTypesAreSelected()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(5));
        PasswordRequest request = new PasswordRequest(
                8, false, false, false, new char[0]);

        // when / then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(request));
        assertEquals("Select at least one character type.", exception.getMessage());
    }

    @Test
    void generateThrowsWhenLengthIsTooShortForSelectedTypes()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(6));
        PasswordRequest request = new PasswordRequest(
                2, true, true, true, new char[0]);

        // when / then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(request));
        assertTrue(exception.getMessage().contains("Length must be at least 3"));
    }

    @Test
    void generateThrowsWhenLengthIsOutOfRange()
    {
        // given
        PasswordGenerator generator = new PasswordGenerator(new Random(7));
        PasswordRequest request = new PasswordRequest(
                0, true, false, false, new char[0]);

        // when / then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(request));
        assertEquals("Length must be a number from 1 to 128.", exception.getMessage());
    }

    private boolean containsAny(String password, String characters)
    {
        for (int i = 0; i < characters.length(); i++)
        {
            if (password.indexOf(characters.charAt(i)) >= 0)
            {
                return true;
            }
        }
        return false;
    }
}
