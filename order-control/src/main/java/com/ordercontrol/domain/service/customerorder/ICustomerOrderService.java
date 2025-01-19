package com.ordercontrol.domain.service.customerorder;

import java.util.List;

import com.ordercontrol.domain.model.CustomerOrder;

public interface ICustomerOrderService {

	List<CustomerOrder> listAllCustomerOrders();

	CustomerOrder getCustomerOrderById(Long id);

	Long saveCustumerOrder(CustomerOrder customerOrder);

}
