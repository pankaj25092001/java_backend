package com.example.Pankaj.orderup.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class OrderLoggingAspectTest {

    private OrderLoggingAspect aspect;
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        aspect = new OrderLoggingAspect();
        joinPoint = mock(ProceedingJoinPoint.class);
    }

    @Test
    void testLogOrderProcessingTime() throws Throwable {
        // Mock method signature
        Signature signature = mock(Signature.class);
        when(signature.getName()).thenReturn("placeOrder");
        when(joinPoint.getSignature()).thenReturn(signature);

        // Mock arguments
        Long productId = 1L;
        String customerName = "John";
        when(joinPoint.getArgs()).thenReturn(new Object[]{productId, customerName});

        // Mock the actual method execution
        when(joinPoint.proceed()).thenReturn(null);  // Or return any result you expect

        // Call aspect method
        Object result = aspect.logOrderProcessingTime(joinPoint);

        // Verify method proceeded
        verify(joinPoint, times(1)).proceed();


    }
}
