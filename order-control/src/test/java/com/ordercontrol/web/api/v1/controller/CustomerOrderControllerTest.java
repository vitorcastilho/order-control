package com.ordercontrol.web.api.v1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.dto.customerorder.OrderItemResponseDto;
import com.ordercontrol.application.service.customerorder.ICustomerOrderService;
import com.ordercontrol.domain.model.enums.CustomerOrderStatus;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@WebMvcTest(CustomerOrderController.class)
@DisplayName("CustomerOrderController")
class CustomerOrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ICustomerOrderService customerOrderService;

	private CustomerOrderResponseDto responseDto() {
		return new CustomerOrderResponseDto(1L, "PED-001", 2L, new BigDecimal("20.00"),
				CustomerOrderStatus.PENDING,
				List.of(new OrderItemResponseDto(3L, new BigDecimal("10.00"), 2, new BigDecimal("20.00"))));
	}

	@Test
	@DisplayName("GET /customer-orders devolve a página de pedidos")
	void listOrders() throws Exception {
		when(customerOrderService.listAllCustomerOrders(any(Pageable.class)))
				.thenReturn(new CustomPageResponse<>(1, 0, 10, List.of(responseDto())));

		mockMvc.perform(get("/api/v1/customer-orders"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results[0].numberOrder").value("PED-001"))
				.andExpect(jsonPath("$.results[0].items[0].productId").value(3));
	}

	@Test
	@DisplayName("GET /customer-orders/{id} devolve o pedido")
	void getOrderById() throws Exception {
		when(customerOrderService.getCustomerOrderById(1L)).thenReturn(responseDto());

		mockMvc.perform(get("/api/v1/customer-orders/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalOrder").value(20.00));
	}

	@Test
	@DisplayName("GET /customer-orders/{id} inexistente devolve 404")
	void getOrderByIdNotFound() throws Exception {
		when(customerOrderService.getCustomerOrderById(99L))
				.thenThrow(new ResourceNotFoundException("dev", "Pedido não encontrado."));

		mockMvc.perform(get("/api/v1/customer-orders/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.clientMessage").value("Pedido não encontrado."));
	}

	@Test
	@DisplayName("GET /customer-orders/status/{status} filtra pelo status")
	void getOrdersByStatus() throws Exception {
		when(customerOrderService.getOrdersByStatus(eq("PENDING"), any(Pageable.class)))
				.thenReturn(new CustomPageResponse<>(1, 0, 10, List.of(responseDto())));

		mockMvc.perform(get("/api/v1/customer-orders/status/PENDING"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results[0].status").value("PENDING"));
	}

	@Test
	@DisplayName("POST /customer-orders devolve 201 com o id criado")
	void postOrder() throws Exception {
		when(customerOrderService.saveCustumerOrder(any(CustomerOrderInsertDto.class))).thenReturn(55L);

		mockMvc.perform(post("/api/v1/customer-orders").contentType(MediaType.APPLICATION_JSON)
				.content("{\"customerId\":1,\"numberOrder\":\"PED-001\","
						+ "\"items\":[{\"productId\":3,\"unitPrice\":10.00,\"quantity\":2}]}"))
				.andExpect(status().isCreated());
	}

	@Test
	@DisplayName("POST /customer-orders com dados inválidos devolve 400 com a mensagem ao cliente")
	void postOrderValidationError() throws Exception {
		when(customerOrderService.saveCustumerOrder(any(CustomerOrderInsertDto.class)))
				.thenThrow(new ValidationException("dev", "Ao menos um item deve ser adicionado ao pedido."));

		mockMvc.perform(post("/api/v1/customer-orders").contentType(MediaType.APPLICATION_JSON)
				.content("{\"customerId\":1,\"numberOrder\":\"PED-001\",\"items\":[]}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.clientMessage").value("Ao menos um item deve ser adicionado ao pedido."));
	}

	@Test
	@DisplayName("PATCH /customer-orders/{id}/status devolve o pedido com o novo status")
	void patchStatus() throws Exception {
		when(customerOrderService.updateOrderStatus(eq(1L), eq("COMPLETED")))
				.thenReturn(new CustomerOrderResponseDto(1L, "PED-001", 2L, new BigDecimal("20.00"),
						CustomerOrderStatus.COMPLETED, List.of()));

		mockMvc.perform(patch("/api/v1/customer-orders/1/status").contentType(MediaType.APPLICATION_JSON)
				.content("{\"status\":\"COMPLETED\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("COMPLETED"));
	}
}
