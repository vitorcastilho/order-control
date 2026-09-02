package com.ordercontrol.application.service.customerorder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.dto.customerorder.OrderItemInsertDto;
import com.ordercontrol.application.service.product.IProductService;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.domain.model.CustomerOrder;
import com.ordercontrol.domain.model.OrderItem;
import com.ordercontrol.domain.model.Product;
import com.ordercontrol.domain.model.enums.CustomerOrderStatus;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.repository.ICustomerOrderRepository;
import com.ordercontrol.infrastructure.validator.CustomerOrderInsertValidator;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerOrderService")
class CustomerOrderServiceTest {

	@Mock
	private ICustomerOrderRepository customerOrderRepository;

	@Mock
	private IProductService productService;

	@Mock
	private CustomerOrderInsertValidator validator;

	@InjectMocks
	private CustomerOrderService customerOrderService;

	private CustomerOrder order(Long id, String numberOrder, CustomerOrderStatus status) {
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
		order.setId(id);
		order.setNumberOrder(numberOrder);
		order.setCustomer(customer);
		order.setItems(List.of(item));
		order.setTotalOrder(new BigDecimal("20.00"));
		order.setStatus(status);
		return order;
	}

	@Nested
	@DisplayName("consultas")
	class Queries {

		@Test
		@DisplayName("lista pedidos convertendo cada entidade em DTO de resposta")
		void listAllCustomerOrders() {
			Pageable pageable = PageRequest.of(0, 10);
			when(customerOrderRepository.findAll(pageable))
					.thenReturn(new PageImpl<>(List.of(order(1L, "PED-001", CustomerOrderStatus.PENDING)), pageable, 1));

			var response = customerOrderService.listAllCustomerOrders(pageable);

			assertEquals(1, response.getResults().size());
			assertEquals("PED-001", response.getResults().get(0).getNumberOrder());
			assertEquals(1, response.getResultSetMetadata().getCount());
		}

		@Test
		@DisplayName("retorna o pedido quando o id existe")
		void getByIdFound() {
			when(customerOrderRepository.findById(1L))
					.thenReturn(Optional.of(order(1L, "PED-001", CustomerOrderStatus.PENDING)));

			CustomerOrderResponseDto dto = customerOrderService.getCustomerOrderById(1L);

			assertEquals(1L, dto.getId());
			assertEquals("PED-001", dto.getNumberOrder());
			assertEquals(1, dto.getItems().size());
		}

		@Test
		@DisplayName("lança ResourceNotFoundException quando o id não existe")
		void getByIdNotFound() {
			when(customerOrderRepository.findById(99L)).thenReturn(Optional.empty());

			ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
					() -> customerOrderService.getCustomerOrderById(99L));

			assertEquals("CustomerOrder not found with Id: 99", exception.getDeveloperMessage());
		}

		@Test
		@DisplayName("filtra pedidos pelo status informado")
		void getOrdersByStatus() {
			Pageable pageable = PageRequest.of(0, 10);
			when(customerOrderRepository.findOrderByStatus(eq(CustomerOrderStatus.COMPLETED), eq(pageable)))
					.thenReturn(new PageImpl<>(List.of(order(1L, "PED-001", CustomerOrderStatus.COMPLETED)), pageable, 1));

			var response = customerOrderService.getOrdersByStatus("COMPLETED", pageable);

			assertEquals(1, response.getResults().size());
			assertEquals(CustomerOrderStatus.COMPLETED, response.getResults().get(0).getStatus());
		}

