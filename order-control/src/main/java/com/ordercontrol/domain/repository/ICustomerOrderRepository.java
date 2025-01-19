package com.ordercontrol.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ordercontrol.domain.model.CustomerOrder;

public interface ICustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

}
