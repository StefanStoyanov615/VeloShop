package gui;

import connection.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryFrame extends JFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public HistoryFrame() {
        setTitle("VeloShop - Purchase History");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Your Past Purchases", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"Product Name", "Quantity", "Price Paid ($)", "Subtotal ($)"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        loadHistory();

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadHistory() {
        List<OrderItem> history = Order.getPurchaseHistory();
        for (OrderItem item : history) {
            double subtotal = item.getPriceAtSale() * item.getQuantity();
            tableModel.addRow(new Object[]{
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtSale(),
                    String.format("%.2f", subtotal)
            });
        }
    }
}