package com.example.Pankaj.orderup.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        Long productId = 42L;
        ProductNotFoundException ex = new ProductNotFoundException(productId);

        assertEquals("Product with ID 42 not found.", ex.getMessage());
    }
}
