package com.example.Pankaj.orderup.service;

import com.example.Pankaj.orderup.model.Order;
import com.example.Pankaj.orderup.model.Product;
import com.example.Pankaj.orderup.repository.OrderRepository;
import com.example.Pankaj.orderup.repository.ProductRepository;
import com.example.Pankaj.orderup.exception.ProductNotFoundException;
import com.example.Pankaj.orderup.exception.OutOfStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void placeOrder(Long productId, String customerName) {
        lock.lock();
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            if (product.getStock() <= 0) {
                throw new OutOfStockException(productId);
            }

            // Reduce stock
            product.setStock(product.getStock() - 1);
            productRepository.save(product);

            // Create and save order
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setProduct(product);
            orderRepository.save(order);

        } finally {
            lock.unlock();
        }
    }
}
