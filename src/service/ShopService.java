package service;

import dao.OrderDAO;
import dao.ProductDAO;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.Product;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private ProductDAO productDAO = new ProductDAO();
    private OrderDAO orderDAO = new OrderDAO();

    
    private List<OrderItem> cart = new ArrayList<>();

    
    public void printAvailableProducts() {
        List<Product> products = productDAO.getAllProducts();
        System.out.println("\n--- СПИСЪК С ПРОДУКТИ ---");
        for (Product p : products) {
            System.out.println("ID: " + p.getId() + " | " + p.getName() + " | Цена: " + p.getPrice() + " лв. | Наличност: " + p.getStockQuantity());
        }
    }

    
    public void addToCart(int productId, int quantity) {
        
        List<Product> allProducts = productDAO.getAllProducts();
        Product selectedProduct = null;

        for (Product p : allProducts) {
            if (p.getId() == productId) {
                selectedProduct = p;
                break;
            }
        }

        if (selectedProduct == null || selectedProduct.getStockQuantity() < quantity) {
            System.out.println("Грешка: Продуктът не съществува или няма достатъчна наличност!");
            return;
        }

        
        OrderItem item = new OrderItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPriceAtSale(selectedProduct.getPrice());

        cart.add(item);
        System.out.println("Добавено в количката: " + selectedProduct.getName() + " (" + quantity + " бр.)");
    }

    
    public void checkout(Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Количката е празна!");
            return;
        }

        
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : cart) {
            BigDecimal itemTotal = item.getPriceAtSale().multiply(new BigDecimal(item.getQuantity()));
            total = total.add(itemTotal);
        }

        
        Order newOrder = new Order();
        newOrder.setCustomerId(customer.getId());
        newOrder.setTotalAmount(total);

        
        boolean success = orderDAO.createOrder(newOrder, cart);

        if (success) {
            
            try {
                for (OrderItem item : cart) {
                    productDAO.updateStock(item.getProductId(), item.getQuantity());
                }
                System.out.println("Поръчката е завършена успешно! Обща сума: " + total + " лв.");
                cart.clear(); 
            } catch (SQLException e) {
                System.out.println("Грешка при обновяване на наличността.");
            }
        } else {
            System.out.println("Възникна грешка при обработка на поръчката.");
        }
    }
}