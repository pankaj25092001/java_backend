package com.example.Pankaj.orderup.service;

import com.example.Pankaj.orderup.exception.OutOfStockException;
import com.example.Pankaj.orderup.exception.ProductNotFoundException;
import com.example.Pankaj.orderup.model.Order;
import com.example.Pankaj.orderup.model.Product;
import com.example.Pankaj.orderup.repository.OrderRepository;
import com.example.Pankaj.orderup.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderService(productRepository, orderRepository);
    }

    @Test
    void placeOrder_successfulOrder_reducesStockAndSavesOrder() {
        // Arrange
        Long productId = 1L;
        String customerName = "John Doe";
        Product product = new Product();
        product.setId(productId);
        product.setStock(5);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        orderService.placeOrder(productId, customerName);

        // Assert
        assertEquals(4, product.getStock()); // Stock reduced
        verify(productRepository).save(product); // Stock saved

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        assertEquals(customerName, orderCaptor.getValue().getCustomerName());
        assertEquals(product, orderCaptor.getValue().getProduct());
    }

    @Test
    void placeOrder_productNotFound_throwsException() {
        // Arrange
        Long productId = 1L;
        String customerName = "Jane";

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            orderService.placeOrder(productId, customerName);
        });

        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void placeOrder_outOfStock_throwsException() {
        // Arrange
        Long productId = 1L;
        String customerName = "Alice";
        Product product = new Product();
        product.setId(productId);
        product.setStock(0); // Out of stock

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(OutOfStockException.class, () -> {
            orderService.placeOrder(productId, customerName);
        });

        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }
}
