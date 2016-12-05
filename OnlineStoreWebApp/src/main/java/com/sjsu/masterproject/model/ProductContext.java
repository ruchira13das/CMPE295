package com.sjsu.masterproject.model;

public class ProductContext {

	private Product product;
	private int quantity;

	public ProductContext() {
		super();
	}

	public ProductContext(Product product, int quantity) {
		super();
		this.product = product;
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ProductContext [product=" + product + ", quantity=" + quantity + "]";
	}

	public double getTotal() {
		// Trim to 2 decimal places
		return Math.round(product.getPrice() * quantity * 100.00) / 100.00;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
