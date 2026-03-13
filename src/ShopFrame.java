import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ShopFrame extends JFrame {
    private DatabaseHelper db = new DatabaseHelper();
    private JTable table;
    private ArrayList<Integer> cart = new ArrayList<>();
    private int userId;

    public ShopFrame(int userId, String role, String name) {
        this.userId = userId;
        setTitle("VeloShop Management System");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Sidebar (Ляво меню) ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UI.PRIMARY);
        sidebar.setPreferredSize(new Dimension(240, 0));

        JLabel brand = new JLabel("VeloShop", SwingConstants.CENTER);
        brand.setFont(UI.FONT_HEADER);
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(30, 0, 30, 0));
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        sidebar.add(brand);

        // Клиентско меню
        sidebar.add(createSectionLabel("МЕНЮ КЛИЕНТ"));
        JButton btnCart = UI.createMenuButton("Добави в количка");
        JButton btnPay = UI.createMenuButton("Плащане");
        sidebar.add(btnCart);
        sidebar.add(btnPay);

        // --- АДМИНИСТРАЦИЯ (Проверка за роля) ---
        // Увери се, че в базата данни в колона Role пише точно "Admin"
        if (role.equalsIgnoreCase("Admin")) {
            sidebar.add(Box.createVerticalStrut(20));
            sidebar.add(createSectionLabel("АДМИНИСТРАЦИЯ"));

            JButton btnProd = UI.createMenuButton("Продукти");
            JButton btnCat = UI.createMenuButton("Категории");
            JButton btnSup = UI.createMenuButton("Доставчици");
            JButton btnUsers = UI.createMenuButton("Потребители");

            // Отваряне на прозорците
            btnProd.addActionListener(e -> new ProductManagementFrame(this).setVisible(true));
            btnCat.addActionListener(e -> new CategoryManagementFrame().setVisible(true));
            btnSup.addActionListener(e -> new SupplierManagementFrame().setVisible(true));
            btnUsers.addActionListener(e -> new UserManagementFrame().setVisible(true));

            sidebar.add(btnProd);
            sidebar.add(btnCat);
            sidebar.add(btnSup);
            sidebar.add(btnUsers);
        }

        sidebar.add(Box.createVerticalGlue()); // Избутва бутона за изход най-долу
        JButton btnExit = UI.createMenuButton("Изход");
        sidebar.add(btnExit);

        // --- Content (Таблица) ---
        table = new JTable();
        UI.styleTable(table);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UI.BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.add(new JScrollPane(table));

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // --- EVENTS ---

        // ПОПРАВКАТА ЗА ГРЕШКАТА (ClassCastException)
        btnCart.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                try {
                    // Взимаме стойността като String и я парсваме към int
                    String idStr = table.getValueAt(row, 0).toString();
                    int id = Integer.parseInt(idStr);

                    cart.add(id);
                    JOptionPane.showMessageDialog(this, "Продуктът е добавен в количката!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Грешка при четене на ID: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Моля, изберете продукт от таблицата!");
            }
        });

        btnPay.addActionListener(e -> new CartFrame(cart, userId, this).setVisible(true));
        btnExit.addActionListener(e -> { dispose(); new Main().setVisible(true); });

        loadProducts();
    }

    private JLabel createSectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(149, 165, 166));
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setBorder(new EmptyBorder(5, 20, 5, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        return l;
    }

    public void loadProducts() {
        try {
            ResultSet rs = db.executeSelect("SELECT ProductID, Brand, ProductName, Price, StockQuantity FROM Products");
            table.setModel(DatabaseHelper.buildTableModel(rs));
        } catch (Exception e) { e.printStackTrace(); }
    }
}