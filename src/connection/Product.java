package connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Product {
    public static List<model.Product> getAllProducts() {
        List<model.Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.name, c.name as cat_name, b.name as brand_name, " +
                "s.name as supplier_name, p.price, p.specifications, p.stock_quantity " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN brands b ON p.brand_id = b.brand_id " +
                "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new model.Product(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("cat_name"),
                        rs.getString("brand_name"),
                        rs.getString("supplier_name"),
                        rs.getDouble("price"),
                        rs.getString("specifications"),
                        rs.getInt("stock_quantity")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }
}