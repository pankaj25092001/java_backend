package com.example.Pankaj.orderup.repository;

import com.example.Pankaj.orderup.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

