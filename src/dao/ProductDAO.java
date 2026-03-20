package dao;

import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT p.*, b.name AS brand_name, s.name AS supplier_name " + "FROM products p " + "JOIN brands b ON p.brand_id = b.brand_id " + "JOIN suppliers s ON p.supplier_id = s.supplier_id";

        try (Connection conn = DatabaseManager.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
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
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantitySold);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        }
    }
}