package com.ordercontrol.application.service.customerorder;

import static com.ordercontrol.application.mapper.CustomerOrderMapper.convertToCustomerOrder;
import static com.ordercontrol.utils.TextUtils.isEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.service.product.IProductService;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.OrderItem;
import com.ordercontrol.domain.model.Product;
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
	private IProductService productService;

	@Autowired
	private CustomerOrderInsertValidator validator;

	@Override
	public CustomPageResponse<CustomerOrderResponseDto> listAllCustomerOrders(Pageable pageable) {
		Page<CustomerOrder> customerOrdersPage = customerOrderRepository.findAll(pageable);
		customerOrdersPage.forEach(order -> cacheCustomerOrder(order));
		return CustomPageResponse.fromPage(customerOrdersPage, CustomerOrderResponseDto::new);
	}

	@Override
	@Cacheable(value = "customerOrders", key = "#customerOrderId")
	public CustomerOrderResponseDto getCustomerOrderById(Long customerOrderId) {
		CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"CustomerOrder not found with Id: ".concat(customerOrderId.toString()),
						"Pedido não encontrado. Favor verificar o id fornecido."));
		return new CustomerOrderResponseDto(customerOrder);
	}

	@Override
	public CustomPageResponse<CustomerOrderResponseDto> getOrdersByStatus(String status, Pageable pageable) {
		CustomerOrderStatus orderStatus = parseStatus(status);
		Page<CustomerOrder> customerOrdersPage = customerOrderRepository.findOrderByStatus(orderStatus, pageable);
		customerOrdersPage.forEach(order -> cacheCustomerOrder(order));
		return CustomPageResponse.fromPage(customerOrdersPage, CustomerOrderResponseDto::new);
	}

	@Override
	public Long saveCustumerOrder(CustomerOrderInsertDto customerOrderInsertDto) {
		validator.validate(customerOrderInsertDto);
		CustomerOrder newCustomerOrder = customerOrderRepository.save(convertToCustomerOrder(customerOrderInsertDto));
		updateQuantityOfLeastRequestedProduct(newCustomerOrder);
		return newCustomerOrder.getId();
	}

	@Override
	@CacheEvict(value = "customerOrders", key = "#customerOrderId")
	public CustomerOrderResponseDto updateOrderStatus(Long customerOrderId, String status) {
		CustomerOrderStatus newStatus = parseStatus(status);
		CustomerOrder customerOrder = customerOrderRepository.findById(customerOrderId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Order not found with Id: ".concat(customerOrderId.toString()),
						"Pedido não encontrado. Favor verificar o id fornecido."));
		customerOrder.setStatus(newStatus);
		customerOrder = customerOrderRepository.save(customerOrder);
		return new CustomerOrderResponseDto(customerOrder);
	}

	private CustomerOrderStatus parseStatus(String status) {
		if (isEmpty(status)) {
			throw new ValidationException("The status is mandatory information.",
					"O status é uma informação obrigatória.");
		}
		try {
			return CustomerOrderStatus.valueOf(status.trim().toUpperCase());
		} catch (IllegalArgumentException exception) {
			throw new ValidationException("The reported status does not exist: ".concat(status),
					"O status informado não existe.");
		}
	}

	private void updateQuantityOfLeastRequestedProduct(CustomerOrder newCustomerOrder) {
		for (OrderItem orderItem : newCustomerOrder.getItems()) {
			Product product = productService.getProductById(orderItem.getProduct().getId());
			Integer quantityToRemove = -orderItem.getQuantity();
			productService.updateQuantityOfProduct(product, quantityToRemove);

		}
	}

	@CachePut(value = "customerOrders", key = "#customerOrder.id")
	public CustomerOrderResponseDto cacheCustomerOrder(CustomerOrder customerOrder) {
		return new CustomerOrderResponseDto(customerOrder);
	}
}
