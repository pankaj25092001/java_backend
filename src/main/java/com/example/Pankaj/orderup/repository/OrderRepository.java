package com.example.Pankaj.orderup.repository;

import com.example.Pankaj.orderup.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
