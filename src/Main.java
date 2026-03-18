import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;

public class Main extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel;
    private final DatabaseHelper db = new DatabaseHelper();

    private JTextField txtLoginUser;
    private JPasswordField txtLoginPass;

    private JTextField txtRegUser, txtRegName, txtRegEmail;
    private JPasswordField txtRegPass;

    public Main() {
        setTitle("VeloShop - Вход в системата");
        setSize(450, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        mainPanel = new JPanel(cardLayout);
        mainPanel.add(createLoginPanel(), "LOGIN");
        mainPanel.add(createRegisterPanel(), "REGISTER");

        add(mainPanel);
    }

    private JPanel createLoginPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UI.PRIMARY);
        p.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("VeloShop", SwingConstants.CENTER);
        title.setFont(UI.FONT_HEADER);
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(400, 200));

        txtLoginUser = UI.createTextField();
        txtLoginPass = new JPasswordField();
        txtLoginPass.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        form.add(createWhiteLabel("Потребител:"));
        form.add(txtLoginUser);
        form.add(createWhiteLabel("Парола:"));
        form.add(txtLoginPass);

        JButton btnLogin = UI.createButton("ВХОД", UI.ACCENT);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnGoToReg = new JButton("Нямаш акаунт? Регистрирай се тук.");
        styleLinkButton(btnGoToReg);

        p.add(title);
        p.add(Box.createVerticalStrut(30));
        p.add(form);
        p.add(Box.createVerticalStrut(20));
        p.add(btnLogin);
        p.add(Box.createVerticalStrut(10));
        p.add(btnGoToReg);

        btnLogin.addActionListener(e -> handleLogin());
        btnGoToReg.addActionListener(e -> {
            setTitle("VeloShop - Регистрация");
            cardLayout.show(mainPanel, "REGISTER");
        });

        return p;
    }

    private JPanel createRegisterPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(UI.BACKGROUND);
        p.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("Създай Акаунт", SwingConstants.CENTER);
        title.setFont(UI.FONT_HEADER);
        title.setForeground(UI.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel form = new JPanel(new GridLayout(8, 1, 5, 5));
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(400, 350));

        txtRegUser = UI.createTextField();
        txtRegPass = new JPasswordField();
        txtRegPass.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtRegName = UI.createTextField();
        txtRegEmail = UI.createTextField();

        form.add(UI.createLabel("Потребителско име:"));
        form.add(txtRegUser);
        form.add(UI.createLabel("Парола:"));
        form.add(txtRegPass);
        form.add(UI.createLabel("Име и Фамилия:"));
        form.add(txtRegName);
        form.add(UI.createLabel("Имейл:"));
        form.add(txtRegEmail);

        JButton btnReg = UI.createButton("РЕГИСТРАЦИЯ", UI.PRIMARY);
        btnReg.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnBack = new JButton("Вече имаш акаунт? Вход.");
        styleLinkButton(btnBack);
        btnBack.setForeground(UI.PRIMARY);

        p.add(title);
        p.add(Box.createVerticalStrut(20));
        p.add(form);
        p.add(Box.createVerticalStrut(20));
        p.add(btnReg);
        p.add(Box.createVerticalStrut(10));
        p.add(btnBack);

        btnReg.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            setTitle("VeloShop - Вход");
            cardLayout.show(mainPanel, "LOGIN");
        });

        return p;
    }

    private void handleLogin() {
        try {
            String sql = "SELECT * FROM Users WHERE Username='" + txtLoginUser.getText() + "' AND Password='" + new String(txtLoginPass.getPassword()) + "'";
            ResultSet rs = db.executeSelect(sql);
            if (rs.next()) {
                new ShopFrame(rs.getInt("UserID"), rs.getString("Role"), rs.getString("FullName")).setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Грешни данни!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleRegister() {
        if (txtRegUser.getText().isEmpty() || new String(txtRegPass.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(this, "Попълни всички полета!");
            return;
        }
        try {
            String sql = "INSERT INTO Users (Username, Password, FullName, Email, Role) VALUES ('" + txtRegUser.getText() + "', '" + new String(txtRegPass.getPassword()) + "', '" + txtRegName.getText() + "', '" + txtRegEmail.getText() + "', 'User')";
            db.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Успешна регистрация! Сега можеш да влезеш.");
            cardLayout.show(mainPanel, "LOGIN");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Потребителското име е заето!");
        }
    }

    private JLabel createWhiteLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UI.FONT_BOLD);
        l.setForeground(Color.WHITE);
        return l;
    }

    private void styleLinkButton(JButton b) {
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setForeground(Color.WHITE);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}