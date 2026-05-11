package org.example.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

// IMPORTANT: name="ShopOrder" avoids clash with HQL reserved word ORDER
@Entity(name = "ShopOrder")
@Table(name = "orders")
public class Order {

    public enum OrderStatus { Pending, Shipped, Delivered, Cancelled }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "order_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus = OrderStatus.Pending;

    @Column(name = "total_amount")
    private double totalAmount;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    public Order() {}
    public Order(Customer customer, double totalAmount) {
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.orderStatus = OrderStatus.Pending;
    }

    public int getOrderId()                          { return orderId; }
    public void setOrderId(int v)                    { this.orderId = v; }
    public Customer getCustomer()                    { return customer; }
    public void setCustomer(Customer v)              { this.customer = v; }
    public Date getOrderDate()                       { return orderDate; }
    public void setOrderDate(Date v)                 { this.orderDate = v; }
    public OrderStatus getOrderStatus()              { return orderStatus; }
    public void setOrderStatus(OrderStatus v)        { this.orderStatus = v; }
    public double getTotalAmount()                   { return totalAmount; }
    public void setTotalAmount(double v)             { this.totalAmount = v; }
    public List<OrderDetail> getOrderDetails()       { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> v) { this.orderDetails = v; }

    public String getStatusBadgeClass() {
        if (orderStatus == null) return "badge-pending";
        switch (orderStatus) {
            case Shipped:   return "badge-shipped";
            case Delivered: return "badge-delivered";
            case Cancelled: return "badge-cancelled";
            default:        return "badge-pending";
        }
    }
}
