package com.ordercontrol.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ordercontrol.domain.model.enums.CustomerOrderStatus;

@DisplayName("Entidades de domínio")
class DomainModelTest {

	@Test
	@DisplayName("Customer expõe os valores atribuídos")
	void customer() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("Ana");
		customer.setEmail("ana@exemplo.com");
		customer.setPhone("48999999999");

		assertEquals(1L, customer.getId());
		assertEquals("Ana", customer.getName());
		assertEquals("ana@exemplo.com", customer.getEmail());
		assertEquals("48999999999", customer.getPhone());
	}

	@Test
	@DisplayName("Product expõe os valores atribuídos")
	void product() {
		Product product = new Product();
		product.setId(2L);
		product.setName("Teclado");
		product.setDescription("Mecânico");
		product.setUnits(5);
		product.setPrice(new BigDecimal("199.90"));

		assertEquals(2L, product.getId());
		assertEquals("Teclado", product.getName());
		assertEquals("Mecânico", product.getDescription());
		assertEquals(5, product.getUnits());
		assertEquals(new BigDecimal("199.90"), product.getPrice());
	}

	@Test
	@DisplayName("CustomerOrder nasce com status PENDING")
	void customerOrderDefaultsToPending() {
		assertEquals(CustomerOrderStatus.PENDING, new CustomerOrder().getStatus());
	}

	@Test
	@DisplayName("CustomerOrder expõe os valores atribuídos")
	void customerOrder() {
		Customer customer = new Customer();
		OrderItem item = new OrderItem();

		CustomerOrder order = new CustomerOrder();
		order.setId(3L);
		order.setNumberOrder("PED-003");
		order.setCustomer(customer);
		order.setItems(List.of(item));
		order.setTotalOrder(new BigDecimal("50.00"));
		order.setStatus(CustomerOrderStatus.CANCELED);

		assertEquals(3L, order.getId());
		assertEquals("PED-003", order.getNumberOrder());
		assertSame(customer, order.getCustomer());
		assertEquals(1, order.getItems().size());
		assertEquals(new BigDecimal("50.00"), order.getTotalOrder());
		assertEquals(CustomerOrderStatus.CANCELED, order.getStatus());
	}

	@Test
	@DisplayName("OrderItem expõe os valores atribuídos")
	void orderItem() {
		CustomerOrder order = new CustomerOrder();
		Product product = new Product();

		OrderItem item = new OrderItem();
		item.setId(4L);
		item.setCustomerOrder(order);
		item.setProduct(product);
		item.setUnitPrice(new BigDecimal("10.00"));
		item.setQuantity(2);
		item.setTotalItemPrice(new BigDecimal("20.00"));

		assertEquals(4L, item.getId());
		assertSame(order, item.getCustomerOrder());
		assertSame(product, item.getProduct());
		assertEquals(new BigDecimal("10.00"), item.getUnitPrice());
		assertEquals(2, item.getQuantity());
		assertEquals(new BigDecimal("20.00"), item.getTotalItemPrice());
	}

	@Test
	@DisplayName("CustomerOrderStatus tem os quatro estados do fluxo de pedido")
	void statusValues() {
		assertEquals(4, CustomerOrderStatus.values().length);
		assertEquals(CustomerOrderStatus.PENDING, CustomerOrderStatus.valueOf("PENDING"));
		assertEquals(CustomerOrderStatus.PROCESSING, CustomerOrderStatus.valueOf("PROCESSING"));
		assertEquals(CustomerOrderStatus.COMPLETED, CustomerOrderStatus.valueOf("COMPLETED"));
		assertEquals(CustomerOrderStatus.CANCELED, CustomerOrderStatus.valueOf("CANCELED"));
	}
}
