package khayyat.passwordgenerator;

/**
 * JSON body sent to the password generator (and later to the Lambda).
 * Gson maps this to and from JSON using the record component names as keys.
 */
public record PasswordRequest(
        int numChars,
        boolean includeUppercase,
        boolean includeLowercase,
        boolean includeNumbers,
        char[] allowedSymbols
)
{
}
