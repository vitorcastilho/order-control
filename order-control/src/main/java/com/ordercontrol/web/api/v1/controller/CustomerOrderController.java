package com.ordercontrol.web.api.v1.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderStatusUpdateDto;
import com.ordercontrol.application.service.customerorder.ICustomerOrderService;
import com.ordercontrol.utils.pagination.CustomPageResponse;
import com.ordercontrol.utils.pagination.Pagination;

import jakarta.validation.Valid;

@RestController
@RequestMapping(CustomerOrderController.API_URL)
public class CustomerOrderController {

	public static final String API_URL = "/customer-orders";

	@Autowired
	private ICustomerOrderService customerOrderService;

	@GetMapping
	public ResponseEntity<CustomPageResponse<CustomerOrderResponseDto>> getCustomerOrders(
			@Valid @ModelAttribute Pagination pagination) {
		return ResponseEntity.ok(customerOrderService.listAllCustomerOrders(pagination.toPageable()));
	}

	@GetMapping("/{customerOrderId}")
	public ResponseEntity<CustomerOrderResponseDto> getCustomerOrderById(@PathVariable Long customerOrderId) {
		return ResponseEntity.ok(customerOrderService.getCustomerOrderById(customerOrderId));
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<CustomPageResponse<CustomerOrderResponseDto>> getOrdersByStatus(@PathVariable String status,
			@Validated @ModelAttribute Pagination pagination) {
		return ResponseEntity.ok(customerOrderService.getOrdersByStatus(status, pagination.toPageable()));
	}

	@PostMapping
	public ResponseEntity<Long> postCustomerOrder(@RequestBody CustomerOrderInsertDto customerOrderInsertDto) {
		Long newCustomerId = customerOrderService.saveCustumerOrder(customerOrderInsertDto);
		return new ResponseEntity<>(newCustomerId, CREATED);
	}

	@PatchMapping("/{customerOrderId}/status")
	public ResponseEntity<CustomerOrderResponseDto> updateCustomerOrderStatus(@PathVariable Long customerOrderId,
			@RequestBody CustomerOrderStatusUpdateDto statusUpdateDto) {
		return ResponseEntity.ok(customerOrderService.updateOrderStatus(customerOrderId, statusUpdateDto.getStatus()));
	}
}
