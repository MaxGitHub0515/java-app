package org.example.controller;

import org.example.dao.ProductDAO;
import org.example.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired ProductDAO productDAO;

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("products",
                (search != null && !search.isBlank())
                        ? productDAO.searchByName(search)
                        : productDAO.getAllProducts());
        model.addAttribute("search", search);
        model.addAttribute("lowStock", productDAO.getLowStock(10));
        return "products";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product, RedirectAttributes ra) {
        if (product.getProductId() == 0) {
            productDAO.addProduct(product);
            ra.addFlashAttribute("success", "Product added.");
        } else {
            productDAO.updateProduct(product);
            ra.addFlashAttribute("success", "Product updated.");
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("product", productDAO.getProductById(id));
        return "product-form";
    }

    @PostMapping("/restock/{id}")
    public String restock(@PathVariable int id,
                          @RequestParam int quantity,
                          RedirectAttributes ra) {
        productDAO.restockProduct(id, quantity);
        ra.addFlashAttribute("success", "Restocked successfully.");
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        try {
            productDAO.deleteProduct(id);
            ra.addFlashAttribute("success", "Product deleted.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/products";
    }
}
