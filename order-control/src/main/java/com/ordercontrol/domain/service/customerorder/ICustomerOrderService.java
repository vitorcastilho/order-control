package com.ordercontrol.domain.service.customerorder;

import java.util.List;

import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.infrastructure.api.v1.controller.customerorder.dto.CustomerOrderResponseDto;

public interface ICustomerOrderService {

	List<CustomerOrderResponseDto> listAllCustomerOrders();

	CustomerOrderResponseDto getCustomerOrderById(Long id);

	Long saveCustumerOrder(CustomerOrder customerOrder);

}
