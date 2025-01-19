package com.ordercontrol.application.service.customerorder;

import static com.ordercontrol.application.mapper.CustomerOrderMapper.convertToCustomerOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.enums.CustomerOrderStatus;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.infrastructure.repository.ICustomerOrderRepository;
import com.ordercontrol.infrastructure.validator.CustomerOrderInsertValidator;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@Service
public class CustomerOrderService implements ICustomerOrderService {

	@Autowired
	private ICustomerOrderRepository customerOrderRepository;

	@Autowired
	private CustomerOrderInsertValidator validator;

	@Override
	public CustomPageResponse<CustomerOrderResponseDto> listAllCustomerOrders(Pageable pageable) {
		Page<CustomerOrder> customerOrdersPage = customerOrderRepository.findAll(pageable);
		return CustomPageResponse.fromPage(customerOrdersPage, CustomerOrderResponseDto::new);
	}

	@Override
	public CustomerOrderResponseDto getCustomerOrderById(Long customerOrderId) {
		CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"CustomerOrder not found with Id: ".concat(customerOrderId.toString()),
						"Pedido não encontrado. Favor verificar o id fornecido."));
		return new CustomerOrderResponseDto(customerOrder);
	}

	@Override
	public CustomPageResponse<CustomerOrderResponseDto> getOrdersByStatus(String status, Pageable pageable) {
		if (CustomerOrderStatus.valueOf(status) == null) {
			throw new ValidationException("The reported status does not exist.", "O status informado não existe.");
		}
		CustomerOrderStatus orderStatus = CustomerOrderStatus.valueOf(status);
		Page<CustomerOrder> customerOrdersPage = customerOrderRepository.findOrderByStatus(orderStatus, pageable);
		return CustomPageResponse.fromPage(customerOrdersPage, CustomerOrderResponseDto::new);
	}

	@Override
	public Long saveCustumerOrder(CustomerOrderInsertDto customerOrderInsertDto) {
		validator.validate(customerOrderInsertDto);
		CustomerOrder newCustomerOrder = customerOrderRepository.save(convertToCustomerOrder(customerOrderInsertDto));
		return newCustomerOrder.getId();
	}

	@Override
	public CustomerOrderResponseDto updateOrderStatus(Long customerOrderId, String status) {
		if (CustomerOrderStatus.valueOf(status) == null) {
			throw new ValidationException("The reported status does not exist.", "O status informado não existe.");
		}
		CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Order not found with Id: ".concat(customerOrderId.toString()),
						"Pedido não encontrado. Favor verificar o id fornecido."));
		customerOrder.setStatus(CustomerOrderStatus.valueOf(status.toUpperCase()));
		customerOrder = customerOrderRepository.save(customerOrder);
		return new CustomerOrderResponseDto(customerOrder);
	}
}
