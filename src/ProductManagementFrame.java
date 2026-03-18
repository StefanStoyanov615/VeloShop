import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

public class ProductManagementFrame extends JFrame {
    private final JTextField txtBrand;
    private final JTextField txtName;
    private final JTextField txtPrice;
    private final JTextField txtQty;
    private final JComboBox<ComboItem> comboCat;
    private final JComboBox<ComboItem> comboSup;
    private final JTable table;
    private final DatabaseHelper db = new DatabaseHelper();
    private final ShopFrame parent;
    private int selectedId = -1;

    public ProductManagementFrame(ShopFrame parent) {
        this.parent = parent;
        setTitle("Управление на Стоки");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(UI.BACKGROUND);
        form.setPreferredSize(new Dimension(300, 0));

        form.add(UI.createLabel("Марка:"));
        form.add(txtBrand = UI.createTextField());
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Име:"));
        form.add(txtName = UI.createTextField());
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Категория:"));
        comboCat = new JComboBox<>();
        comboCat.setBackground(Color.WHITE);
        form.add(comboCat);
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Доставчик:"));
        comboSup = new JComboBox<>();
        comboSup.setBackground(Color.WHITE);
        form.add(comboSup);
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Цена:"));
        form.add(txtPrice = UI.createTextField());
        form.add(Box.createVerticalStrut(5));

        form.add(UI.createLabel("Наличност:"));
        form.add(txtQty = UI.createTextField());
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

        loadDropdowns();
        loadData();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) table.getValueAt(row, 0);
                    txtBrand.setText(table.getValueAt(row, 1).toString());
                    txtName.setText(table.getValueAt(row, 2).toString());
                    txtPrice.setText(table.getValueAt(row, 3).toString());
                    txtQty.setText(table.getValueAt(row, 4).toString());
                }
            }
        });

        btnAdd.addActionListener(e -> {
            try {
                String sql = "INSERT INTO Products (Brand, ProductName, CategoryID, SupplierID, Price, StockQuantity) VALUES ('" + txtBrand.getText() + "', '" + txtName.getText() + "', " + ((ComboItem) comboCat.getSelectedItem()).getId() + ", " + ((ComboItem) comboSup.getSelectedItem()).getId() + ", " + txtPrice.getText() + ", " + txtQty.getText() + ")";
                db.executeUpdate(sql);
                loadData();
                clearForm();
                parent.loadProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Грешка: " + ex.getMessage());
            }
        });

        btnEdit.addActionListener(e -> {
            if (selectedId == -1) return;
            try {
                String sql = "UPDATE Products SET Brand='" + txtBrand.getText() + "', ProductName='" + txtName.getText() + "', CategoryID=" + ((ComboItem) comboCat.getSelectedItem()).getId() + ", SupplierID=" + ((ComboItem) comboSup.getSelectedItem()).getId() + ", Price=" + txtPrice.getText() + ", StockQuantity=" + txtQty.getText() + " WHERE ProductID=" + selectedId;
                db.executeUpdate(sql);
                loadData();
                clearForm();
                parent.loadProducts();
                JOptionPane.showMessageDialog(this, "Продуктът е обновен!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnDel.addActionListener(e -> {
            if (selectedId == -1) return;
            if (JOptionPane.showConfirmDialog(this, "Сигурни ли сте?") == 0) {
                try {
                    db.executeUpdate("DELETE FROM Products WHERE ProductID=" + selectedId);
                    loadData();
                    clearForm();
                    parent.loadProducts();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Не може да се изтрие (има продажби).");
                }
            }
        });
    }

    private void loadData() {
        try {
            ResultSet rs = db.executeSelect("SELECT ProductID, Brand, ProductName, Price, StockQuantity FROM Products");
            table.setModel(DatabaseHelper.buildTableModel(rs));
        } catch (Exception e) {
        }
    }

    private void loadDropdowns() {
        try {
            ResultSet rsCat = db.executeSelect("SELECT CategoryID, CategoryName FROM Categories");
            while (rsCat.next()) comboCat.addItem(new ComboItem(rsCat.getInt(1), rsCat.getString(2)));

            ResultSet rsSup = db.executeSelect("SELECT SupplierID, SupplierName FROM Suppliers");
            while (rsSup.next()) comboSup.addItem(new ComboItem(rsSup.getInt(1), rsSup.getString(2)));
        } catch (Exception e) {
        }
    }

    private void clearForm() {
        txtBrand.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtQty.setText("");
        selectedId = -1;
    }
}