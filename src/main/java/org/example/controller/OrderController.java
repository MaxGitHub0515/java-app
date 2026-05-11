package org.example.controller;

import org.example.dao.CustomerDAO;
import org.example.dao.OrderDAO;
import org.example.dao.ProductDAO;
import org.example.model.Order;
import org.example.model.Order.OrderStatus;
import org.example.model.OrderDetail;
import org.example.model.Product;
import org.example.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired OrderDAO    orderDAO;
    @Autowired CustomerDAO customerDAO;
    @Autowired ProductDAO  productDAO;

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        List<Order> orders;
        if (status != null && !status.isBlank() && !status.equals("All")) {
            try { orders = orderDAO.getByStatus(OrderStatus.valueOf(status)); }
            catch (IllegalArgumentException e) { orders = orderDAO.getAllOrders(); }
        } else {
            orders = orderDAO.getAllOrders();
        }
        model.addAttribute("orders", orders);
        model.addAttribute("selectedStatus", status != null ? status : "All");
        model.addAttribute("statuses", OrderStatus.values());
        return "orders";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable int id, Model model) {
        Order o = orderDAO.getOrderById(id);
        if (o == null) return "redirect:/orders";
        model.addAttribute("order", o);
        model.addAttribute("details", orderDAO.getDetails(id));
        model.addAttribute("statuses", OrderStatus.values());
        return "order-detail";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable int id,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        try {
            orderDAO.updateStatus(id, OrderStatus.valueOf(status));
            ra.addFlashAttribute("success", "Order status updated.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", "Invalid status.");
        }
        return "redirect:/orders/" + id;
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable int id, RedirectAttributes ra) {
        orderDAO.cancelOrder(id);
        ra.addFlashAttribute("success", "Order cancelled. Stock restored.");
        return "redirect:/orders";
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("customers", customerDAO.getAllCustomers());
        model.addAttribute("products", productDAO.getAllProducts());
        return "order-form";
    }

    @PostMapping("/place")
    public String placeOrder(@RequestParam int customerId,
                             @RequestParam List<Integer> productIds,
                             @RequestParam List<Integer> quantities,
                             RedirectAttributes ra) {
        try {
            Customer customer = customerDAO.getCustomerById(customerId);
            List<OrderDetail> items = new ArrayList<>();
            for (int i = 0; i < productIds.size(); i++) {
                if (quantities.get(i) <= 0) continue;
                Product p = productDAO.getProductById(productIds.get(i));
                items.add(new OrderDetail(null, p, quantities.get(i), p.getPrice()));
            }
            if (items.isEmpty()) {
                ra.addFlashAttribute("error", "No items selected.");
                return "redirect:/orders/new";
            }
            Order order = orderDAO.placeOrder(customer, items);
            ra.addFlashAttribute("success", "Order #" + order.getOrderId() + " placed successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
            return "redirect:/orders/new";
        }
        return "redirect:/orders";
    }
}
