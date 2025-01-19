package com.ordercontrol.web.api.v1.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.application.dto.customer.CustomerInsertDto;
import com.ordercontrol.application.service.customer.ICustomerService;
import com.ordercontrol.domain.model.Customer;

@RestController
@RequestMapping(CustomerController.API_URL)
public class CustomerController {

	public static final String API_URL = "/customers";

	@Autowired
	private ICustomerService customerService;

	@GetMapping
	public List<Customer> getCustomers() {
		return customerService.listAllCustomers();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> getClientById(@PathVariable Long id) {
		return ResponseEntity.ok(customerService.getCustomerById(id));
	}

	@PostMapping
	public ResponseEntity<Long> postClient(@RequestBody CustomerInsertDto customerInsertDto) {
		Long newCustomerId = customerService.saveCustumer(customerInsertDto.convertoToCustumer());
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
