package com.ordercontrol.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordercontrol.domain.model.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {

}
