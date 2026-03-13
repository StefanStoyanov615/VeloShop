import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CategoryManagementFrame extends JFrame {
    private DatabaseHelper db = new DatabaseHelper();
    private JTable table;
    private JTextField txtName, txtDesc;
    private int selectedId = -1;

    public CategoryManagementFrame() {
        setTitle("Управление на Категории");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(UI.BACKGROUND);
        form.setPreferredSize(new Dimension(280, 0));

        form.add(UI.createLabel("Име:")); form.add(txtName = UI.createTextField());
        form.add(Box.createVerticalStrut(10));
        form.add(UI.createLabel("Описание:")); form.add(txtDesc = UI.createTextField());
        form.add(Box.createVerticalStrut(20));

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        btnPanel.setOpaque(false);
        JButton btnAdd = UI.createButton("ДОБАВИ", UI.ACCENT);
        JButton btnEdit = UI.createButton("РЕДАКТИРАЙ", UI.EDIT);
        JButton btnDel = UI.createButton("ИЗТРИЙ", UI.WARNING);
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDel);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        form.add(btnPanel);

        table = new JTable();
        UI.styleTable(table);

        add(form, BorderLayout.WEST);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    selectedId = (int) table.getValueAt(row, 0);
                    txtName.setText(table.getValueAt(row, 1).toString());
                    txtDesc.setText(table.getValueAt(row, 2).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                db.executeUpdate("INSERT INTO Categories (CategoryName, Description) VALUES ('" + txtName.getText() + "', '" + txtDesc.getText() + "')");
                refresh();
            } catch(Exception ex) {}
        });

        btnEdit.addActionListener(e -> {
            if(selectedId == -1) return;
            try {
                db.executeUpdate("UPDATE Categories SET CategoryName='" + txtName.getText() + "', Description='" + txtDesc.getText() + "' WHERE CategoryID=" + selectedId);
                refresh();
            } catch(Exception ex) {}
        });

        btnDel.addActionListener(e -> {
            if(selectedId == -1) return;
            if(JOptionPane.showConfirmDialog(this, "Сигурни ли сте?") == 0) {
                try {
                    db.executeUpdate("DELETE FROM Categories WHERE CategoryID=" + selectedId);
                    refresh();
                } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Категорията се използва и не може да бъде изтрита."); }
            }
        });

        refresh();
    }

    private void refresh() {
        try { table.setModel(DatabaseHelper.buildTableModel(db.executeSelect("SELECT * FROM Categories"))); } catch(Exception e) {}
        txtName.setText(""); txtDesc.setText(""); selectedId = -1;
    }
}