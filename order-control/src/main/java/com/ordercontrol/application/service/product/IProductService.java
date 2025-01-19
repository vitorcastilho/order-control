package com.ordercontrol.application.service.product;

import java.util.List;

import com.ordercontrol.domain.model.Product;

public interface IProductService {

	List<Product> listAllProducts();

	Product getProductById(Long id);

	Long saveProduct(Product product);

}
