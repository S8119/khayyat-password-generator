package khayyat.passwordgenerator;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PasswordGeneratorController
{
    private final PasswordGenerator passwordGenerator;
    private final JTextField lengthField;
    private final JCheckBox uppercaseCheckBox;
    private final JCheckBox lowercaseCheckBox;
    private final JCheckBox numbersCheckBox;
    private final JCheckBox symbolsCheckBox;
    private final JTextField symbolsField;
    private final JTextField passwordField;
    private final JLabel errorLabel;

    public PasswordGeneratorController(PasswordGenerator passwordGenerator, JTextField lengthField,
                                       JCheckBox uppercaseCheckBox, JCheckBox lowercaseCheckBox,
                                       JCheckBox numbersCheckBox, JCheckBox symbolsCheckBox,
                                       JTextField symbolsField, JTextField passwordField,
                                       JLabel errorLabel)
    {
        this.passwordGenerator = passwordGenerator;
        this.lengthField = lengthField;
        this.uppercaseCheckBox = uppercaseCheckBox;
        this.lowercaseCheckBox = lowercaseCheckBox;
        this.numbersCheckBox = numbersCheckBox;
        this.symbolsCheckBox = symbolsCheckBox;
        this.symbolsField = symbolsField;
        this.passwordField = passwordField;
        this.errorLabel = errorLabel;
    }

    public void generatePassword()
    {
        errorLabel.setText(" ");
        passwordField.setText("");

        int numChars;
        try
        {
            numChars = Integer.parseInt(lengthField.getText().trim());
        } catch (NumberFormatException e)
        {
            errorLabel.setText("Enter a whole number for length.");
            return;
        }

        char[] allowedSymbols = new char[0];
        if (symbolsCheckBox.isSelected())
        {
            String symbolsText = symbolsField.getText();
            if (symbolsText == null || symbolsText.trim().isEmpty())
            {
                errorLabel.setText("Enter the symbols you want, or uncheck Symbols.");
                return;
            }
            allowedSymbols = symbolsText.toCharArray();
        }

        PasswordRequest request = new PasswordRequest(
                numChars,
                uppercaseCheckBox.isSelected(),
                lowercaseCheckBox.isSelected(),
                numbersCheckBox.isSelected(),
                allowedSymbols);

        try
        {
            PasswordResponse response = passwordGenerator.generate(request);
            passwordField.setText(response.password());
        } catch (IllegalArgumentException e)
        {
            errorLabel.setText(e.getMessage());
        }
    }
}
