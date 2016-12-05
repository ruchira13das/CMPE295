package com.sjsu.masterproject.model;

import java.io.Serializable;

public class CartContext implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private Cart cart;
	private ProductContext productForCart;

	public CartContext(String id, Cart cart, ProductContext productForCart) {
		super();
		this.id = id;
		this.cart = cart;
		this.productForCart = productForCart;
	}

	public CartContext() {
		super();
	}

	@Override
	public String toString() {
		return "CartContext [id=" + id + ", cart=" + cart + ", productForCart="
				+ productForCart + "]";
	}

	public boolean isValid() {
		if (productForCart != null && cart != null && cart.isValid()) {
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

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public ProductContext getProductForCart() {
		return productForCart;
	}

	public void setProductForCart(ProductContext productForCart) {
		this.productForCart = productForCart;
	}

}
