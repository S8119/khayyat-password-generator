package khayyat.passwordgenerator;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PasswordGeneratorFrame extends JFrame
{
    public PasswordGeneratorFrame()
    {
        setSize(520, 360);
        setTitle("Password Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        final JLabel titleLabel = new JLabel("Password Generator");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 22f));

        final JLabel lengthLabel = new JLabel("Length");
        final JTextField lengthField = new JTextField("20", 6);

        final JCheckBox uppercaseCheckBox = new JCheckBox("Uppercase (A-Z)", true);
        final JCheckBox lowercaseCheckBox = new JCheckBox("Lowercase (a-z)", true);
        final JCheckBox numbersCheckBox = new JCheckBox("Numbers (0-9)", true);
        final JCheckBox symbolsCheckBox = new JCheckBox("Symbols", true);

        final JTextField symbolsField = new JTextField("!@#$%^&", 12);
        symbolsField.setToolTipText("Characters that may be used as symbols");

        final JButton generateButton = new JButton("Generate password");

        final JLabel passwordLabel = new JLabel("Password");
        final JTextField passwordField = new JTextField(24);
        passwordField.setEditable(false);
        passwordField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));

        final JLabel errorLabel = new JLabel(" ");
        errorLabel.setForeground(new Color(180, 0, 0));

        final PasswordGenerator passwordGenerator = new PasswordGenerator();
        final PasswordGeneratorController controller = new PasswordGeneratorController(
                passwordGenerator, lengthField, uppercaseCheckBox, lowercaseCheckBox,
                numbersCheckBox, symbolsCheckBox, symbolsField, passwordField, errorLabel);

        symbolsCheckBox.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                boolean includeSymbols = symbolsCheckBox.isSelected();
                symbolsField.setEnabled(includeSymbols);
            }
        });

        generateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                controller.generatePassword();
            }
        });

        addComponent(titleLabel, 0, 0, 2);
        addComponent(lengthLabel, 0, 1, 1);
        addComponent(lengthField, 1, 1, 1);
        addComponent(uppercaseCheckBox, 0, 2, 2);
        addComponent(lowercaseCheckBox, 0, 3, 2);
        addComponent(numbersCheckBox, 0, 4, 2);
        addComponent(symbolsCheckBox, 0, 5, 1);
        addComponent(symbolsField, 1, 5, 1);
        addComponent(generateButton, 0, 6, 2);
        addComponent(passwordLabel, 0, 7, 1);
        addComponent(passwordField, 1, 7, 1);
        addComponent(errorLabel, 0, 8, 2);
    }

    private void addComponent(JComponent component, int x, int y, int width)
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(6, 8, 6, 8);
        add(component, constraints);
    }

    public static void main(String[] args)
    {
        PasswordGeneratorFrame frame = new PasswordGeneratorFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
