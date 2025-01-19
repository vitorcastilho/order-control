package com.ordercontrol.application.dto.customerorder;

import java.math.BigDecimal;

import com.ordercontrol.domain.model.OrderItem;
import com.ordercontrol.domain.model.Product;

public class OrderItemInsertDto {

	private Long productId;
	private BigDecimal unitPrice;
	private Integer quantity;

	public OrderItem convertToOrderItem() {
		OrderItem orderItem = new OrderItem();
		Product product = new Product();
		product.setId(productId);
		orderItem.setProductId(product);
		orderItem.setUnitPrice(unitPrice);
		orderItem.setQuantity(quantity);
		orderItem.setTotalItemPrice(this.unitPrice.multiply(BigDecimal.valueOf(this.quantity)));
		return orderItem;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
