package com.ordercontrol.web.api.v1.controller;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordercontrol.application.dto.products.ProductInsertDto;
import com.ordercontrol.application.service.product.IProductService;
import com.ordercontrol.domain.model.Product;

@RestController
@RequestMapping(ProductController.API_URL)
public class ProductController {

	public static final String API_URL = "/products";

	@Autowired
	private IProductService productService;

	@GetMapping
	public List<Product> getCustomers() {
		return productService.listAllProducts();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> getClientById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@PostMapping
	public ResponseEntity<Long> postClient(@RequestBody ProductInsertDto productInsertDto) {
		Long newCustomerId = productService.saveProduct(productInsertDto.convertToProduct());
		return new ResponseEntity<>(newCustomerId, CREATED);
	}
}
