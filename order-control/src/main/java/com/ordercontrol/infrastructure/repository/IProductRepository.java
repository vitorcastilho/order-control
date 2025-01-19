package com.ordercontrol.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordercontrol.domain.model.Product;

public interface IProductRepository extends JpaRepository<Product, Long> {

}
