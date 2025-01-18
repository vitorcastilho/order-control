package com.ordercontrol.domain.service.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.repository.ICustomerRepository;
import com.ordercontrol.exception.ResourceNotFoundException;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private ICustomerRepository customerRepository;

	@Override
	public List<Customer> listAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found with Id: ".concat(id.toString()),
						"Cliente não encontrado. Favor verificar o id fornecido."));
	}

	@Override
	public Long saveCustumer(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		return newCustomer.getId();
	}

}
