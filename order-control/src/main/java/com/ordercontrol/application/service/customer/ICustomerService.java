package com.ordercontrol.application.service.customer;

import org.springframework.data.domain.Pageable;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.utils.pagination.CustomPageResponse;

public interface ICustomerService {

	CustomPageResponse<Customer> listAllCustomers(Pageable pageable);

	Customer getCustomerById(Long customerId);

	Long saveCustumer(Customer customer);

}
