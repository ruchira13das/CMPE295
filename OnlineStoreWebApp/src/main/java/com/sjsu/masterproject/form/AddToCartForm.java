package com.sjsu.masterproject.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
public class AddToCartForm {

	private String productId;

	@NotNull(message = "Invalid quantity")
	@Min(1)
	private int quantity;

	public AddToCartForm() {
		super();
	}

	public AddToCartForm(String productId, int quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
