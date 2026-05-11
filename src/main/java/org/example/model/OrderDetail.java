package org.example.model;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private int orderDetailId;

    // mappedBy refers to the field name "orderDetails" on the Order class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price;

    public OrderDetail() {}
    public OrderDetail(Order order, Product product, int quantity, double price) {
        this.order = order; this.product = product;
        this.quantity = quantity; this.price = price;
    }

    public int getOrderDetailId()               { return orderDetailId; }
    public void setOrderDetailId(int v)         { this.orderDetailId = v; }
    public Order getOrder()                     { return order; }
    public void setOrder(Order v)               { this.order = v; }
    public Product getProduct()                 { return product; }
    public void setProduct(Product v)           { this.product = v; }
    public int getQuantity()                    { return quantity; }
    public void setQuantity(int v)              { this.quantity = v; }
    public double getPrice()                    { return price; }
    public void setPrice(double v)              { this.price = v; }
    public double getLineTotal()                { return quantity * price; }
}
