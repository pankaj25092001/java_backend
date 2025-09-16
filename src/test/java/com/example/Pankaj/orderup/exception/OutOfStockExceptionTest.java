package com.example.Pankaj.orderup.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OutOfStockExceptionTest {

    @Test
    void testExceptionMessage() {
        Long productId = 100L;
        OutOfStockException ex = new OutOfStockException(productId);

        assertEquals("Product with ID 100 is out of stock.", ex.getMessage());
    }
}
