package com.example.Pankaj.orderup.controller;

import com.example.Pankaj.orderup.model.Product;
import com.example.Pankaj.orderup.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String placeOrder(@RequestParam Long productId, @RequestParam String customerName) {
        orderService.placeOrder(productId, customerName);
        return "Order placed successfully for " + customerName;
    }
    @Autowired
    private com.example.Pankaj.orderup.repository.ProductRepository productRepository;

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Autowired
    private com.example.Pankaj.orderup.repository.OrderRepository orderRepository;

    @GetMapping
    public List<com.example.Pankaj.orderup.model.Order> getAllOrders() {
        return orderRepository.findAll();
    }


}
