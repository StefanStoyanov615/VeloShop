import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CartFrame extends JFrame {
    private final DatabaseHelper db = new DatabaseHelper();
    private final ArrayList<Integer> cart;
    private final int userId;
    private final ShopFrame parent;

    public CartFrame(ArrayList<Integer> cart, int userId, ShopFrame parent) {
        this.cart = cart;
        this.userId = userId;
        this.parent = parent;

        setTitle("Твоята Количка");
        setSize(400, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Вашата Поръчка", SwingConstants.CENTER);
        title.setFont(UI.FONT_HEADER);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        p.add(title, BorderLayout.NORTH);

        DefaultListModel<String> model = new DefaultListModel<>();
        double total = 0;
        try {
            for (int id : cart) {
                ResultSet rs = db.executeSelect("SELECT ProductName, Price FROM Products WHERE ProductID=" + id);
                if (rs.next()) {
                    model.addElement("• " + rs.getString("ProductName") + " — " + rs.getDouble("Price") + " лв.");
                    total += rs.getDouble("Price");
                }
            }
        } catch (Exception e) {
        }

        JList<String> list = new JList<>(model);
        list.setFont(UI.FONT_PLAIN);
        list.setFixedCellHeight(30);
        list.setSelectionBackground(UI.BACKGROUND);
        list.setSelectionForeground(UI.TEXT_DARK);

        p.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2, 1, 10, 10));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(20, 0, 0, 0));

        JLabel lblTotal = new JLabel("ОБЩО: " + String.format("%.2f", total) + " лв.", SwingConstants.RIGHT);
        lblTotal.setFont(UI.FONT_HEADER);
        lblTotal.setForeground(UI.ACCENT);

        JButton btnPay = UI.createButton("ПЛАЩАНЕ", UI.ACCENT);

        bottom.add(lblTotal);
        bottom.add(btnPay);
        p.add(bottom, BorderLayout.SOUTH);

        add(p);

        btnPay.addActionListener(e -> processPayment());
    }

    private void processPayment() {
        if (cart.isEmpty()) return;
        try {
            for (int id : cart) {
                db.executeUpdate("INSERT INTO Sales (ProductID, UserID, Quantity, TotalPrice) VALUES (" + id + "," + userId + ", 1, (SELECT Price FROM Products WHERE ProductID=" + id + "))");
                db.executeUpdate("UPDATE Products SET StockQuantity = StockQuantity - 1 WHERE ProductID=" + id);
            }
            JOptionPane.showMessageDialog(this, "Плащането е успешно!");
            cart.clear();
            parent.loadProducts();
            this.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}