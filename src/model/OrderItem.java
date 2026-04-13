package model;

public class OrderItem {
    private final int orderItemId;
    private final Product product;
    private final int quantity;
    private final double priceAtSale;

    public OrderItem(int orderItemId, Product product, int quantity, double priceAtSale) {
        this.orderItemId = orderItemId;
        this.product = product;
        this.quantity = quantity;
        this.priceAtSale = priceAtSale;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceAtSale() {
        return priceAtSale;
    }
}