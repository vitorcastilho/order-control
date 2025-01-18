package com.ordercontrol.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.repository.CustomerRepository;
import com.ordercontrol.exception.ResourceNotFoundException;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<Customer> listAllCustomers() {
		List<Customer> customers = customerRepository.findAll();
		return customers;
	}

	@Override
	public Customer getCustomerById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with Id: ".concat(id.toString()),
						"Cliente não encontrado. Favor verificar o id fornecido."));
		return customer;
	}

	@Override
	public Long saveCustumer(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		return newCustomer.getId();
	}

}
