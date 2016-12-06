package com.sjsu.masterproject.model;

import java.io.Serializable;

public class Purchase implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private Cart cart;
	private String purchaseDate;

	public Purchase(String id, Cart cart, String purchaseDate) {
		super();
		this.id = id;
		this.cart = cart;
		this.purchaseDate = purchaseDate;
	}

	public Purchase() {
		super();
	}

	@Override
	public String toString() {
		return "Purchase [id=" + id + ", cart=" + cart + ", purchaseDate=" + purchaseDate + "]";
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

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
}
