package khayyat.passwordgenerator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Builds a password from a {@link PasswordRequest}.
 * This is the logic a Lambda handler would call after parsing the JSON body.
 */
public class PasswordGenerator
{
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    private final int minLength = 1;
    private final int maxLength = 128;

    private final Random random;

    // SecureRandom is used for actual passwords. Tests pass a seeded Random instead.
    public PasswordGenerator()
    {
        this(new SecureRandom());
    }

    public PasswordGenerator(Random random)
    {
        this.random = random;
    }

    public PasswordResponse generate(PasswordRequest request)
    {
        int numChars = request.numChars();
        validateLength(numChars);

        List<String> requiredPools = new ArrayList<>();
        StringBuilder allAllowed = new StringBuilder();

        if (request.includeUppercase())
        {
            requiredPools.add(UPPERCASE_LETTERS);
            allAllowed.append(UPPERCASE_LETTERS);
        }
        if (request.includeLowercase())
        {
            requiredPools.add(LOWERCASE_LETTERS);
            allAllowed.append(LOWERCASE_LETTERS);
        }
        if (request.includeNumbers())
        {
            requiredPools.add(DIGITS);
            allAllowed.append(DIGITS);
        }

        String symbolPool = uniqueSymbols(request.allowedSymbols());
        if (!symbolPool.isEmpty())
        {
            requiredPools.add(symbolPool);
            allAllowed.append(symbolPool);
        }

        if (requiredPools.isEmpty())
        {
            throw new IllegalArgumentException("Select at least one character type.");
        }

        if (numChars < requiredPools.size())
        {
            throw new IllegalArgumentException(
                    "Length must be at least " + requiredPools.size()
                            + " so the password can include every selected character type.");
        }

        List<Character> passwordChars = new ArrayList<>();
        // Guarantee at least one character from each selected type, then fill the rest.
        for (String pool : requiredPools)
        {
            passwordChars.add(randomCharFrom(pool));
        }
        String allowedChars = allAllowed.toString();
        while (passwordChars.size() < numChars)
        {
            passwordChars.add(randomCharFrom(allowedChars));
        }

        // Pass our Random so tests with a seeded Random stay deterministic.
        Collections.shuffle(passwordChars, random);
        return new PasswordResponse(joinChars(passwordChars));
    }

    private void validateLength(int numChars)
    {
        if (numChars < minLength || numChars > maxLength)
        {
            throw new IllegalArgumentException(
                    "Length must be a number from " + minLength + " to " + maxLength + ".");
        }
    }

    private String uniqueSymbols(char[] allowedSymbols)
    {
        if (allowedSymbols == null)
        {
            return "";
        }

        StringBuilder unique = new StringBuilder();
        for (char symbol : allowedSymbols)
        {
            if (Character.isWhitespace(symbol))
            {
                continue;
            }
            if (unique.indexOf(String.valueOf(symbol)) == -1)
            {
                unique.append(symbol);
            }
        }
        return unique.toString();
    }

    private char randomCharFrom(String pool)
    {
        return pool.charAt(random.nextInt(pool.length()));
    }

    private String joinChars(List<Character> characters)
    {
        StringBuilder password = new StringBuilder();
        for (char character : characters)
        {
            password.append(character);
        }
        return password.toString();
    }
}
