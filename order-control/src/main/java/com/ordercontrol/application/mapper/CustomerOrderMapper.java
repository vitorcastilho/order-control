package com.ordercontrol.application.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.OrderItem;

public class CustomerOrderMapper {

	public static CustomerOrder convertToCustomerOrder(CustomerOrderInsertDto customerDto) {
		CustomerOrder customerOrder = new CustomerOrder();
		customerOrder.setNumberOrder(customerDto.getNumberOrder());
		Customer customer = new Customer();
		customer.setId(customerDto.getCustomerId());
		customerOrder.setCustomerId(customer);
		customerOrder
				.setItems(customerDto.getItems() != null ? customerDto.getItems().stream().map(orderItemInsertDto -> {
					OrderItem orderItem = orderItemInsertDto.convertToOrderItem();
					orderItem.setCustomerOrderId(customerOrder);
					return orderItem;
				}).collect(Collectors.toList()) : null);
		customerOrder.setTotalOrder(customerDto.getItems() != null ? customerDto.getItems().stream()
				.map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO);
		return customerOrder;
	}
}
