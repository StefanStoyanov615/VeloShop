package gui;

import connection.Customer;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    public LoginFrame() {
        setTitle("VeloShop - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        add(new JLabel(" Username:"));
        add(userField);
        add(new JLabel(" Password:"));
        add(passField);
        add(loginBtn);
        add(registerBtn);

        loginBtn.addActionListener(e -> {
            if (Customer.login(userField.getText(), new String(passField.getPassword()))) {
                new ShopFrame().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });

        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            this.dispose();
        });
    }
}