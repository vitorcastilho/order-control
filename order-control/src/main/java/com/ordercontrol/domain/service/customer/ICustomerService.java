package com.ordercontrol.domain.service.customer;

import java.util.List;

import com.ordercontrol.domain.model.Customer;

public interface ICustomerService {

	List<Customer> listAllCustomers();

	Customer getCustomerById(Long id);

	Long saveCustumer(Customer customer);

}
