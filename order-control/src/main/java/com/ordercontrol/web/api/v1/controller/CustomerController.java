package com.ordercontrol.web.api.v1.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.application.dto.customer.CustomerInsertDto;
import com.ordercontrol.application.service.customer.ICustomerService;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.utils.pagination.CustomPageResponse;
import com.ordercontrol.utils.pagination.Pagination;

import jakarta.validation.Valid;

@RestController
@RequestMapping(CustomerController.API_URL)
public class CustomerController {

	public static final String API_URL = "/customers";

	@Autowired
	private ICustomerService customerService;

	@GetMapping
	public CustomPageResponse<Customer> getCustomers(@Valid @ModelAttribute Pagination pagination) {
		return customerService.listAllCustomers(pagination.toPageable());
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
		return ResponseEntity.ok(customerService.getCustomerById(customerId));
	}

	@PostMapping
	public ResponseEntity<Long> postCustomer(@RequestBody CustomerInsertDto customerInsertDto) {
		Long newCustomerId = customerService.saveCustumer(customerInsertDto.convertoToCustumer());
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
