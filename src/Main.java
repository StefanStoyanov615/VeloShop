import model.Customer;
import model.Product;
import service.AuthService;
import service.ShopService;
import dao.ProductDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private AuthService authService = new AuthService();
    private ShopService shopService = new ShopService();
    private ProductDAO productDAO = new ProductDAO();
    private List<Product> displayedProducts;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public Main() {
        setTitle("VeloShop - Професионална Система");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createStorePanel(), "Store");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 52, 54));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("VELOSHOP LOGIN");
        title.setForeground(Color.WHITE); title.setFont(new Font("SansSerif", Font.BOLD, 26));
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; panel.add(title, g);

        g.gridwidth = 1; g.gridy = 1; g.gridx = 0;
        JLabel l1 = new JLabel("Потребител:"); l1.setForeground(Color.WHITE); panel.add(l1, g);
        JTextField txtUser = new JTextField(15); g.gridx = 1; panel.add(txtUser, g);

        g.gridy = 2; g.gridx = 0;
        JLabel l2 = new JLabel("Парола:"); l2.setForeground(Color.WHITE); panel.add(l2, g);
        JPasswordField txtPass = new JPasswordField(15); g.gridx = 1; panel.add(txtPass, g);

        JButton btnLogin = new JButton("ВЛИЗАНЕ");
        btnLogin.setBackground(new Color(0, 184, 148)); btnLogin.setForeground(Color.WHITE);
        g.gridy = 3; g.gridx = 0; g.gridwidth = 2; g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnLogin, g);

        JButton btnGoReg = new JButton("Нямате профил? Регистрирайте се тук");
        btnGoReg.setContentAreaFilled(false); btnGoReg.setBorderPainted(false); btnGoReg.setForeground(Color.LIGHT_GRAY);
        g.gridy = 4; panel.add(btnGoReg, g);

        btnLogin.addActionListener(e -> {
            Customer c = authService.login(txtUser.getText(), new String(txtPass.getPassword()));
            if (c != null) { refreshTable(); cardLayout.show(mainPanel, "Store"); }
            else { JOptionPane.showMessageDialog(this, "Грешни данни!"); }
        });
        btnGoReg.addActionListener(e -> cardLayout.show(mainPanel, "Register"));
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(5, 5, 5, 5);

        JTextField fName = new JTextField(15); JTextField lName = new JTextField(15);
        JTextField uName = new JTextField(15); JPasswordField pass = new JPasswordField(15);
        JTextField email = new JTextField(15);

        panel.add(new JLabel("Име:"), g); g.gridx = 1; panel.add(fName, g);
        g.gridx = 0; g.gridy = 1; panel.add(new JLabel("Фамилия:"), g); g.gridx = 1; panel.add(lName, g);
        g.gridx = 0; g.gridy = 2; panel.add(new JLabel("Потребител:"), g); g.gridx = 1; panel.add(uName, g);
        g.gridx = 0; g.gridy = 3; panel.add(new JLabel("Парола:"), g); g.gridx = 1; panel.add(pass, g);
        g.gridx = 0; g.gridy = 4; panel.add(new JLabel("Имейл:"), g); g.gridx = 1; panel.add(email, g);

        JButton btnReg = new JButton("СЪЗДАЙ ПРОФИЛ"); g.gridy = 5; g.gridx = 0; g.gridwidth = 2; panel.add(btnReg, g);
        JButton btnBack = new JButton("Назад"); g.gridy = 6; panel.add(btnBack, g);

        btnReg.addActionListener(e -> {
            if(authService.register(fName.getText(), lName.getText(), uName.getText(), new String(pass.getPassword()), email.getText()))
                cardLayout.show(mainPanel, "Login");
        });
        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
        return panel;
    }

    private JTable table;
    private DefaultTableModel tableModel;

    private JPanel createStorePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(9, 132, 227)); header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcome = new JLabel("Магазин VeloShop"); welcome.setForeground(Color.WHITE); welcome.setFont(new Font("Arial", Font.BOLD, 18));
        JButton btnLogout = new JButton("Изход"); header.add(welcome, BorderLayout.WEST); header.add(btnLogout, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        String[] cols = {"Продукт", "Марка", "Доставчик", "Спецификации", "Цена (лв.)", "Наличност"};
        tableModel = new DefaultTableModel(cols, 0); table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JSpinner spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAdd = new JButton("Добави в количката");
        JButton btnCheckout = new JButton("ПЛАЩАНЕ");
        btnCheckout.setBackground(new Color(225, 112, 85)); btnCheckout.setForeground(Color.WHITE);

        footer.add(new JLabel("Брой:")); footer.add(spinQty); footer.add(btnAdd); footer.add(btnCheckout);
        panel.add(footer, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = displayedProducts.get(row).getId();
                shopService.addToCart(id, (int) spinQty.getValue());
                JOptionPane.showMessageDialog(this, "Добавено в количката!");
            } else { JOptionPane.showMessageDialog(this, "Изберете продукт!"); }
        });

        btnCheckout.addActionListener(e -> {
            shopService.checkout(authService.getLoggedInCustomer());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Поръчката е завършена успешно!");
        });

        btnLogout.addActionListener(e -> { authService.logout(); cardLayout.show(mainPanel, "Login"); });
        return panel;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        displayedProducts = productDAO.getAllProducts();
        for (Product p : displayedProducts) {
            tableModel.addRow(new Object[]{p.getName(), p.getBrandName(), p.getSupplierName(), p.getSpecifications(), p.getPrice(), p.getStockQuantity()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}