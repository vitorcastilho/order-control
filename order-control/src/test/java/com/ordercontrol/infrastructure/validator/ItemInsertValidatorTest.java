package com.ordercontrol.infrastructure.validator;

import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.domain.model.Product;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.infrastructure.repository.IProductRepository;

@ExtendWith(MockitoExtension.class)
class ItemInsertValidatorTest {

	private static final Long PRODUCT_ID = 1L;

	@InjectMocks
	private ItemInsertValidator itemInsertValidator;

	@Mock
	private IProductRepository productRepository;

	@Test
	void shouldNotThrowAnyException_whenOrderItemInsertDtoIsOk() {
		Mockito.when(productRepository.findById(any())).thenReturn(Optional.of(productMock()));
		itemInsertValidator.validate(orderItemInsertDtoMock());
	}

	@Test
	void shouldThrowValidationException_whenProductIdIsNull() {
		OrderItemInsertDto orderItemDto = orderItemInsertDtoMock();
		orderItemDto.setProductId(null);
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			itemInsertValidator.validate(orderItemDto);
		});
		Assertions.assertEquals("The productId is mandatory information.", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenProductDoesNotExist() {
		Mockito.when(productRepository.findById(any())).thenReturn(Optional.empty());
		ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			itemInsertValidator.validate(orderItemInsertDtoMock());
		});
		Assertions.assertEquals("Product not found with Id: 1", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenUnitPriceIsNegative() {
		Mockito.when(productRepository.findById(any())).thenReturn(Optional.of(productMock()));
		OrderItemInsertDto orderItemDto = orderItemInsertDtoMock();
		orderItemDto.setUnitPrice(BigDecimal.valueOf(-1.0));
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			itemInsertValidator.validate(orderItemDto);
		});
		Assertions.assertEquals("The unitPrice cannot be a negative number or zero: -1.0", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenQuantityIsNegative() {
		Mockito.when(productRepository.findById(any())).thenReturn(Optional.of(productMock()));
		OrderItemInsertDto orderItemDto = orderItemInsertDtoMock();
		orderItemDto.setQuantity(-1);
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			itemInsertValidator.validate(orderItemDto);
		});
		Assertions.assertEquals("The quantity cannot be a negative number  or zero: -1", exception.getMessage());
	}

	@Test
	void shouldThrowValidationException_whenQuantityExceedsStock() {
		OrderItemInsertDto orderItemDto = orderItemInsertDtoMock();
		orderItemDto.setQuantity(15);
		Mockito.when(productRepository.findById(any())).thenReturn(Optional.of(productMock()));
		ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
			itemInsertValidator.validate(orderItemDto);
		});
		Assertions.assertEquals(
				"The quantity requested exceeds the quantity available in stock. The quantity available for product id 1 is: 10",
				exception.getMessage());
	}

	private OrderItemInsertDto orderItemInsertDtoMock() {
		OrderItemInsertDto orderItemDto = new OrderItemInsertDto();
		orderItemDto.setProductId(PRODUCT_ID);
		orderItemDto.setUnitPrice(BigDecimal.valueOf(1.59));
		orderItemDto.setQuantity(10);
		return orderItemDto;
	}

	private Product productMock() {
		Product product = new Product();
		product.setId(PRODUCT_ID);
		product.setName("Produto A");
		product.setDescription("Descrição");
		product.setUnits(10);
		product.setPrice(BigDecimal.valueOf(5.50));
		return product;
	}
}