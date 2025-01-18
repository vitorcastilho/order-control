package com.ordercontrol.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordercontrol.domain.model.Customer;

public interface ICustomerRepository extends JpaRepository<Customer, Long> {

}
