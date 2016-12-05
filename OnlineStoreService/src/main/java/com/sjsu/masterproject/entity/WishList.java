package com.sjsu.masterproject.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;

public class WishList implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Customer customer;
	private List<Product> products;

	public WishList() {
		super();
	}

	public WishList(String id, Customer customer, List<Product> products) {
		super();
		this.id = id;
		this.customer = customer;
		this.products = products;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", customer=" + customer + ", products=" + products + "]";
	}

	public boolean isValid() {
		if (customer != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}
}
