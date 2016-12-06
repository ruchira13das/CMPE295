package com.sjsu.masterproject.service;

import com.sjsu.masterproject.entity.Cart;
import com.sjsu.masterproject.entity.CartContext;
import com.sjsu.masterproject.entity.WishList;
import com.sjsu.masterproject.entity.WishListContext;

public interface CheckoutService {

	public Cart addToCart(CartContext cartContext) throws Exception;

	public Cart removeFromCart(CartContext cartContext) throws Exception;

	public Cart getCart(String customerId) throws Exception;

	public WishList addToWishList(WishListContext wishListContext) throws Exception;

	public WishList getWishList(String customerId) throws Exception;

	public WishList removeFromWishList(WishListContext wishListContext) throws Exception;

	public boolean purchase(Cart cart) throws Exception;

	public Cart mergeCarts(Cart anonymousCart, String customerId) throws Exception;

}
