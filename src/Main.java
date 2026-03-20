import dao.ProductDAO;
import model.Customer;
import model.Product;
import service.AuthService;
import service.ShopService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private final AuthService authService = new AuthService();
    private final ShopService shopService = new ShopService();
    private final ProductDAO productDAO = new ProductDAO();
    private List<Product> displayedProducts;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel(cardLayout);

    public Main() {
        setTitle("VeloShop System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createRegisterPanel(), "Register");
        mainPanel.add(createStorePanel(), "Store");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));

        JTextField txtUser = new JTextField();
        JPasswordField txtPass = new JPasswordField();
        JButton btnLogin = new JButton("Влез");
        JButton btnToReg = new JButton("Към Регистрация");

        panel.add(new JLabel("Потребител:"));
        panel.add(txtUser);
        panel.add(new JLabel("Парола:"));
        panel.add(txtPass);
        panel.add(btnLogin);
        panel.add(btnToReg);

        btnLogin.addActionListener(e -> {
            Customer c = authService.login(txtUser.getText(), new String(txtPass.getPassword()));
            if (c != null) {
                refreshTable();
                cardLayout.show(mainPanel, "Store");
            } else {
                JOptionPane.showMessageDialog(this, "Грешни данни!");
            }
        });

        btnToReg.addActionListener(e -> cardLayout.show(mainPanel, "Register"));
        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));

        JTextField fName = new JTextField();
        JTextField lName = new JTextField();
        JTextField uName = new JTextField();
        JPasswordField pass = new JPasswordField();
        JTextField email = new JTextField();
        JTextField phone = new JTextField();
        JTextField address = new JTextField();

        JButton btnReg = new JButton("Регистрирай ме");
        JButton btnBack = new JButton("Назад");

        panel.add(new JLabel("Име:"));
        panel.add(fName);
        panel.add(new JLabel("Фамилия:"));
        panel.add(lName);
        panel.add(new JLabel("Потребител:"));
        panel.add(uName);
        panel.add(new JLabel("Парола:"));
        panel.add(pass);
        panel.add(new JLabel("Имейл:"));
        panel.add(email);
        panel.add(new JLabel("Телефон:"));
        panel.add(phone);
        panel.add(new JLabel("Адрес:"));
        panel.add(address);
        panel.add(btnReg);
        panel.add(btnBack);

        btnReg.addActionListener(e -> {
            boolean success = authService.register(fName.getText(), lName.getText(), uName.getText(), new String(pass.getPassword()), email.getText(), phone.getText(), address.getText());

            if (success) {
                JOptionPane.showMessageDialog(this, "Успешна регистрация!");
                cardLayout.show(mainPanel, "Login");
            } else {
                JOptionPane.showMessageDialog(this, "Грешка при регистрация!");
            }
        });

        btnBack.addActionListener(e -> cardLayout.show(mainPanel, "Login"));
        return panel;
    }

    private JTable table;
    private DefaultTableModel tableModel;

    private JPanel createStorePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDel = new JButton("Изтрий профил");
        JButton btnLogout = new JButton("Изход");
        top.add(btnDel);
        top.add(btnLogout);
        panel.add(top, BorderLayout.NORTH);

        String[] cols = {"Продукт", "Марка", "Доставчик", "Спецификации", "Цена", "Наличност"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout());
        JSpinner spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        JButton btnAdd = new JButton("Добави в количка");
        JButton btnBuy = new JButton("Купи всичко");
        bottom.add(new JLabel("Количество:"));
        bottom.add(spinQty);
        bottom.add(btnAdd);
        bottom.add(btnBuy);
        panel.add(bottom, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = displayedProducts.get(row).getId();
                shopService.addToCart(id, (int) spinQty.getValue());
                JOptionPane.showMessageDialog(this, "Добавено!");
            }
        });

        btnBuy.addActionListener(e -> {
            shopService.checkout(authService.getLoggedInCustomer());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Поръчката е завършена!");
        });

        btnLogout.addActionListener(e -> {
            authService.logout();
            cardLayout.show(mainPanel, "Login");
        });

        btnDel.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Изтриване на акаунта?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (authService.deleteAccount()) {
                    cardLayout.show(mainPanel, "Login");
                }
            }
        });

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