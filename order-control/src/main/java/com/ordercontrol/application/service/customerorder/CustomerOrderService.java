package com.ordercontrol.application.service.customerorder;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.repository.ICustomerOrderRepository;

@Service
public class CustomerOrderService implements ICustomerOrderService {

	@Autowired
	private ICustomerOrderRepository customerOrderRepository;

	@Override
	public List<CustomerOrderResponseDto> listAllCustomerOrders() {
		List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
		return customerOrders.stream().map(CustomerOrderResponseDto::new).collect(Collectors.toList());
	}

	@Override
	public CustomerOrderResponseDto getCustomerOrderById(Long id) {
		CustomerOrder customerOrder = customerOrderRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("CustomerOrder not found with Id: ".concat(id.toString()),
						"Pedido não encontrado. Favor verificar o id fornecido."));
		return new CustomerOrderResponseDto(customerOrder);
	}

	@Override
	public Long saveCustumerOrder(CustomerOrderInsertDto customerOrderInsertDto) {
		CustomerOrder newCustomerOrder = customerOrderRepository.save(customerOrderInsertDto.convertToCustomerOrder());
		return newCustomerOrder.getId();
	}

}
