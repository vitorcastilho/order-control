package com.ordercontrol.application.service.customerorder;

import java.util.List;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;

public interface ICustomerOrderService {

	List<CustomerOrderResponseDto> listAllCustomerOrders();

	CustomerOrderResponseDto getCustomerOrderById(Long id);

	Long saveCustumerOrder(CustomerOrderInsertDto customerOrderInsertDto);

}
