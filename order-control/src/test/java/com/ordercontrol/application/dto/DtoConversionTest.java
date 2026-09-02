package com.ordercontrol.application.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ordercontrol.application.dto.customer.CustomerInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderStatusUpdateDto;
import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.application.dto.customerorder.OrderItemResponseDto;
import com.ordercontrol.application.dto.products.ProductInsertDto;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.OrderItem;
import com.ordercontrol.domain.model.Product;
import com.ordercontrol.domain.model.enums.CustomerOrderStatus;

@DisplayName("Conversão dos DTOs")
class DtoConversionTest {

	@Test
	@DisplayName("CustomerInsertDto vira Customer com os mesmos dados")
	void customerInsertDto() {
		CustomerInsertDto dto = new CustomerInsertDto();
		dto.setName("Vitor");
		dto.setEmail("vitor@exemplo.com");
		dto.setPhone("48999999999");

		Customer customer = dto.convertoToCustumer();

		assertEquals("Vitor", customer.getName());
		assertEquals("vitor@exemplo.com", customer.getEmail());
		assertEquals("48999999999", customer.getPhone());
		assertEquals("Vitor", dto.getName());
		assertEquals("vitor@exemplo.com", dto.getEmail());
		assertEquals("48999999999", dto.getPhone());
	}

	@Test
	@DisplayName("ProductInsertDto vira Product com os mesmos dados")
	void productInsertDto() {
		ProductInsertDto dto = new ProductInsertDto();
		dto.setName("Teclado");
		dto.setDescription("Mecânico");
		dto.setUnits(10);
		dto.setPrice(new BigDecimal("199.90"));

		Product product = dto.convertToProduct();

		assertEquals("Teclado", product.getName());
		assertEquals("Mecânico", product.getDescription());
		assertEquals(10, product.getUnits());
		assertEquals(new BigDecimal("199.90"), product.getPrice());
		assertEquals("Teclado", dto.getName());
		assertEquals("Mecânico", dto.getDescription());
		assertEquals(10, dto.getUnits());
		assertEquals(new BigDecimal("199.90"), dto.getPrice());
	}

	@Test
	@DisplayName("OrderItemInsertDto calcula o total do item na conversão")
	void orderItemInsertDto() {
		OrderItemInsertDto dto = new OrderItemInsertDto();
		dto.setProductId(3L);
		dto.setUnitPrice(new BigDecimal("12.50"));
		dto.setQuantity(4);

		OrderItem item = dto.convertToOrderItem();

		assertEquals(3L, item.getProduct().getId());
		assertEquals(new BigDecimal("12.50"), item.getUnitPrice());
		assertEquals(4, item.getQuantity());
		assertEquals(0, new BigDecimal("50.00").compareTo(item.getTotalItemPrice()));
		assertEquals(3L, dto.getProductId());
	}

	@Test
	@DisplayName("OrderItemResponseDto é construído a partir da entidade")
	void orderItemResponseFromEntity() {
		Product product = new Product();
		product.setId(5L);
		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setUnitPrice(new BigDecimal("2.00"));
		item.setQuantity(3);
		item.setTotalItemPrice(new BigDecimal("6.00"));

		OrderItemResponseDto dto = new OrderItemResponseDto(item);

		assertEquals(5L, dto.getProductId());
		assertEquals(new BigDecimal("2.00"), dto.getUnitPrice());
		assertEquals(3, dto.getQuantity());
		assertEquals(new BigDecimal("6.00"), dto.getTotalItemPrice());
	}

	@Test
	@DisplayName("OrderItemResponseDto expõe os valores atribuídos pelos setters")
	void orderItemResponseAccessors() {
		OrderItemResponseDto dto = new OrderItemResponseDto(1L, BigDecimal.ONE, 1, BigDecimal.ONE);
		dto.setProductId(9L);
		dto.setUnitPrice(new BigDecimal("7.00"));
		dto.setQuantity(2);
		dto.setTotalItemPrice(new BigDecimal("14.00"));

		assertEquals(9L, dto.getProductId());
		assertEquals(new BigDecimal("7.00"), dto.getUnitPrice());
		assertEquals(2, dto.getQuantity());
		assertEquals(new BigDecimal("14.00"), dto.getTotalItemPrice());
		assertNotNull(new OrderItemResponseDto());
	}

	@Test
	@DisplayName("CustomerOrderResponseDto é construído a partir da entidade, incluindo os itens")
	void customerOrderResponseFromEntity() {
		Customer customer = new Customer();
		customer.setId(1L);

		Product product = new Product();
		product.setId(2L);

		OrderItem item = new OrderItem();
		item.setProduct(product);
		item.setUnitPrice(new BigDecimal("10.00"));
		item.setQuantity(2);
		item.setTotalItemPrice(new BigDecimal("20.00"));

		CustomerOrder order = new CustomerOrder();
		order.setId(77L);
		order.setNumberOrder("PED-077");
		order.setCustomer(customer);
		order.setItems(List.of(item));
		order.setTotalOrder(new BigDecimal("20.00"));
		order.setStatus(CustomerOrderStatus.PROCESSING);

		CustomerOrderResponseDto dto = new CustomerOrderResponseDto(order);

		assertEquals(77L, dto.getId());
		assertEquals("PED-077", dto.getNumberOrder());
		assertEquals(1L, dto.getCustomerId());
		assertEquals(new BigDecimal("20.00"), dto.getTotalOrder());
		assertEquals(CustomerOrderStatus.PROCESSING, dto.getStatus());
		assertEquals(1, dto.getItems().size());
		assertEquals(2L, dto.getItems().get(0).getProductId());
	}

	@Test
	@DisplayName("CustomerOrderResponseDto expõe os valores atribuídos pelos setters")
	void customerOrderResponseAccessors() {
		CustomerOrderResponseDto dto = new CustomerOrderResponseDto(1L, "A", 2L, BigDecimal.ONE,
				CustomerOrderStatus.PENDING, List.of());
		dto.setId(5L);
		dto.setNumberOrder("PED-005");
		dto.setCustomerId(6L);
		dto.setTotalOrder(new BigDecimal("30.00"));
		dto.setStatus(CustomerOrderStatus.COMPLETED);
		dto.setItems(List.of(new OrderItemResponseDto()));

		assertEquals(5L, dto.getId());
		assertEquals("PED-005", dto.getNumberOrder());
		assertEquals(6L, dto.getCustomerId());
		assertEquals(new BigDecimal("30.00"), dto.getTotalOrder());
		assertEquals(CustomerOrderStatus.COMPLETED, dto.getStatus());
		assertEquals(1, dto.getItems().size());
		assertNotNull(new CustomerOrderResponseDto());
	}

	@Test
	@DisplayName("CustomerOrderStatusUpdateDto carrega o status informado")
	void statusUpdateDto() {
		CustomerOrderStatusUpdateDto dto = new CustomerOrderStatusUpdateDto();
		dto.setStatus("COMPLETED");

		assertEquals("COMPLETED", dto.getStatus());
	}
}
