package gui;

import connection.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartFrame extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JLabel totalLabel;

    public CartFrame() {
        setTitle("VeloShop - My Shopping Cart");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Product Name", "Quantity", "Unit Price ($)", "Subtotal ($)"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        table.removeColumn(table.getColumnModel().getColumn(0));

        totalLabel = new JLabel("Total Amount: $0.00 ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalLabel.setForeground(new Color(34, 139, 34));

        JButton deleteBtn = new JButton("Remove Selected Item");
        JButton checkoutBtn = new JButton("Place Order / Checkout");

        deleteBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));

        deleteBtn.setBackground(new Color(255, 204, 204));
        checkoutBtn.setBackground(new Color(204, 255, 204));

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Items in your cart:"));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.add(deleteBtn);
        buttonPanel.add(checkoutBtn);

        bottomPanel.add(buttonPanel, BorderLayout.WEST);
        bottomPanel.add(totalLabel, BorderLayout.EAST);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int orderItemId = (int) tableModel.getValueAt(selectedRow, 0);

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove this item?",
                        "Remove Item", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Order.removeFromCart(orderItemId);
                    loadCart();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            }
        });

        checkoutBtn.addActionListener(e -> {
            if (tableModel.getRowCount() > 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Confirm purchase?", "Checkout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {

                    Order.placeOrder();

                    ShopFrame.refreshTable();

                    JOptionPane.showMessageDialog(this, "Order Placed Successfully!");
                    this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Your cart is empty.");
            }
        });

        loadCart();
    }

    private void loadCart() {
        tableModel.setRowCount(0);
        double grandTotal = 0.0;

        List<OrderItem> items = Order.getCartItems();

        for (OrderItem item : items) {
            double subtotal = item.getPriceAtSale() * item.getQuantity();
            grandTotal += subtotal;

            tableModel.addRow(new Object[]{
                    item.getOrderItemId(),
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtSale(),
                    String.format("%.2f", subtotal)
            });
        }

        totalLabel.setText(String.format("Total Amount: $%.2f ", grandTotal));
    }
}