package com.ordercontrol.web.api.v1.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import com.ordercontrol.application.service.product.IProductService;
import com.ordercontrol.domain.model.Product;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController")
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private IProductService productService;

	private Product product() {
		Product product = new Product();
		product.setId(1L);
		product.setName("Teclado");
		product.setUnits(5);
		product.setPrice(new BigDecimal("199.90"));
		return product;
	}

	@Test
	@DisplayName("GET /products devolve a página de produtos")
	void listProducts() throws Exception {
		when(productService.listAllProducts(any(Pageable.class)))
				.thenReturn(new CustomPageResponse<>(1, 0, 10, List.of(product())));

		mockMvc.perform(get("/api/v1/products"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.results[0].name").value("Teclado"));
	}

	@Test
	@DisplayName("GET /products/{id} devolve o produto")
	void getProductById() throws Exception {
		when(productService.getProductById(1L)).thenReturn(product());

		mockMvc.perform(get("/api/v1/products/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.units").value(5));
	}

	@Test
	@DisplayName("POST /products devolve 201 com o id criado")
	void postProduct() throws Exception {
		when(productService.saveProduct(any(Product.class))).thenReturn(9L);

		mockMvc.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"Mouse\",\"description\":\"Sem fio\",\"units\":3,\"price\":99.90}"))
				.andExpect(status().isCreated());
	}
}
