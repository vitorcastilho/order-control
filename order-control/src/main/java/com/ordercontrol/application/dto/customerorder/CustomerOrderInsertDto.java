package com.ordercontrol.application.dto.customerorder;

import java.util.List;

public class CustomerOrderInsertDto {

	private String numberOrder;
	private Long customerId;
	private List<OrderItemInsertDto> items;

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

	public List<OrderItemInsertDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemInsertDto> items) {
		this.items = items;
	}
}
