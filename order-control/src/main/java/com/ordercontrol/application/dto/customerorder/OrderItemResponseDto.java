package com.ordercontrol.application.dto.customerorder;

import java.math.BigDecimal;

import com.ordercontrol.domain.model.OrderItem;

public class OrderItemResponseDto {

	private Long productId;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal totalItemPrice;

	public OrderItemResponseDto(OrderItem orderItem) {
        this.productId = orderItem.getProductId().getId();
        this.unitPrice = orderItem.getUnitPrice();
        this.quantity = orderItem.getQuantity();
        this.totalItemPrice = orderItem.getTotalItemPrice();
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

	public BigDecimal getTotalItemPrice() {
		return totalItemPrice;
	}

	public void setTotalItemPrice(BigDecimal totalItemPrice) {
		this.totalItemPrice = totalItemPrice;
	}
}
