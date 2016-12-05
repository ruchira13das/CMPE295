package com.sjsu.masterproject.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Cart implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private Customer customer;
	private List<ProductContext> productsInCart;
	private double totalValue;

	public Cart() {
		super();
	}

	public Cart(String id, Customer customer, List<ProductContext> productsInCart, double totalValue) {
		super();
		this.id = id;
		this.customer = customer;
		this.productsInCart = productsInCart;
		this.totalValue = totalValue;
	}

	public boolean isValid() {
		if (customer != null) {
			return true;
		} else {
			return false;
		}
	}

	public void calculateTotal() {
		totalValue = 0.0;
		if (productsInCart != null && productsInCart.size() > 0) {
			for (ProductContext product : productsInCart) {
				this.totalValue = this.totalValue + product.getProduct().getPrice() * product.getQuantity();
			}
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

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", customer=" + customer + ", productsInCart=" + productsInCart + ", totalValue="
				+ totalValue + "]";
	}

	public List<ProductContext> getProductsInCart() {
		return productsInCart;
	}

	public void setProductsInCart(List<ProductContext> productsInCart) {
		this.productsInCart = productsInCart;
	}

	public List<Product> extractProductsInCart() {
		List<Product> products = new ArrayList<>();
		for (ProductContext productContext :  productsInCart) {
			products.add(productContext.getProduct());
		}

		return products;
	}

}
