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

import com.ordercontrol.application.dto.products.ProductInsertDto;
import com.ordercontrol.application.service.product.IProductService;
import com.ordercontrol.domain.model.Product;
import com.ordercontrol.utils.pagination.CustomPageResponse;
import com.ordercontrol.utils.pagination.Pagination;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Produtos", description = "Cadastro e consulta de produtos, incluindo o saldo em estoque.")
@RestController
@RequestMapping(ProductController.API_URL)
public class ProductController {

	public static final String API_URL = "/products";

	@Autowired
	private IProductService productService;

	@GetMapping
	@Operation(summary = "Lista os produtos", description = "Retorna os produtos de forma paginada, com o saldo atual em estoque.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Página de produtos retornada")
	})
	public CustomPageResponse<Product> getProducts(@Valid @ModelAttribute Pagination pagination) {
		return productService.listAllProducts(pagination.toPageable());
	}

	@GetMapping("/{productId}")
	@Operation(summary = "Busca um produto por id", description = "Retorna o produto correspondente ao id informado.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Produto encontrado"),
			@ApiResponse(responseCode = "404", description = "Produto não encontrado")
	})
	public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
		return ResponseEntity.ok(productService.getProductById(productId));
	}

	@PostMapping
	@Operation(summary = "Cria um produto", description = "Cadastra um produto e devolve o id gerado.")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "Produto criado")
	})
	public ResponseEntity<Long> postProduct(@RequestBody ProductInsertDto productInsertDto) {
		Long newCustomerId = productService.saveProduct(productInsertDto.convertToProduct());
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
