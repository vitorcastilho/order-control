package com.ordercontrol.domain.model;

import static com.ordercontrol.domain.model.enums.CustomerOrderStatus.PENDING;
import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;

import com.ordercontrol.domain.model.enums.CustomerOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_order")
public class CustomerOrder {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(name = "number_order", nullable = false, unique = true, length = 50)
	private String numberOrder;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@OneToMany(mappedBy = "customerOrderId", cascade = ALL, orphanRemoval = true)
	private List<OrderItem> items;

	@Column(name = "total_order", nullable = false, precision = 10, scale = 2)
	private BigDecimal totalOrder;

	@Enumerated(STRING)
	@Column(name = "status", nullable = false)
	private CustomerOrderStatus status = PENDING;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumberOrder() {
		return numberOrder;
	}

	public void setNumberOrder(String numberOrder) {
		this.numberOrder = numberOrder;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public BigDecimal getTotalOrder() {
		return totalOrder;
	}

	public void setTotalOrder(BigDecimal totalOrder) {
		this.totalOrder = totalOrder;
	}

	public CustomerOrderStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerOrderStatus status) {
		this.status = status;
	}
}
