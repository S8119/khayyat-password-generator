package khayyat.passwordgenerator;

/**
 * JSON body returned by the password generator (and later by the Lambda).
 */
public record PasswordResponse(String password)
{
}
