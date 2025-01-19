package com.ordercontrol.application.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ordercontrol.domain.model.Product;
import com.ordercontrol.infrastructure.exception.ResourceNotFoundException;
import com.ordercontrol.infrastructure.repository.IProductRepository;

@Service
public class ProductService implements IProductService {

	@Autowired
	private IProductRepository productRepository;

	@Override
	public List<Product> listAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: ".concat(id.toString()),
						"Produto não encontrado. Favor verificar o id fornecido."));
	}

	@Override
	public Long saveProduct(Product product) {
		Product newProduct = productRepository.save(product);
		return newProduct.getId();
	}

}
