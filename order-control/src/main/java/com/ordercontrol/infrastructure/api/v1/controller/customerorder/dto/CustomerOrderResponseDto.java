package com.ordercontrol.infrastructure.api.v1.controller.customerorder.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.ordercontrol.domain.model.CustomerOrder;

public class CustomerOrderResponseDto {

	private String numberOrder;
	private Long customerId;
	private BigDecimal totalOrder;
	private List<OrderItemResponseDto> items;

	public CustomerOrderResponseDto(CustomerOrder customerOrder) {
		this.numberOrder = customerOrder.getNumberOrder();
		this.customerId = customerOrder.getCustomerId().getId();
		this.totalOrder = customerOrder.getTotalOrder();
		this.items = customerOrder.getItems().stream().map(OrderItemResponseDto::new).collect(Collectors.toList());
	}

	public String getNumberOrder() {
		return numberOrder;
	}

	public void setNumberOrder(String numberOrder) {
		this.numberOrder = numberOrder;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(BigDecimal totalOrder) {
		this.totalOrder = totalOrder;
	}

	public List<OrderItemResponseDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponseDto> items) {
		this.items = items;
	}
}
