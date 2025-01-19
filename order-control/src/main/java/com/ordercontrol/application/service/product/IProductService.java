package com.ordercontrol.application.service.product;

import org.springframework.data.domain.Pageable;

import com.ordercontrol.domain.model.Product;
import com.ordercontrol.utils.pagination.CustomPageResponse;

public interface IProductService {

	CustomPageResponse<Product> listAllProducts(Pageable pageable);

	Product getProductById(Long productId);

	Long saveProduct(Product product);

}
