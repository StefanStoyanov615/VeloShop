package dao;

import model.Order;
import model.OrderItem;

import java.sql.*;
import java.util.List;

public class OrderDAO {

    public boolean createOrder(Order order, List<OrderItem> items) {
        String orderSql = "INSERT INTO orders (customer_id, total_amount, status) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price_at_sale) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);


            PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getCustomerId());
            psOrder.setBigDecimal(2, order.getTotalAmount());
            psOrder.setString(3, "Paid");
            psOrder.executeUpdate();


            ResultSet rs = psOrder.getGeneratedKeys();
            if (rs.next()) {
                int orderId = rs.getInt(1);


                PreparedStatement psItem = conn.prepareStatement(itemSql);
                for (OrderItem item : items) {
                    psItem.setInt(1, orderId);
                    psItem.setInt(2, item.getProductId());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getPriceAtSale());
                    psItem.addBatch();
                }
                psItem.executeBatch();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}