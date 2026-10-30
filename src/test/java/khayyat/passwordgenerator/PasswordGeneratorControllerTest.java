package khayyat.passwordgenerator;

import org.junit.jupiter.api.Test;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordGeneratorControllerTest
{
    @Test
    void generatePasswordFillsPasswordField()
    {
        // given
        JTextField lengthField = new JTextField("10");
        JCheckBox uppercaseCheckBox = new JCheckBox("", true);
        JCheckBox lowercaseCheckBox = new JCheckBox("", true);
        JCheckBox numbersCheckBox = new JCheckBox("", true);
        JCheckBox symbolsCheckBox = new JCheckBox("", true);
        JTextField symbolsField = new JTextField("!@#");
        JTextField passwordField = new JTextField();
        JLabel errorLabel = new JLabel(" ");
        PasswordGeneratorController controller = new PasswordGeneratorController(
                new PasswordGenerator(new Random(11)),
                lengthField,
                uppercaseCheckBox,
                lowercaseCheckBox,
                numbersCheckBox,
                symbolsCheckBox,
                symbolsField,
                passwordField,
                errorLabel);

        // when
        controller.generatePassword();

        // then
        assertEquals(10, passwordField.getText().length());
        assertEquals(" ", errorLabel.getText());
    }

    @Test
    void generatePasswordShowsErrorForNonNumericLength()
    {
        // given
        JTextField lengthField = new JTextField("abc");
        JCheckBox uppercaseCheckBox = new JCheckBox("", true);
        JCheckBox lowercaseCheckBox = new JCheckBox("", true);
        JCheckBox numbersCheckBox = new JCheckBox("", true);
        JCheckBox symbolsCheckBox = new JCheckBox("", true);
        JTextField symbolsField = new JTextField("!@#");
        JTextField passwordField = new JTextField("old");
        JLabel errorLabel = new JLabel(" ");
        PasswordGeneratorController controller = new PasswordGeneratorController(
                new PasswordGenerator(new Random(12)),
                lengthField,
                uppercaseCheckBox,
                lowercaseCheckBox,
                numbersCheckBox,
                symbolsCheckBox,
                symbolsField,
                passwordField,
                errorLabel);

        // when
        controller.generatePassword();

        // then
        assertEquals("Enter a whole number for length.", errorLabel.getText());
        assertEquals("", passwordField.getText());
    }

    @Test
    void generatePasswordShowsErrorWhenSymbolsCheckedButEmpty()
    {
        // given
        JTextField lengthField = new JTextField("12");
        JCheckBox uppercaseCheckBox = new JCheckBox("", true);
        JCheckBox lowercaseCheckBox = new JCheckBox("", true);
        JCheckBox numbersCheckBox = new JCheckBox("", true);
        JCheckBox symbolsCheckBox = new JCheckBox("", true);
        JTextField symbolsField = new JTextField("   ");
        JTextField passwordField = new JTextField("old");
        JLabel errorLabel = new JLabel(" ");
        PasswordGeneratorController controller = new PasswordGeneratorController(
                new PasswordGenerator(new Random(13)),
                lengthField,
                uppercaseCheckBox,
                lowercaseCheckBox,
                numbersCheckBox,
                symbolsCheckBox,
                symbolsField,
                passwordField,
                errorLabel);

        // when
        controller.generatePassword();

        // then
        assertEquals("Enter the symbols you want, or uncheck Symbols.", errorLabel.getText());
        assertEquals("", passwordField.getText());
    }
}
