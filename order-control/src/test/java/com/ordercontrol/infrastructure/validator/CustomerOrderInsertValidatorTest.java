package com.ordercontrol.infrastructure.validator;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.infrastructure.repository.ICustomerOrderRepository;
import com.ordercontrol.infrastructure.repository.ICustomerRepository;
import com.ordercontrol.infrastructure.repository.IProductRepository;

@ExtendWith(MockitoExtension.class)
class CustomerOrderInsertValidatorTest {

	private static final String NUMBER_ORDER = "ABC";
	private static final Long CUSTOMER_ID = 1L;
	private static final Long PRODUCT_ID = 1L;

	@InjectMocks
	private CustomerOrderInsertValidator Validator;

	@Mock
	private ICustomerRepository customerRepository;
	@Mock
	private ICustomerOrderRepository customerOrderRepository;

	@Mock
	private IProductRepository productRepository;

	@Mock
	private ItemInsertValidator itemValidator;

	@Test
	void shouldNotThrowAnyException_whenCustomerOrderInsertDtoIsOk() {
		Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customerMock()));
		Mockito.doNothing().when(itemValidator).validate(Mockito.any());
		Validator.validate(customerOrderInsertDtoMock());
	}

	@Test
	void shouldThrowValidationException_whenCustomerIdIsNull() {
		CustomerOrderInsertDto customerOrderInsertDto = customerOrderInsertDtoMock();
		customerOrderInsertDto.setCustomerId(null);
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			Validator.validate(customerOrderInsertDto);
		});
		Assertions.assertEquals("The customer is mandatory information.", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenCustomerIdNotExist() {
		Mockito.when(customerRepository.findById(any())).thenReturn(Optional.empty());
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			Validator.validate(customerOrderInsertDtoMock());
		});
		Assertions.assertEquals("Customer not found with Id: 1", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenNumberOrderIsEmpty() {
		Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customerMock()));
		CustomerOrderInsertDto customerOrderInsertDto = customerOrderInsertDtoMock();
		customerOrderInsertDto.setNumberOrder("");
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			Validator.validate(customerOrderInsertDto);
		});
		Assertions.assertEquals("The numberOrder is mandatory information.", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenNumberOrderIsNull() {
		Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customerMock()));
		CustomerOrderInsertDto customerOrderInsertDto = customerOrderInsertDtoMock();
		customerOrderInsertDto.setNumberOrder(null);
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			Validator.validate(customerOrderInsertDto);
		});
		Assertions.assertEquals("The numberOrder is mandatory information.", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenNumberOrderAlreadyExists() {
		Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customerMock()));
		Mockito.when(customerOrderRepository.existsByNumberOrder(any())).thenReturn(true);
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			Validator.validate(customerOrderInsertDtoMock());
		});
		Assertions.assertEquals("Number Order already exists: ABC", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenItemIsEmpty() {
		Mockito.when(customerRepository.findById(any())).thenReturn(Optional.of(customerMock()));
		CustomerOrderInsertDto customerOrderInsertDto = customerOrderInsertDtoMock();
		customerOrderInsertDto.setItems(new ArrayList<>());
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			Validator.validate(customerOrderInsertDto);
		});
		Assertions.assertEquals("Items cannot be empty. At least one item must be added to customer order.",
				exception.getMessage());
	}

	private CustomerOrderInsertDto customerOrderInsertDtoMock() {
		CustomerOrderInsertDto customerOrderInsertDto = new CustomerOrderInsertDto();
		customerOrderInsertDto.setNumberOrder(NUMBER_ORDER);
		customerOrderInsertDto.setCustomerId(CUSTOMER_ID);
		customerOrderInsertDto.setItems(orderItemInsertDtoMock());
		return customerOrderInsertDto;
	}

	private Customer customerMock() {
		Customer customer = new Customer();
		customer.setId(CUSTOMER_ID);
		return customer;
	}

	private List<OrderItemInsertDto> orderItemInsertDtoMock() {
		List<OrderItemInsertDto> orderItems = new ArrayList<>();
		OrderItemInsertDto orderItemDto = orderItemDtoMock();
		orderItems.add(orderItemDto);
		return orderItems;
	}

	private OrderItemInsertDto orderItemDtoMock() {
		OrderItemInsertDto orderItemDto = new OrderItemInsertDto();
		orderItemDto.setProductId(PRODUCT_ID);
		orderItemDto.setUnitPrice(BigDecimal.valueOf(1.59));
		orderItemDto.setQuantity(10);
		return orderItemDto;
	}
}
