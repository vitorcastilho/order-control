package com.ordercontrol.application.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.repository.ICustomerRepository;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	private ICustomerRepository customerRepository;

	@Override
	public CustomPageResponse<Customer> listAllCustomers(Pageable pageable) {
		Page<Customer> customerPage = customerRepository.findAll(pageable);
		return CustomPageResponse.fromPage(customerPage);
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		return customerRepository.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Customer not found with Id: ".concat(customerId.toString()),
						"Cliente não encontrado. Favor verificar o id fornecido."));
	}

	@Override
	public Long saveCustumer(Customer customer) {
		Customer newCustomer = customerRepository.save(customer);
		return newCustomer.getId();
	}

}
