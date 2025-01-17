package com.ordercontrol.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.repository.CustomerRepository;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public Long saveCustumer(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		return newCustomer.getId();
	}

}
