package model;

public class Product {
    private final int productId;
    private final String name;
    private final String category;
    private final String brand;
    private final String supplier;
    private final double price;
    private final String specifications;
    private final int stockQuantity;

    public Product(int productId, String name, String category, String brand, String supplier, double price, String specifications, int stockQuantity) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.supplier = supplier;
        this.price = price;
        this.specifications = specifications;
        this.stockQuantity = stockQuantity;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBrand() {
        return brand;
    }

    public String getSupplier() {
        return supplier;
    }

    public double getPrice() {
        return price;
    }

    public String getSpecifications() {
        return specifications;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}