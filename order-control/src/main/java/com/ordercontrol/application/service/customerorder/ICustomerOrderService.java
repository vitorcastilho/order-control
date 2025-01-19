package com.ordercontrol.application.service.customerorder;

import org.springframework.data.domain.Pageable;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.utils.pagination.CustomPageResponse;

public interface ICustomerOrderService {

	CustomPageResponse<CustomerOrderResponseDto> listAllCustomerOrders(Pageable pageable);

	CustomerOrderResponseDto getCustomerOrderById(Long customerOrderId);

	Long saveCustumerOrder(CustomerOrderInsertDto customerOrderInsertDto);

	CustomerOrderResponseDto updateOrderStatus(Long customerOrderId, String status);

	CustomPageResponse<CustomerOrderResponseDto> getOrdersByStatus(String status, Pageable pageable);
}
