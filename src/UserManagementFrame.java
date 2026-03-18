import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

public class UserManagementFrame extends JFrame {
    private final DatabaseHelper db = new DatabaseHelper();
    private final JTable userTable;
    private final JTextField txtUsername;
    private final JTextField txtFullName;
    private final JTextField txtEmail;
    private final JPasswordField txtPassword;
    private final JComboBox<String> comboRole;
    private int selectedId = -1;

    public UserManagementFrame() {
        setTitle("Управление на Потребители");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(UI.BACKGROUND);
        form.setPreferredSize(new Dimension(300, 0));

        form.add(UI.createLabel("Потребителско име:"));
        form.add(txtUsername = UI.createTextField());
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Парола:"));
        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)), new EmptyBorder(5, 10, 5, 10)));
        form.add(txtPassword);
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Име и Фамилия:"));
        form.add(txtFullName = UI.createTextField());
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Имейл:"));
        form.add(txtEmail = UI.createTextField());
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Роля:"));
        comboRole = new JComboBox<>(new String[]{"User", "Admin"});
        comboRole.setBackground(Color.WHITE);
        form.add(comboRole);
        form.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        btnPanel.setOpaque(false);

        JButton btnAdd = UI.createButton("ДОБАВИ", UI.ACCENT);
        JButton btnEdit = UI.createButton("РЕДАКТИРАЙ", UI.EDIT);
        JButton btnDel = UI.createButton("ИЗТРИЙ", UI.WARNING);

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDel);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        form.add(btnPanel);

        userTable = new JTable();
        UI.styleTable(userTable);

        add(form, BorderLayout.WEST);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = userTable.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) userTable.getValueAt(row, 0);
                    txtUsername.setText(userTable.getValueAt(row, 1).toString());
                    txtFullName.setText(userTable.getValueAt(row, 2).toString());
                    txtEmail.setText(userTable.getValueAt(row, 3).toString());

                    String role = userTable.getValueAt(row, 4).toString();
                    comboRole.setSelectedItem(role);

                    txtPassword.setText("");
                }
            }
        });

        btnAdd.addActionListener(e -> {
            String pass = new String(txtPassword.getPassword());
            if (txtUsername.getText().isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Потребителско име и парола са задължителни!");
                return;
            }
            try {
                String sql = "INSERT INTO Users (Username, Password, FullName, Email, Role) VALUES ('" + txtUsername.getText() + "', '" + pass + "', '" + txtFullName.getText() + "', '" + txtEmail.getText() + "', '" + comboRole.getSelectedItem() + "')";
                db.executeUpdate(sql);
                loadUsers();
                clearForm();
                JOptionPane.showMessageDialog(this, "Потребителят е добавен!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Грешка: Потребителското име е заето.");
            }
        });

        btnEdit.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Моля, изберете потребител от таблицата!");
                return;
            }
            try {
                String pass = new String(txtPassword.getPassword());
                String sql;

                if (pass.isEmpty()) {
                    sql = "UPDATE Users SET Username='" + txtUsername.getText() + "', " + "FullName='" + txtFullName.getText() + "', " + "Email='" + txtEmail.getText() + "', " + "Role='" + comboRole.getSelectedItem() + "' " + "WHERE UserID=" + selectedId;
                } else {
                    sql = "UPDATE Users SET Username='" + txtUsername.getText() + "', " + "Password='" + pass + "', " + "FullName='" + txtFullName.getText() + "', " + "Email='" + txtEmail.getText() + "', " + "Role='" + comboRole.getSelectedItem() + "' " + "WHERE UserID=" + selectedId;
                }

                db.executeUpdate(sql);
                loadUsers();
                clearForm();
                JOptionPane.showMessageDialog(this, "Данните са обновени!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnDel.addActionListener(e -> {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this, "Моля, изберете потребител от таблицата!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Сигурни ли сте, че искате да изтриете този потребител?", "Потвърждение", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    db.executeUpdate("DELETE FROM Users WHERE UserID=" + selectedId);
                    loadUsers();
                    clearForm();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Не може да се изтрие (потребителят има продажби).");
                }
            }
        });

        loadUsers();
    }

    private void loadUsers() {
        try {
            String sql = "SELECT UserID, Username, FullName, Email, Role FROM Users";
            ResultSet rs = db.executeSelect(sql);
            userTable.setModel(DatabaseHelper.buildTableModel(rs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtFullName.setText("");
        txtEmail.setText("");
        comboRole.setSelectedIndex(0);
        selectedId = -1;
    }
}