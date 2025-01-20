package com.ordercontrol.application.dto.customerorder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.enums.CustomerOrderStatus;

public class CustomerOrderResponseDto {

	private Long id;
	private String numberOrder;
	private Long customerId;
	private BigDecimal totalOrder;
	private CustomerOrderStatus status;
	private List<OrderItemResponseDto> items;

	public CustomerOrderResponseDto(CustomerOrder customerOrder) {
		this.id = customerOrder.getId();
		this.numberOrder = customerOrder.getNumberOrder();
		this.customerId = customerOrder.getCustomer().getId();
		this.totalOrder = customerOrder.getTotalOrder();
		this.status = customerOrder.getStatus();
		this.items = customerOrder.getItems().stream().map(OrderItemResponseDto::new).collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public CustomerOrderStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerOrderStatus status) {
		this.status = status;
	}

	public List<OrderItemResponseDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponseDto> items) {
		this.items = items;
	}
}
