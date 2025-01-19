package com.ordercontrol.application.dto.customerorder;

import java.util.List;

public class CustomerOrderInsertDto {

	private String numberOrder;
	private Long customerId;
	private List<OrderItemInsetDto> items;

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

	public List<OrderItemInsetDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemInsetDto> items) {
		this.items = items;
	}
}