		@Test
		@DisplayName("status inexistente interrompe a consulta antes de tocar o repositório")
		void getOrdersByStatusRejectsUnknownStatus() {
			assertThrows(IllegalArgumentException.class,
					() -> customerOrderService.getOrdersByStatus("INEXISTENTE", PageRequest.of(0, 10)));

			verify(customerOrderRepository, never()).findOrderByStatus(any(), any());
		}
	}

	@Nested
	@DisplayName("criação")
	class Creation {

		private CustomerOrderInsertDto insertDto() {
			OrderItemInsertDto item = new OrderItemInsertDto();
			item.setProductId(2L);
			item.setUnitPrice(new BigDecimal("10.00"));
			item.setQuantity(2);

			CustomerOrderInsertDto dto = new CustomerOrderInsertDto();
			dto.setCustomerId(1L);
			dto.setNumberOrder("PED-001");
			dto.setItems(List.of(item));
			return dto;
		}

		@Test
		@DisplayName("valida, persiste e devolve o id gerado")
		void savesAndReturnsId() {
			CustomerOrder saved = order(55L, "PED-001", CustomerOrderStatus.PENDING);
			when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(saved);
			when(productService.getProductById(2L)).thenReturn(saved.getItems().get(0).getProduct());

			Long id = customerOrderService.saveCustumerOrder(insertDto());

			assertEquals(55L, id);
			verify(validator).validate(any(CustomerOrderInsertDto.class));
			verify(customerOrderRepository).save(any(CustomerOrder.class));
		}

		@Test
		@DisplayName("dá baixa no estoque de cada item, na quantidade pedida")
		void decreasesStockForEachItem() {
			CustomerOrder saved = order(55L, "PED-001", CustomerOrderStatus.PENDING);
			Product product = saved.getItems().get(0).getProduct();
			when(customerOrderRepository.save(any(CustomerOrder.class))).thenReturn(saved);
			when(productService.getProductById(2L)).thenReturn(product);

			customerOrderService.saveCustumerOrder(insertDto());

			verify(productService).updateQuantityOfProduct(product, -2);
		}
	}

	@Nested
	@DisplayName("atualização de status")
	class StatusUpdate {

		@Test
		@DisplayName("altera o status e devolve o pedido atualizado")
		void updatesStatus() {
			CustomerOrder existing = order(1L, "PED-001", CustomerOrderStatus.PENDING);
			when(customerOrderRepository.findById(1L)).thenReturn(Optional.of(existing));
			when(customerOrderRepository.save(existing)).thenReturn(existing);

			CustomerOrderResponseDto dto = customerOrderService.updateOrderStatus(1L, "COMPLETED");

			assertEquals(CustomerOrderStatus.COMPLETED, dto.getStatus());
			verify(customerOrderRepository).save(existing);
		}

		@Test
		@DisplayName("status em minúsculas é recusado, embora o método aplique toUpperCase ao gravar")
		void rejectsLowercaseStatusDespiteToUpperCaseOnWrite() {
			assertThrows(IllegalArgumentException.class,
					() -> customerOrderService.updateOrderStatus(1L, "completed"));

			verify(customerOrderRepository, never()).findById(any());
		}

		@Test
		@DisplayName("lança ResourceNotFoundException quando o pedido não existe")
		void orderNotFound() {
			when(customerOrderRepository.findById(99L)).thenReturn(Optional.empty());

			ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
					() -> customerOrderService.updateOrderStatus(99L, "COMPLETED"));

			assertEquals("Order not found with Id: 99", exception.getDeveloperMessage());
		}

		@Test
		@DisplayName("status inexistente interrompe antes de buscar o pedido")
		void rejectsUnknownStatus() {
			assertThrows(IllegalArgumentException.class,
					() -> customerOrderService.updateOrderStatus(1L, "INEXISTENTE"));

			verify(customerOrderRepository, never()).findById(any());
		}
	}

	@Test
	@DisplayName("cacheCustomerOrder devolve a representação do pedido em DTO")
	void cacheCustomerOrder() {
		CustomerOrderResponseDto dto = customerOrderService
				.cacheCustomerOrder(order(3L, "PED-003", CustomerOrderStatus.PROCESSING));

		assertEquals(3L, dto.getId());
		assertEquals("PED-003", dto.getNumberOrder());
	}
}
