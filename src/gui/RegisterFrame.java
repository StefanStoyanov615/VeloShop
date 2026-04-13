package gui;

import connection.Customer;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    public RegisterFrame() {
        setTitle("VeloShop - Register");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        JTextField fNameField = new JTextField();
        JTextField lNameField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        add(new JLabel(" First Name:"));
        add(fNameField);
        add(new JLabel(" Last Name:"));
        add(lNameField);
        add(new JLabel(" Username:"));
        add(userField);
        add(new JLabel(" Password:"));
        add(passField);
        add(new JLabel(" Email:"));
        add(emailField);
        add(new JLabel(" Phone:"));
        add(phoneField);
        add(registerBtn);
        add(backBtn);

        registerBtn.addActionListener(e -> {
            boolean success = Customer.register(
                    fNameField.getText(), lNameField.getText(), userField.getText(),
                    new String(passField.getPassword()), emailField.getText(), phoneField.getText()
            );
            if (success) {
                JOptionPane.showMessageDialog(this, "Registered successfully!");
                new LoginFrame().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error registering. Try different username/email.");
            }
        });

        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
    }
}