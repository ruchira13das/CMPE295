package com.sjsu.masterproject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

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
		// Trim to 2 decimal places
		return Math.round(totalValue * 100.00) / 100.00;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public double getTax() {
		// Trim to 2 decimal places
		return Math.round(totalValue * 0.0875 * 100.00) / 100.00;
	}

	public double getTaxIncTotal() {
		return Math.round((getTotalValue() + getTax()) * 100.00) / 100.00;
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
