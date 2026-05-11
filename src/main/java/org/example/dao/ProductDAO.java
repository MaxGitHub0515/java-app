package org.example.dao;

import org.example.model.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class ProductDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void addProduct(Product p) {
        p.setDateAdded(new Date());
        em.persist(p);
    }

    @Transactional(readOnly = true)
    public Product getProductById(int id) {
        return em.find(Product.class, id);
    }

    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return em.createQuery("FROM Product ORDER BY name", Product.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Product> searchByName(String kw) {
        return em.createQuery(
                        "FROM Product p WHERE lower(p.name) LIKE :k", Product.class)
                .setParameter("k", "%" + kw.toLowerCase() + "%")
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStock(int threshold) {
        return em.createQuery(
                        "FROM Product p WHERE p.stockQuantity <= :t ORDER BY p.stockQuantity",
                        Product.class)
                .setParameter("t", threshold)
                .getResultList();
    }

    @Transactional
    public void updateProduct(Product p) {
        Product existing = em.find(Product.class, p.getProductId());
        if (existing != null) {
            p.setDateAdded(existing.getDateAdded());
        }
        em.merge(p);
    }

    @Transactional
    public void restockProduct(int id, int qty) {
        Product p = em.find(Product.class, id);
        if (p != null) {
            p.setStockQuantity(p.getStockQuantity() + qty);
            em.merge(p);
        }
    }

    @Transactional
    public void deleteProduct(int id) {
        // FIX: do NOT delete order_details when a product is deleted.
        // Deleting order_details destroys the historical record of what was in each order.
        // Instead, block deletion if the product has any order history.
        Long usageCount = em.createQuery(
                        "SELECT COUNT(od) FROM OrderDetail od WHERE od.product.productId = :id",
                        Long.class)
                .setParameter("id", id)
                .getSingleResult();

        if (usageCount > 0) {
            throw new RuntimeException(
                    "Cannot delete product: it appears in " + usageCount + " order(s). " +
                            "Cancel or delete those orders first, or keep the product in the catalogue.");
        }

        Product p = em.find(Product.class, id);
        if (p != null) em.remove(p);
    }

    @Transactional(readOnly = true)
    public long count() {
        return em.createQuery("SELECT COUNT(p) FROM Product p", Long.class)
                .getSingleResult();
    }

    @Transactional(readOnly = true)
    public double avgPrice() {
        Double r = em.createQuery("SELECT AVG(p.price) FROM Product p", Double.class)
                .getSingleResult();
        return r != null ? r : 0.0;
    }
}
