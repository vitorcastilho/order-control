package com.ordercontrol.application.service.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ordercontrol.domain.model.Product;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.exception.ValidationException;
import com.ordercontrol.infrastructure.repository.IProductRepository;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService")
class ProductServiceTest {

	@Mock
	private IProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private Product product(Long id, String name, Integer units) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setUnits(units);
		product.setPrice(new BigDecimal("10.00"));
		return product;
	}

	@Test
	@DisplayName("lista produtos paginados preservando os metadados da página")
	void listAllProducts() {
		Pageable pageable = PageRequest.of(0, 10);
		when(productRepository.findAll(pageable))
				.thenReturn(new PageImpl<>(List.of(product(1L, "Teclado", 5)), pageable, 1));

		CustomPageResponse<Product> response = productService.listAllProducts(pageable);

		assertEquals(1, response.getResults().size());
		assertEquals("Teclado", response.getResults().get(0).getName());
	}

	@Test
	@DisplayName("retorna o produto quando o id existe")
	void getProductByIdFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product(1L, "Teclado", 5)));

		assertEquals("Teclado", productService.getProductById(1L).getName());
	}

	@Test
	@DisplayName("lança ResourceNotFoundException quando o id não existe")
	void getProductByIdNotFound() {
		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> productService.getProductById(99L));

		assertEquals("Product not found with id: 99", exception.getDeveloperMessage());
	}

	@Test
	@DisplayName("ao salvar, devolve o id gerado pelo repositório")
	void saveProductReturnsGeneratedId() {
		when(productRepository.save(any(Product.class))).thenReturn(product(7L, "Mouse", 3));

		assertEquals(7L, productService.saveProduct(product(null, "Mouse", 3)));
	}

	@Nested
	@DisplayName("updateQuantityOfProduct")
	class StockUpdate {

		@Test
		@DisplayName("quantidade positiva aumenta o estoque")
		void increasesStock() {
			Product product = product(1L, "Teclado", 5);

			productService.updateQuantityOfProduct(product, 3);

			ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
			verify(productRepository).save(captor.capture());
			assertEquals(8, captor.getValue().getUnits());
		}

		@Test
		@DisplayName("quantidade negativa diminui o estoque")
		void decreasesStock() {
			Product product = product(1L, "Teclado", 5);

			productService.updateQuantityOfProduct(product, -2);

			ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
			verify(productRepository).save(captor.capture());
			assertEquals(3, captor.getValue().getUnits());
		}

		@Test
		@DisplayName("permite zerar o estoque quando a baixa é igual ao disponível")
		void allowsExactDepletion() {
			Product product = product(1L, "Teclado", 5);

			productService.updateQuantityOfProduct(product, -5);

			assertEquals(0, product.getUnits());
			verify(productRepository).save(product);
		}

		@Test
		@DisplayName("recusa a baixa e não persiste quando o estoque é insuficiente")
		void rejectsInsufficientStock() {
			Product product = product(1L, "Teclado", 2);

			ValidationException exception = assertThrows(ValidationException.class,
					() -> productService.updateQuantityOfProduct(product, -3));

			assertEquals("Not enough stock to decrease for product: 1", exception.getDeveloperMessage());
			assertEquals("Estoque insuficiente para diminuir o produto: Teclado", exception.getClientMessage());
			assertEquals(2, product.getUnits());
			verify(productRepository, never()).save(any(Product.class));
		}

		@Test
		@DisplayName("quantidade zero é tratada como aumento e mantém o estoque")
		void zeroKeepsStock() {
			Product product = product(1L, "Teclado", 5);

			productService.updateQuantityOfProduct(product, 0);

			assertEquals(5, product.getUnits());
			verify(productRepository).save(product);
		}
	}
}
