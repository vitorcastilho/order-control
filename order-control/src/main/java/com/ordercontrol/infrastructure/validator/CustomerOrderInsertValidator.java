package com.ordercontrol.infrastructure.validator;

import static com.ordercontrol.utils.NumberUtils.isInvalidId;
import static com.ordercontrol.utils.TextUtils.isEmpty;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.infrastructure.repository.ICustomerOrderRepository;
import com.ordercontrol.infrastructure.repository.ICustomerRepository;

@Component
public class CustomerOrderInsertValidator {

	@Autowired
	private ICustomerRepository customerRepository;

	@Autowired
	ICustomerOrderRepository customerOrderRepository;

	@Autowired
	private ItemInsertValidator itemValidator;

	public void validate(CustomerOrderInsertDto customerDto) {
		validateCustomerId(customerDto.getCustomerId());
		validateNumberOrder(customerDto.getNumberOrder());
		validateItems(customerDto.getItems());
	}

	private void validateCustomerId(Long customerId) {
		if (isInvalidId(customerId)) {
			throw new ValidationException("The customer is mandatory information.",
					"O cliente é uma informação obrigatória.");
		}
		validateIfCustomerExists(customerId);
	}

	private void validateIfCustomerExists(Long customerId) {
		if (!customerRepository.findById(customerId).isPresent()) {
			throw new ResourceNotFoundException("Customer not found with Id: ".concat(customerId.toString()),
					"Cliente não encontrado. Favor verificar o id fornecido.");
		}
	}

	private void validateNumberOrder(String numberOrder) {
		if (isEmpty(numberOrder)) {
			throw new ValidationException("The numberOrder is mandatory information.",
					"O número da ordem é uma informação obrigatória.");
		}
		if (customerOrderRepository.existsByNumberOrder(numberOrder)) {
			throw new ValidationException("Number Order already exists: ".concat(numberOrder),
					"Número do pedido já existe. Favor verificar o número do pedido fornecido.");
		}
	}

	private void validateItems(List<OrderItemInsertDto> items) {
		if (items.size() < 1) {
			throw new ValidationException("Items cannot be empty. At least one item must be added to customer order.",
					"Ao menos um item deve ser adicionado ao pedido.");
		}
		for (OrderItemInsertDto itemDto : items) {
			itemValidator.validate(itemDto);
		}
	}
}
