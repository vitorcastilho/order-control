package com.ordercontrol.application.service.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.repository.ICustomerRepository;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService")
class CustomerServiceTest {

	@Mock
	private ICustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;

	private Customer customer(Long id, String name) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setName(name);
		return customer;
	}

	@Test
	@DisplayName("lista clientes paginados preservando os metadados da página")
	void listAllCustomers() {
		Pageable pageable = PageRequest.of(0, 10);
		when(customerRepository.findAll(pageable))
				.thenReturn(new PageImpl<>(List.of(customer(1L, "Ana")), pageable, 1));

		CustomPageResponse<Customer> response = customerService.listAllCustomers(pageable);

		assertEquals(1, response.getResults().size());
		assertEquals("Ana", response.getResults().get(0).getName());
		assertEquals(1, response.getResultSetMetadata().getCount());
	}

	@Test
	@DisplayName("retorna o cliente quando o id existe")
	void getCustomerByIdFound() {
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer(1L, "Ana")));

		assertEquals("Ana", customerService.getCustomerById(1L).getName());
	}

	@Test
	@DisplayName("lança ResourceNotFoundException quando o id não existe")
	void getCustomerByIdNotFound() {
		when(customerRepository.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> customerService.getCustomerById(99L));

		assertEquals("Customer not found with Id: 99", exception.getDeveloperMessage());
		assertEquals("Cliente não encontrado. Favor verificar o id fornecido.", exception.getClientMessage());
	}

	@Test
	@DisplayName("ao salvar, devolve o id gerado pelo repositório")
	void saveCustomerReturnsGeneratedId() {
		when(customerRepository.save(any(Customer.class))).thenReturn(customer(42L, "Ana"));

		assertEquals(42L, customerService.saveCustumer(customer(null, "Ana")));
		verify(customerRepository).save(any(Customer.class));
	}
}
