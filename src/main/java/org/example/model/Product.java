package org.example.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "date_added")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdded;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    public Product() {}

    public Product(String name, String description, double price, int stockQuantity) {
        this.name = name; this.description = description;
        this.price = price; this.stockQuantity = stockQuantity;
    }

    public int getProductId()                       { return productId; }
    public void setProductId(int v)                 { this.productId = v; }
    public String getName()                         { return name; }
    public void setName(String v)                   { this.name = v; }
    public String getDescription()                  { return description; }
    public void setDescription(String v)            { this.description = v; }
    public double getPrice()                        { return price; }
    public void setPrice(double v)                  { this.price = v; }
    public int getStockQuantity()                   { return stockQuantity; }
    public void setStockQuantity(int v)             { this.stockQuantity = v; }
    public Date getDateAdded()                      { return dateAdded; }
    public void setDateAdded(Date v)                { this.dateAdded = v; }
    public List<OrderDetail> getOrderDetails()      { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> v){ this.orderDetails = v; }

    public int getStockPercent() {
        int max = 50;
        return Math.min(100, (int)((stockQuantity / (double) max) * 100));
    }
    public String getStockColor() {
        int pct = getStockPercent();
        if (pct > 50) return "#639922";
        if (pct > 20) return "#EF9F27";
        return "#E24B4A";
    }
}
