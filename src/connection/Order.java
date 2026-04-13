package connection;

import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private static int getOrCreatePendingOrder() {
        int orderId = -1;
        String checkSql = "SELECT order_id FROM orders WHERE customer_id = ? AND status = 'Pending'";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setInt(1, Database.currentCustomerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                orderId = rs.getInt("order_id");
            } else {
                String insertSql = "INSERT INTO orders (customer_id, status, total_amount) VALUES (?, 'Pending', 0.00)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setInt(1, Database.currentCustomerId);
                    insertStmt.executeUpdate();
                    ResultSet keys = insertStmt.getGeneratedKeys();
                    if (keys.next()) orderId = keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public static boolean addToCart(int productId, double price) {
        String stockSql = "SELECT stock_quantity FROM products WHERE product_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stockStmt = conn.prepareStatement(stockSql)) {
            stockStmt.setInt(1, productId);
            ResultSet rs = stockStmt.executeQuery();
            if (rs.next()) {
                int currentStock = rs.getInt("stock_quantity");
                if (currentStock <= 0) return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        int orderId = getOrCreatePendingOrder();
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_sale) VALUES (?, ?, 1, ?)";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
            updateOrderTotal(orderId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void placeOrder() {
        int orderId = getOrCreatePendingOrder();
        if (orderId == -1) return;

        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            String updateStockSql = "UPDATE products p " +
                    "JOIN order_items oi ON p.product_id = oi.product_id " +
                    "SET p.stock_quantity = p.stock_quantity - oi.quantity " +
                    "WHERE oi.order_id = ?";
            try (PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {
                stockStmt.setInt(1, orderId);
                stockStmt.executeUpdate();
            }

            String completeOrderSql = "UPDATE orders SET status = 'Paid' WHERE order_id = ?";
            try (PreparedStatement orderStmt = conn.prepareStatement(completeOrderSql)) {
                orderStmt.setInt(1, orderId);
                orderStmt.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<OrderItem> getCartItems() {
        List<OrderItem> items = new ArrayList<>();
        int orderId = getOrCreatePendingOrder();
        String sql = "SELECT oi.order_item_id, oi.quantity, oi.price_at_sale, " +
                "p.product_id, p.name, p.price, p.specifications, p.stock_quantity, " +
                "c.name as cat_name, b.name as brand_name, s.name as supplier_name " +
                "FROM order_items oi " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id " +
                "WHERE oi.order_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                model.Product p = new model.Product(
                        rs.getInt("product_id"), rs.getString("name"), rs.getString("cat_name"),
                        rs.getString("brand_name"), rs.getString("supplier_name"),
                        rs.getDouble("price"), rs.getString("specifications"), rs.getInt("stock_quantity")
                );
                items.add(new OrderItem(rs.getInt("order_item_id"), p, rs.getInt("quantity"), rs.getDouble("price_at_sale")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public static void removeFromCart(int orderItemId) {
        int orderId = getOrCreatePendingOrder();
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            stmt.executeUpdate();
            updateOrderTotal(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateOrderTotal(int orderId) {
        String sql = "UPDATE orders SET total_amount = COALESCE((SELECT SUM(price_at_sale * quantity) FROM order_items WHERE order_id = ?), 0.00) WHERE order_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<OrderItem> getPurchaseHistory() {
        List<OrderItem> history = new ArrayList<>();
        String sql = "SELECT oi.order_item_id, oi.quantity, oi.price_at_sale, " +
                "p.product_id, p.name, o.order_date " +
                "FROM order_items oi " +
                "JOIN orders o ON oi.order_id = o.order_id " +
                "JOIN products p ON oi.product_id = p.product_id " +
                "WHERE o.customer_id = ? AND o.status = 'Paid' " +
                "ORDER BY o.order_date DESC";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Database.currentCustomerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                model.Product p = new model.Product(
                        rs.getInt("product_id"), rs.getString("name"),
                        "", "", "", 0.0, "", 0
                );

                history.add(new OrderItem(rs.getInt("order_item_id"), p, rs.getInt("quantity"), rs.getDouble("price_at_sale")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }
}