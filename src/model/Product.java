package model;

import java.math.BigDecimal;

public class Product {
    private int id;
    private String name;
    private int categoryId;   
    private int brandId;      
    private int supplierId;   
    private String brandName;    
    private String supplierName; 
    private BigDecimal price; 
    private String specifications;
    private int stockQuantity;

    public Product(int id, String name, int categoryId, int brandId, int supplierId, String brandName, String supplierName, BigDecimal price, String specifications, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.supplierId = supplierId;
        this.brandName = brandName;
        this.supplierName = supplierName;
        this.price = price;
        this.specifications = specifications;
        this.stockQuantity = stockQuantity;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", brandId=" + brandId +
                ", supplierId=" + supplierId +
                ", brandName='" + brandName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", price=" + price +
                ", specifications='" + specifications + '\'' +
                ", stockQuantity=" + stockQuantity +
                '}';
    }

}