package com.example.Pankaj.orderup.controller;

import com.example.Pankaj.orderup.model.Order;
import com.example.Pankaj.orderup.model.Product;
import com.example.Pankaj.orderup.repository.OrderRepository;
import com.example.Pankaj.orderup.repository.ProductRepository;
import com.example.Pankaj.orderup.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    private Product product1;
    private Product product2;

    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("Pizza");
        product1.setStock(10);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Burger");
        product2.setStock(5);

        order1 = new Order();
        order1.setId(1L);
        order1.setCustomerName("Alice");
        order1.setProduct(product1);

        order2 = new Order();
        order2.setId(2L);
        order2.setCustomerName("Bob");
        order2.setProduct(product2);
    }

    @Test
    void testPlaceOrder_ReturnsSuccessMessage() throws Exception {
        Long productId = 1L;
        String customerName = "Charlie";

        doNothing().when(orderService).placeOrder(productId, customerName);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .param("productId", productId.toString())
                        .param("customerName", customerName)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Order placed successfully for " + customerName)));

        verify(orderService, times(1)).placeOrder(productId, customerName);
    }

    @Test
    void testGetAllProducts_ReturnsListOfProducts() throws Exception {
        List<Product> products = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Pizza"))
                .andExpect(jsonPath("$[1].name").value("Burger"));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllOrders_ReturnsListOfOrders() throws Exception {
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].customerName").value("Alice"))
                .andExpect(jsonPath("$[1].customerName").value("Bob"));

        verify(orderRepository, times(1)).findAll();
    }
}
