package com.sjsu.masterproject.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class WishListContext implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private WishList wishList;
	private Product productForWishList;

	public WishListContext() {
		super();
	}

	public WishListContext(String id, WishList wishList, Product productForWishList) {
		super();
		this.id = id;
		this.wishList = wishList;
		this.productForWishList = productForWishList;
	}

	@Override
	public String toString() {
		return "WishListContext [id=" + id + ", cart=" + wishList + ", productForWishList="
				+ productForWishList + "]";
	}

	public boolean isValid() {
		if (productForWishList != null && wishList != null && wishList.isValid()) {
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

	public WishList getWishList() {
		return wishList;
	}

	public void setCart(WishList wishList) {
		this.wishList = wishList;
	}

	public Product getProductForWishList() {
		return productForWishList;
	}

	public void setProductForWishList(Product productForWishList) {
		this.productForWishList = productForWishList;
	}

}
