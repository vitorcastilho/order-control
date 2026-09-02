package com.ordercontrol.web.api.v1.controller;

import static org.springframework.http.HttpStatus.CREATED;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.application.dto.customerorder.CustomerOrderInsertDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderResponseDto;
import com.ordercontrol.application.dto.customerorder.CustomerOrderStatusUpdateDto;
import com.ordercontrol.application.service.customerorder.ICustomerOrderService;
import com.ordercontrol.utils.pagination.CustomPageResponse;
import com.ordercontrol.utils.pagination.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Pedidos", description = "Criação, consulta e mudança de status dos pedidos. A criação dá baixa no estoque.")
@RestController
@RequestMapping(CustomerOrderController.API_URL)
public class CustomerOrderController {

	public static final String API_URL = "/customer-orders";

	@Autowired
	private ICustomerOrderService customerOrderService;

	@GetMapping
	@Operation(summary = "Lista os pedidos", description = "Retorna os pedidos de forma paginada, com os itens de cada um.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de pedidos retornada")
	})
	public ResponseEntity<CustomPageResponse<CustomerOrderResponseDto>> getCustomerOrders(
			@Valid @ModelAttribute Pagination pagination) {
		return ResponseEntity.ok(customerOrderService.listAllCustomerOrders(pagination.toPageable()));
	}

	@GetMapping("/{customerOrderId}")
	@Operation(summary = "Busca um pedido por id", description = "Resposta servida a partir do cache Redis, com TTL de 10 minutos.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Pedido encontrado"),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado")
	})
	public ResponseEntity<CustomerOrderResponseDto> getCustomerOrderById(@PathVariable Long customerOrderId) {
		return ResponseEntity.ok(customerOrderService.getCustomerOrderById(customerOrderId));
	}

	@GetMapping("/status/{status}")
	@Operation(summary = "Filtra os pedidos por status", description = "Aceita PENDING, PROCESSING, COMPLETED ou CANCELED, em qualquer caixa.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de pedidos retornada"),
			@ApiResponse(responseCode = "400", description = "Status informado não existe")
	})
	public ResponseEntity<CustomPageResponse<CustomerOrderResponseDto>> getOrdersByStatus(@PathVariable String status,
			@Validated @ModelAttribute Pagination pagination) {
		return ResponseEntity.ok(customerOrderService.getOrdersByStatus(status, pagination.toPageable()));
	}

	@PostMapping
	@Operation(summary = "Cria um pedido", description = "Valida cliente, número do pedido e itens, dá baixa no estoque de cada produto e devolve o id gerado. A operação é recusada quando o estoque é insuficiente.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Pedido criado"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos ou estoque insuficiente"),
			@ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
	})
	public ResponseEntity<Long> postCustomerOrder(@RequestBody CustomerOrderInsertDto customerOrderInsertDto) {
		Long newCustomerId = customerOrderService.saveCustumerOrder(customerOrderInsertDto);
		return new ResponseEntity<>(newCustomerId, CREATED);
	}

	@PatchMapping("/{customerOrderId}/status")
	@Operation(summary = "Altera o status de um pedido", description = "Invalida a entrada do pedido no cache.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Status alterado"),
			@ApiResponse(responseCode = "400", description = "Status informado não existe"),
			@ApiResponse(responseCode = "404", description = "Pedido não encontrado")
	})
	public ResponseEntity<CustomerOrderResponseDto> updateCustomerOrderStatus(@PathVariable Long customerOrderId,
			@RequestBody CustomerOrderStatusUpdateDto statusUpdateDto) {
		return ResponseEntity.ok(customerOrderService.updateOrderStatus(customerOrderId, statusUpdateDto.getStatus()));
	}
}
