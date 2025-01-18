package com.ordercontrol.infrastructure.api.v1.controller.customer.dto;

import com.ordercontrol.domain.model.Customer;

public class CustomerInsertDto {

	private String name;
	private String email;
	private String phone;
	
	public Customer convertoToCustumer() {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setEmail(email);
		customer.setPhone(phone);
		return customer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
