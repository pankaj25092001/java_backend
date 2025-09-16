package com.example.Pankaj.orderup.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderLoggingAspect {

    @Around("execution(* com.example.orderup.service.OrderService.placeOrder(..))")
    public Object logOrderProcessingTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        System.out.println("🚀 Starting method: " + methodName + " with args: " + args[0] + ", " + args[1]);
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        System.out.println("✅ Finished method: " + methodName + " in " + (end - start) + "ms");

        return result;
    }
}
