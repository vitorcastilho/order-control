package com.ordercontrol.application.dto.customerorder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.OrderItem;

public class CustomerOrderInsertDto {

	private String numberOrder;
	private Long customerId;
	private List<OrderItemInsetDto> items;

	public CustomerOrder convertToCustomerOrder() {
		CustomerOrder customerOrder = new CustomerOrder();
		customerOrder.setNumberOrder(numberOrder);
		Customer customer = new Customer();
		customer.setId(customerId);
		customerOrder.setCustomerId(customer);
		customerOrder.setItems(this.items != null ? this.items.stream().map(orderItemInsertDto -> {
			OrderItem orderItem = orderItemInsertDto.convertToOrderItem();
			orderItem.setCustomerOrderId(customerOrder);
			return orderItem;
		}).collect(Collectors.toList()) : null);
		customerOrder.setTotalOrder(this.items != null
                ? this.items.stream()
                    .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                : BigDecimal.ZERO);
		return customerOrder;
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

	public List<OrderItemInsetDto> getItems() {
		return items;
	}

	public void setItems(List<OrderItemInsetDto> items) {
		this.items = items;
	}
}
