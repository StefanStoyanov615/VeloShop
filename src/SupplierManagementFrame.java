import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SupplierManagementFrame extends JFrame {
    private final DatabaseHelper db = new DatabaseHelper();
    private final JTable table;
    private final JTextField txtName;
    private final JTextField txtCity;
    private final JTextField txtPhone;
    private int selectedId = -1;

    public SupplierManagementFrame() {
        setTitle("Управление на Доставчици");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(UI.BACKGROUND);
        form.setPreferredSize(new Dimension(280, 0));

        form.add(UI.createLabel("Име:"));
        form.add(txtName = UI.createTextField());
        form.add(Box.createVerticalStrut(10));
        form.add(UI.createLabel("Град:"));
        form.add(txtCity = UI.createTextField());
        form.add(Box.createVerticalStrut(10));
        form.add(UI.createLabel("Телефон:"));
        form.add(txtPhone = UI.createTextField());
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

        table = new JTable();
        UI.styleTable(table);

        add(form, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) table.getValueAt(row, 0);
                    txtName.setText(table.getValueAt(row, 1).toString());
                    txtCity.setText(table.getValueAt(row, 2).toString());
                    txtPhone.setText(table.getValueAt(row, 3).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                db.executeUpdate("INSERT INTO Suppliers (SupplierName, City, Phone) VALUES ('" + txtName.getText() + "', '" + txtCity.getText() + "', '" + txtPhone.getText() + "')");
                refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnEdit.addActionListener(e -> {
            if (selectedId == -1) return;
            try {
                db.executeUpdate("UPDATE Suppliers SET SupplierName='" + txtName.getText() + "', City='" + txtCity.getText() + "', Phone='" + txtPhone.getText() + "' WHERE SupplierID=" + selectedId);
                refresh();
                JOptionPane.showMessageDialog(this, "Обновено!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnDel.addActionListener(e -> {
            if (selectedId == -1) return;
            if (JOptionPane.showConfirmDialog(this, "Сигурни ли сте?") == 0) {
                try {
                    db.executeUpdate("DELETE FROM Suppliers WHERE SupplierID=" + selectedId);
                    refresh();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Грешка при изтриване.");
                }
            }
        });

        refresh();
    }

    private void refresh() {
        try {
            table.setModel(DatabaseHelper.buildTableModel(db.executeSelect("SELECT * FROM Suppliers")));
        } catch (Exception e) {
        }
        txtName.setText("");
        txtCity.setText("");
        txtPhone.setText("");
        selectedId = -1;
    }
}