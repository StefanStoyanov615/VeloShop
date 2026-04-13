package gui;

import connection.Order;
import connection.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShopFrame extends JFrame {
    private static ShopFrame instance;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ShopFrame() {
        instance = this;
        setTitle("VeloShop - Full Product Catalog");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = {
                "ID", "Product Name", "Category", "Brand", "Supplier", "Price ($)", "Specs", "Stock"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);

        loadProducts();

        JPanel bottomPanel = new JPanel();
        JButton addCartBtn = new JButton("Add Selected to Cart");
        JButton viewCartBtn = new JButton("Open My Cart");
        JButton historyBtn = new JButton("Order History");
        JButton logoutBtn = new JButton("Logout");

        bottomPanel.add(addCartBtn);
        bottomPanel.add(viewCartBtn);
        bottomPanel.add(historyBtn);
        bottomPanel.add(logoutBtn);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        addCartBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int productId = (int) tableModel.getValueAt(row, 0);
                double price = (double) tableModel.getValueAt(row, 5);
                Order.addToCart(productId, price);
                JOptionPane.showMessageDialog(this, "Added to cart!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row first.");
            }
        });

        viewCartBtn.addActionListener(e -> new CartFrame().setVisible(true));

        historyBtn.addActionListener(e -> {
            new HistoryFrame().setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            connection.Database.currentCustomerId = -1;
            new LoginFrame().setVisible(true);
            this.dispose();
        });
    }

    public static void refreshTable() {
        if (instance != null) {
            instance.loadProducts();
        }
    }

    public void loadProducts() {
        tableModel.setRowCount(0);
        List<model.Product> products = Product.getAllProducts();
        for (model.Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    p.getBrand(),
                    p.getSupplier(),
                    p.getPrice(),
                    p.getSpecifications(),
                    p.getStockQuantity()
            });
        }
    }
}