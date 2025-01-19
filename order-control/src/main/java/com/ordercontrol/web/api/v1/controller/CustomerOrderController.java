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

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.service.customerorder.ICustomerOrderService;

@RestController
@RequestMapping(CustomerOrderController.API_URL)
public class CustomerOrderController {

	public static final String API_URL = "/customer-orders";

	@Autowired
	private ICustomerOrderService customerOrderService;

	@GetMapping
	public List<CustomerOrderResponseDto> getCustomers() {
		return customerOrderService.listAllCustomerOrders();
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerOrderResponseDto> getClientById(@PathVariable Long id) {
		return ResponseEntity.ok(customerOrderService.getCustomerOrderById(id));
	}

	@PostMapping
	public ResponseEntity<Long> postClient(@RequestBody CustomerOrderInsertDto customerOrderInsertDto) {
		Long newCustomerId = customerOrderService.saveCustumerOrder(customerOrderInsertDto);
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
