package com.ordercontrol.infrastructure.api.v1.controller.products.dto;

import java.math.BigDecimal;

import com.ordercontrol.domain.model.Product;

public class ProductInsertDto {

	private String name;
	private String description;
	private Integer units;
	private BigDecimal price;

	public Product convertToProduct() {
		Product product = new Product();
		product.setName(name);
		product.setDescription(description);
		product.setUnits(units);
		product.setPrice(price);
		return product;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
