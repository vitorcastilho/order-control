package com.ordercontrol.infrastructure.api.v1.controller.client;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.domain.service.ICustomerService;
import com.ordercontrol.infrastructure.api.v1.controller.client.dto.CustomerDto;

@RestController
@RequestMapping(CustomerController.API_URL)
public class CustomerController {

	public static final String API_URL = "/customers";

	@Autowired
	private ICustomerService customerService;

	@PostMapping
	public ResponseEntity<Long> postClient(@RequestBody CustomerDto customerDto) {
		Long newCustomerId = customerService.saveCustumer(customerDto.convertoToCustumer());
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
