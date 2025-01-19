package com.ordercontrol.domain.service.customerorder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.repository.ICustomerOrderRepository;
import com.ordercontrol.exception.ResourceNotFoundException;

@Service
public class CustomerOrderService implements ICustomerOrderService {

	@Autowired
	private ICustomerOrderRepository customerOrderRepository;

	@Override
	public List<CustomerOrder> listAllCustomerOrders() {
		return customerOrderRepository.findAll();
	}

	@Override
	public CustomerOrder getCustomerOrderById(Long id) {
		return customerOrderRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("CustomerOrder not found with Id: ".concat(id.toString()),
						"Pedido não encontrado. Favor verificar o id fornecido."));
	}

	@Override
	public Long saveCustumerOrder(CustomerOrder customerOrder) {
		CustomerOrder newCustomerOrder = customerOrderRepository.save(customerOrder);
		return newCustomerOrder.getId();
	}

}
