package connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {

    public List<model.Product> getAllProducts() {
        List<model.Product> products = new ArrayList<>();

        String sql = "SELECT p.*, b.name AS brand_name, s.name AS supplier_name " + "FROM products p " + "JOIN brands b ON p.brand_id = b.brand_id " + "JOIN suppliers s ON p.supplier_id = s.supplier_id";

        try (Connection conn = Database.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                model.Product p = new model.Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setBrandName(rs.getString("brand_name"));
                p.setSupplierName(rs.getString("supplier_name"));
                p.setSpecifications(rs.getString("specifications"));
                p.setPrice(rs.getBigDecimal("price"));
                p.setStockQuantity(rs.getInt("stock_quantity"));
                products.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }


    public void updateStock(int productId, int quantitySold) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantitySold);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }
}