package com.ordercontrol.application.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.OrderItem;

@DisplayName("CustomerOrderMapper")
class CustomerOrderMapperTest {

	private OrderItemInsertDto item(long productId, String unitPrice, int quantity) {
		OrderItemInsertDto dto = new OrderItemInsertDto();
		dto.setProductId(productId);
		dto.setUnitPrice(new BigDecimal(unitPrice));
		dto.setQuantity(quantity);
		return dto;
	}

	private CustomerOrderInsertDto order(Long customerId, String numberOrder, List<OrderItemInsertDto> items) {
		CustomerOrderInsertDto dto = new CustomerOrderInsertDto();
		dto.setCustomerId(customerId);
		dto.setNumberOrder(numberOrder);
		dto.setItems(items);
		return dto;
	}

	@Test
	@DisplayName("copia número do pedido e id do cliente para a entidade")
	void mapsHeaderFields() {
		CustomerOrder result = CustomerOrderMapper
				.convertToCustomerOrder(order(9L, "PED-001", List.of(item(1L, "10.00", 1))));

		assertEquals("PED-001", result.getNumberOrder());
		assertNotNull(result.getCustomer());
		assertEquals(9L, result.getCustomer().getId());
	}

	@Test
	@DisplayName("soma preço unitário vezes quantidade de todos os itens no total do pedido")
	void calculatesTotalFromItems() {
		CustomerOrder result = CustomerOrderMapper.convertToCustomerOrder(
				order(1L, "PED-002", List.of(item(1L, "10.00", 2), item(2L, "5.50", 4))));

		assertEquals(0, new BigDecimal("42.00").compareTo(result.getTotalOrder()));
	}

	@Test
	@DisplayName("liga cada item ao pedido que o contém")
	void bindsItemsBackToOrder() {
		CustomerOrder result = CustomerOrderMapper
				.convertToCustomerOrder(order(1L, "PED-003", List.of(item(7L, "3.00", 5))));

		assertEquals(1, result.getItems().size());
		OrderItem orderItem = result.getItems().get(0);
		assertSame(result, orderItem.getCustomerOrder());
		assertEquals(7L, orderItem.getProduct().getId());
		assertEquals(5, orderItem.getQuantity());
		assertEquals(0, new BigDecimal("15.00").compareTo(orderItem.getTotalItemPrice()));
	}

	@Test
	@DisplayName("quando não há itens, deixa a lista nula e o total zerado")
	void handlesNullItems() {
		CustomerOrder result = CustomerOrderMapper.convertToCustomerOrder(order(1L, "PED-004", null));

		assertNull(result.getItems());
		assertEquals(BigDecimal.ZERO, result.getTotalOrder());
	}

	@Test
	@DisplayName("lista de itens vazia resulta em total zerado")
	void handlesEmptyItems() {
		CustomerOrder result = CustomerOrderMapper.convertToCustomerOrder(order(1L, "PED-005", List.of()));

		assertEquals(0, result.getItems().size());
		assertEquals(0, BigDecimal.ZERO.compareTo(result.getTotalOrder()));
	}

	@Test
	@DisplayName("a classe utilitária pode ser instanciada sem efeito colateral")
	void constructorIsAccessible() {
		new CustomerOrderMapper();
	}
}
