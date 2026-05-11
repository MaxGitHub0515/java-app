package org.example.dao;

import org.example.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class CustomerDAO {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void addCustomer(Customer c) {
        c.setRegistrationDate(new Date());
        em.persist(c);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(int id) {
        return em.find(Customer.class, id);
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return em.createQuery("FROM Customer ORDER BY lastName", Customer.class)
                .getResultList();
    }

    @Transactional(readOnly = true)
    public List<Customer> searchByName(String kw) {
        return em.createQuery(
                        "FROM Customer c WHERE lower(c.firstName) LIKE :k OR lower(c.lastName) LIKE :k",
                        Customer.class)
                .setParameter("k", "%" + kw.toLowerCase() + "%")
                .getResultList();
    }

    @Transactional
    public void updateCustomer(Customer c) {
        Customer existing = em.find(Customer.class, c.getCustomerId());
        if (existing != null) {
            // Preserve the original registration date — never overwrite on update
            c.setRegistrationDate(existing.getRegistrationDate());
        }
        em.merge(c);
    }

    @Transactional
    public void deleteCustomer(int id) {
        em.createQuery(
                        "DELETE FROM OrderDetail od WHERE od.order.orderId IN " +
                                "(SELECT o.orderId FROM ShopOrder o WHERE o.customer.customerId = :id)")
                .setParameter("id", id)
                .executeUpdate();

        em.createQuery(
                        "DELETE FROM ShopOrder o WHERE o.customer.customerId = :id")
                .setParameter("id", id)
                .executeUpdate();

        Customer c = em.find(Customer.class, id);
        if (c != null) em.remove(c);
    }

    @Transactional(readOnly = true)
    public long count() {
        return em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class)
                .getSingleResult();
    }
}

