package com.ordercontrol.web.api.v1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.ordercontrol.application.service.customer.ICustomerService;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@WebMvcTest(CustomerController.class)
@DisplayName("CustomerController")
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ICustomerService customerService;

	private Customer customer() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("Ana");
		customer.setEmail("ana@exemplo.com");
		return customer;
	}

	@Test
	@DisplayName("GET /customers devolve a página de clientes")
	void listCustomers() throws Exception {
		when(customerService.listAllCustomers(any(Pageable.class)))
				.thenReturn(new CustomPageResponse<>(1, 0, 10, List.of(customer())));

		mockMvc.perform(get("/api/v1/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results[0].name").value("Ana"))
				.andExpect(jsonPath("$.resultSetMetadata.count").value(1));
	}

	@Test
	@DisplayName("GET /customers/{id} devolve o cliente")
	void getCustomerById() throws Exception {
		when(customerService.getCustomerById(1L)).thenReturn(customer());

		mockMvc.perform(get("/api/v1/customers/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("ana@exemplo.com"));
	}

	@Test
	@DisplayName("GET /customers/{id} inexistente devolve 404 com a mensagem ao cliente")
	void getCustomerByIdNotFound() throws Exception {
		when(customerService.getCustomerById(99L))
				.thenThrow(new ResourceNotFoundException("dev", "Cliente não encontrado."));

		mockMvc.perform(get("/api/v1/customers/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.clientMessage").value("Cliente não encontrado."));
	}

	@Test
	@DisplayName("POST /customers devolve 201 com o id criado")
	void postCustomer() throws Exception {
		when(customerService.saveCustumer(any(Customer.class))).thenReturn(10L);

		mockMvc.perform(post("/api/v1/customers").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Ana\",\"email\":\"ana@exemplo.com\",\"phone\":\"48999999999\"}"))
				.andExpect(status().isCreated());
	}
}
