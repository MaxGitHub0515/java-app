package org.example.dao;

import org.example.model.*;
import org.example.model.Order.OrderStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Order placeOrder(Customer customer, List<OrderDetail> items) {
        Customer managedCustomer = em.find(Customer.class, customer.getCustomerId());
        if (managedCustomer == null)
            throw new RuntimeException("Customer not found: " + customer.getCustomerId());

        for (OrderDetail od : items) {
            Product p = em.find(Product.class, od.getProduct().getProductId());
            if (p == null)
                throw new RuntimeException("Product not found.");
            if (p.getStockQuantity() < od.getQuantity())
                throw new RuntimeException("Insufficient stock for: " + p.getName() +
                        " (available: " + p.getStockQuantity() + ")");
        }

        double total = items.stream().mapToDouble(OrderDetail::getLineTotal).sum();
        Order order = new Order(managedCustomer, total);
        order.setOrderDate(new Date());
        em.persist(order);

        for (OrderDetail od : items) {
            od.setOrder(order);
            em.persist(od);
            Product p = em.find(Product.class, od.getProduct().getProductId());
            p.setStockQuantity(p.getStockQuantity() - od.getQuantity());
            em.merge(p);
        }

        return order;
    }

    // FIX: JOIN FETCH customer eagerly so Thymeleaf can access it after session closes
    @Transactional(readOnly = true)
    public Order getOrderById(int id) {
        List<Order> results = em.createQuery(
                        "FROM ShopOrder o JOIN FETCH o.customer WHERE o.orderId = :id",
                        Order.class)
                .setParameter("id", id)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return em.createQuery(
                        "FROM ShopOrder o JOIN FETCH o.customer ORDER BY o.orderDate DESC",
                        Order.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Order> getByCustomer(int customerId) {
        return em.createQuery(
                        "FROM ShopOrder o JOIN FETCH o.customer " +
                                "WHERE o.customer.customerId = :cid ORDER BY o.orderDate DESC",
                        Order.class)
                .setParameter("cid", customerId)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Order> getByStatus(OrderStatus status) {
        return em.createQuery(
                        "FROM ShopOrder o JOIN FETCH o.customer WHERE o.orderStatus = :s " +
                                "ORDER BY o.orderDate DESC",
                        Order.class)
                .setParameter("s", status)
                .getResultList();
    }

    // FIX: @Transactional(readOnly=true) so persistence context is open for this query
    @Transactional(readOnly = true)
    public List<OrderDetail> getDetails(int orderId) {
        return em.createQuery(
                        "FROM OrderDetail od JOIN FETCH od.product WHERE od.order.orderId = :oid",
                        OrderDetail.class)
                .setParameter("oid", orderId)
                .getResultList();
    }

    @Transactional
    public void updateStatus(int orderId, OrderStatus status) {
        Order o = em.find(Order.class, orderId);
        if (o != null) {
            o.setOrderStatus(status);
            em.merge(o);
        }
    }

    @Transactional
    public void cancelOrder(int orderId) {
        Order o = em.find(Order.class, orderId);
        if (o == null) return;

        List<OrderDetail> details = em.createQuery(
                        "FROM OrderDetail od JOIN FETCH od.product WHERE od.order.orderId = :oid",
                        OrderDetail.class)
                .setParameter("oid", orderId)
                .getResultList();

        for (OrderDetail od : details) {
            Product p = em.find(Product.class, od.getProduct().getProductId());
            if (p != null) {
                p.setStockQuantity(p.getStockQuantity() + od.getQuantity());
                em.merge(p);
            }
        }

        o.setOrderStatus(OrderStatus.Cancelled);
        em.merge(o);
    }

    @Transactional
    public void deleteOrder(int orderId) {
        em.createQuery("DELETE FROM OrderDetail od WHERE od.order.orderId = :oid")
                .setParameter("oid", orderId)
                .executeUpdate();
        Order o = em.find(Order.class, orderId);
        if (o != null) em.remove(o);
    }

    @Transactional(readOnly = true)
    public long count() {
        return em.createQuery("SELECT COUNT(o) FROM ShopOrder o", Long.class)
                .getSingleResult();
    }
}
