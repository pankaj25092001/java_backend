# 🍔 OrderUp — Spring Boot Order Management System

A simple RESTful API for placing food orders, with a focus on **thread safety**, **clean architecture**, and **clear exception handling**.

---

## 🧱 Project Overview
This app allows customers to place orders for products (like food items). It manages stock levels, throws helpful errors when things go wrong (like out-of-stock or missing products), and logs performance using AOP.

### Key Features:
- Place an order for a product
- View all products
- View all orders
- Global error handling
- Thread-safe stock updates
- Logging via AOP

---

## Design Decisions

### Why Use `ReentrantLock`?
Updating product stock is a **critical section** — if two people place an order at the same time for the last unit of stock, we risk overselling. Instead of using `synchronized`, I chose `ReentrantLock` for these reasons:

- It's more **flexible** — we can lock/unlock exactly where we need
- It **doesn't block** unrelated threads or methods
- It plays nicer with Spring's AOP and transaction management

**In `OrderService`:**
```java
lock.lock();
try {
    // check stock, reduce stock, save order
} finally {
    lock.unlock();
}
```

### How the AOP Aspect Works
The `OrderLoggingAspect` uses `@Aspect` with `@Around` advice to log:

- When the `placeOrder()` method starts
- What arguments it received
- How long the method took to execute

Example:
```
Starting method: placeOrder with args: 1, Alice
Finished method: placeOrder in 15ms
```

---

### Why AOP?
- Keeps logging **out of business logic**
- Results in **cleaner, more readable code**
- Easily **reusable** for other methods if needed later

---

## API Endpoints

| Method | URL               | Description         |
|--------|-------------------|---------------------|
| POST   | `/orders`         | Place a new order   |
| GET    | `/orders/products`| List all products   |
| GET    | `/orders`         | List all orders     |

---

## How to Test with `curl`
> Make sure the app is running at `http://localhost:8080`

### 1. ✅ Successful Order
```bash
curl -X POST "http://localhost:8080/orders?productId=1&customerName=Alice"
```
Response:
```
Order placed successfully for Alice
```

### 2. ❌ Product Not Found
```bash
curl -X POST "http://localhost:8080/orders?productId=999&customerName=Bob"
```
Response:
```
Product with ID 999 not found.
HTTP Status: 400 Bad Request
```

### 3. ❌ Out of Stock
First, either set stock = 0 for a product manually in DB, OR place enough orders to deplete it.

```bash
curl -X POST "http://localhost:8080/orders?productId=1&customerName=Charlie"
```
Response:
```
Product with ID 1 is out of stock.
HTTP Status: 400 Bad Request
```

---

## 🧪 Testing

The project includes unit and integration tests for:
- ✅ `OrderService` (thread-safe business logic)
- ✅ `OrderController` (REST endpoint layer)
- ✅ `GlobalExceptionHandler` (error handling)
- ✅ `OrderLoggingAspect` (AOP logging)
- ✅ Custom exceptions (`ProductNotFound`, `OutOfStock`)

Run all tests using:
```bash
mvn clean test verify
```

---

## 🛠 Tech Stack
- Java 17
- Spring Boot 3.x
- Maven
- JPA / Hibernate
- H2 / PostgreSQL
- JUnit 5
- Mockito

---

## ✅ To Run the App
```bash
mvn spring-boot:run
```

---

## 🧪 What’s Covered

### 🔧 `OrderService`
- Thread-safe order placement logic
- Stock checks and reductions
- Exception throwing (e.g., out of stock, product not found)

**Type:** Unit tests  
**Tools:** JUnit 5 + Mockito

---

### 🌐 `OrderController`
- REST endpoint behaviors
- Parameter parsing
- Delegation to service layer

**Type:** Unit tests using `@WebMvcTest`  
**Tools:** MockMvc, Spring Boot Test

---

### ⚠️ `GlobalExceptionHandler`
- Custom error responses for:
  - Product not found
  - Out of stock
  - Unexpected errors

**Type:** Integration-style unit tests  
**Tools:** MockMvc + Mocked Service Behavior

---

### 🕵️ `OrderLoggingAspect`
- Logs method start and end
- Captures method arguments
- Measures execution time

**Type:** Unit test with `ProceedingJoinPoint` mock  
**Tools:** JUnit 5 + Mockito

---

### ❗ Custom Exceptions
- `ProductNotFoundException`
- `OutOfStockException`

Each is tested for correct message formatting.

**Type:** Unit tests  
**Tools:** JUnit 5

---

## 🧰 Tools & Libraries
- **JUnit 5** — testing framework  
- **Mockito** — mocking service layers and dependencies  
- **Spring Boot Test** — testing annotations and test context  
- **MockMvc** — simulating HTTP requests to controllers  

---

## 🚀 How to Run All Tests
From the root of the project:
```bash
mvn test
```
