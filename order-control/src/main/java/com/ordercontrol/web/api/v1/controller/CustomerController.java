package com.ordercontrol.web.api.v1.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.application.dto.customer.CustomerInsertDto;
import com.ordercontrol.application.service.customer.ICustomerService;
import com.ordercontrol.domain.model.Customer;
import com.ordercontrol.utils.pagination.CustomPageResponse;
import com.ordercontrol.utils.pagination.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "Cadastro e consulta de clientes.")
@RestController
@RequestMapping(CustomerController.API_URL)
public class CustomerController {

	public static final String API_URL = "/customers";

	@Autowired
	private ICustomerService customerService;

	@GetMapping
	@Operation(summary = "Lista os clientes", description = "Retorna os clientes de forma paginada. Use offset, limit e sortBy para navegar.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de clientes retornada")
	})
	public CustomPageResponse<Customer> getCustomers(@Valid @ModelAttribute Pagination pagination) {
		return customerService.listAllCustomers(pagination.toPageable());
	}

	@GetMapping("/{customerId}")
	@Operation(summary = "Busca um cliente por id", description = "Retorna o cliente correspondente ao id informado.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	})
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long customerId) {
		return ResponseEntity.ok(customerService.getCustomerById(customerId));
	}

	@PostMapping
	@Operation(summary = "Cria um cliente", description = "Cadastra um cliente e devolve o id gerado.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Cliente criado")
	})
	public ResponseEntity<Long> postCustomer(@RequestBody CustomerInsertDto customerInsertDto) {
		Long newCustomerId = customerService.saveCustumer(customerInsertDto.convertoToCustumer());
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
