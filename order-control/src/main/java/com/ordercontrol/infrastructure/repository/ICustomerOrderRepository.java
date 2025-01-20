package com.ordercontrol.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.enums.CustomerOrderStatus;

public interface ICustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

	Page<CustomerOrder> findOrderByStatus(CustomerOrderStatus orderStatus, Pageable pageable);
	
	boolean existsByNumberOrder(String numberOrder);
}
