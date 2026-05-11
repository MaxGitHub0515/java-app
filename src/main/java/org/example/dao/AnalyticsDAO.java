package org.example.dao;

import org.example.model.Order.OrderStatus;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnalyticsDAO {

    @PersistenceContext
    private EntityManager em;

    public double getTotalRevenue() {
        Double r = em.createQuery(
                "SELECT SUM(o.totalAmount) FROM ShopOrder o WHERE o.orderStatus <> :cancelled",
                Double.class)
            .setParameter("cancelled", OrderStatus.Cancelled)
            .getSingleResult();
        return r != null ? r : 0.0;
    }

    public double getAvgOrderValue() {
        Double r = em.createQuery(
                "SELECT AVG(o.totalAmount) FROM ShopOrder o WHERE o.orderStatus <> :cancelled",
                Double.class)
            .setParameter("cancelled", OrderStatus.Cancelled)
            .getSingleResult();
        return r != null ? r : 0.0;
    }

    public double getTotalInventoryValue() {
        Double r = em.createQuery(
                "SELECT SUM(p.price * p.stockQuantity) FROM Product p",
                Double.class)
            .getSingleResult();
        return r != null ? r : 0.0;
    }

    /** Returns Object[]{status, count} */
    public List<Object[]> getOrderCountByStatus() {
        return em.createQuery(
                "SELECT o.orderStatus, COUNT(o) FROM ShopOrder o GROUP BY o.orderStatus",
                Object[].class)
            .getResultList();
    }

    /** Returns Object[]{firstName, lastName, revenue, orderCount} */
    public List<Object[]> getTopCustomers(int limit) {
        return em.createQuery(
                "SELECT c.firstName, c.lastName, SUM(o.totalAmount), COUNT(o) " +
                "FROM ShopOrder o JOIN o.customer c " +
                "WHERE o.orderStatus <> :cancelled " +
                "GROUP BY c.customerId ORDER BY SUM(o.totalAmount) DESC",
                Object[].class)
            .setParameter("cancelled", OrderStatus.Cancelled)
            .setMaxResults(limit)
            .getResultList();
    }

    /** Returns Object[]{name, unitsSold, revenue} */
    public List<Object[]> getBestSellingProducts(int limit) {
        return em.createQuery(
                "SELECT p.name, SUM(od.quantity), SUM(od.quantity * od.price) " +
                "FROM OrderDetail od JOIN od.product p " +
                "GROUP BY p.productId ORDER BY SUM(od.quantity) DESC",
                Object[].class)
            .setMaxResults(limit)
            .getResultList();
    }

    /** Returns Object[]{name, unitsSold, revenue, avgPrice} */
    public List<Object[]> getRevenuePerProduct() {
        return em.createQuery(
                "SELECT p.name, SUM(od.quantity), SUM(od.quantity * od.price), AVG(od.price) " +
                "FROM OrderDetail od JOIN od.product p " +
                "GROUP BY p.productId ORDER BY SUM(od.quantity * od.price) DESC",
                Object[].class)
            .getResultList();
    }

    /** Returns Object[]{firstName, lastName, orderCount} */
    public List<Object[]> getOrdersPerCustomer() {
        return em.createQuery(
                "SELECT c.firstName, c.lastName, COUNT(o) " +
                "FROM ShopOrder o JOIN o.customer c " +
                "GROUP BY c.customerId ORDER BY COUNT(o) DESC",
                Object[].class)
            .getResultList();
    }
}
