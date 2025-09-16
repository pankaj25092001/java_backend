package com.example.Pankaj.orderup.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(Long productId) {
        super("Product with ID " + productId + " is out of stock.");
    }
}
