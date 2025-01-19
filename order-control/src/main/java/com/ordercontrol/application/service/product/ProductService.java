package com.ordercontrol.application.service.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.Product;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.repository.IProductRepository;
import com.ordercontrol.utils.pagination.CustomPageResponse;

@Service
public class ProductService implements IProductService {

	@Autowired
	private IProductRepository productRepository;

	@Override
	public CustomPageResponse<Product> listAllProducts(Pageable pageable) {
		Page<Product> productsPage = productRepository.findAll(pageable);
		return CustomPageResponse.fromPage(productsPage);
	}

	@Override
	public Product getProductById(Long productId) {
		return productRepository.findById(productId).orElseThrow(
				() -> new ResourceNotFoundException("Product not found with id: ".concat(productId.toString()),
						"Produto não encontrado. Favor verificar o id fornecido."));
	}

	@Override
	public Long saveProduct(Product product) {
		Product newProduct = productRepository.save(product);
		return newProduct.getId();
	}

}
