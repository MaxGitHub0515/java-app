package org.example.controller;

import org.example.dao.AnalyticsDAO;
import org.example.dao.CustomerDAO;
import org.example.dao.OrderDAO;
import org.example.dao.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnalyticsController {

    @Autowired AnalyticsDAO analyticsDAO;
    @Autowired CustomerDAO  customerDAO;
    @Autowired ProductDAO   productDAO;
    @Autowired OrderDAO     orderDAO;

    @GetMapping({"/", "/analytics"})
    public String analytics(Model model) {
        model.addAttribute("totalRevenue",      analyticsDAO.getTotalRevenue());
        model.addAttribute("avgOrderValue",     analyticsDAO.getAvgOrderValue());
        model.addAttribute("inventoryValue",    analyticsDAO.getTotalInventoryValue());
        model.addAttribute("totalOrders",       orderDAO.count());
        model.addAttribute("totalCustomers",    customerDAO.count());
        model.addAttribute("totalProducts",     productDAO.count());
        model.addAttribute("ordersByStatus",    analyticsDAO.getOrderCountByStatus());
        model.addAttribute("topCustomers",      analyticsDAO.getTopCustomers(5));
        model.addAttribute("bestProducts",      analyticsDAO.getBestSellingProducts(5));
        model.addAttribute("revenuePerProduct", analyticsDAO.getRevenuePerProduct());
        return "analytics";
    }
}
