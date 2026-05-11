package org.example.controller;

import org.example.dao.CustomerDAO;
import org.example.dao.OrderDAO;
import org.example.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired CustomerDAO customerDAO;
    @Autowired OrderDAO    orderDAO;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("customers",
                (search != null && !search.isBlank())
                        ? customerDAO.searchByName(search)
                        : customerDAO.getAllCustomers());
        model.addAttribute("search", search);
        return "customers";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable int id, Model model) {
        Customer c = customerDAO.getCustomerById(id);
        if (c == null) return "redirect:/customers";
        model.addAttribute("customer", c);
        model.addAttribute("orders", orderDAO.getByCustomer(id));
        return "customer-detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "customer-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer, RedirectAttributes ra) {
        if (customer.getCustomerId() == 0) {
            customerDAO.addCustomer(customer);
            ra.addFlashAttribute("success", "Customer added successfully.");
        } else {
            customerDAO.updateCustomer(customer);
            ra.addFlashAttribute("success", "Customer updated.");
        }
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("customer", customerDAO.getCustomerById(id));
        return "customer-form";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        customerDAO.deleteCustomer(id);
        ra.addFlashAttribute("success", "Customer deleted.");
        return "redirect:/customers";
    }
}
