package com.sjsu.masterproject.client;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sjsu.masterproject.model.Cart;
import com.sjsu.masterproject.model.CartContext;
import com.sjsu.masterproject.model.WishList;
import com.sjsu.masterproject.model.WishListContext;

@Component
public class CheckoutServiceClient extends OnlineStoreServiceClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	private static final String GET_CART_URI = "/checkout/cart/get/";
	private static final String ADD_TO_CART_URI = "/checkout/cart/add";
	private static final String REMOVE_FROM_CART_URI = "/checkout/cart/remove";
	private static final String MERGE_CART_URI = "/checkout/cart/merge/";

	private static final String GET_WISHLIST_URI = "/checkout/wishlist/get/";
	private static final String ADD_TO_WISHLIST_URI = "/checkout/wishlist/add";
	private static final String REMOVE_FROM_WISHLIST_URI = "/checkout/wishlist/remove";

	private static final String PURCHASE_URI = "/checkout/purchase";

	// Service client to get cart for the customer
	public Cart getCartForCustomer(String customerId) throws Exception {
		log.info("getCartForCustomer for {}", customerId);

		if (StringUtils.isBlank(customerId)) {
			throw new Exception("getCartForCustomer: Customer Id is invalid!");
		}

		Cart cart = null;
		String serviceEndPoint = getBaseServiceUrl(env) + GET_CART_URI + customerId + "/";

		log.info("getCartForCustomer: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<Cart> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<Cart>() {
			});
			cart = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting cart for: {}!", customerId, e);
		}

		log.info("Cart:: {}", cart);

		return cart;
	}

	// Service client to add product to cart
	public Cart addToCart(CartContext cartContext) throws Exception {
		log.info("addToCart for {}", cartContext);

		if (cartContext == null) {
			throw new Exception("addToCart: Cart context is invalid!");
		}

		Cart cart = null;
		String serviceEndPoint = getBaseServiceUrl(env) + ADD_TO_CART_URI;

		log.info("addToCart: Service End point: {}", serviceEndPoint);

		try {
			cart = getRestTemplate().postForObject(serviceEndPoint, cartContext, Cart.class);
		} catch (Exception e) {
			log.error("Error while adding to cart for: {}!", cartContext, e);
		}

		log.info("Updated cart:: {}", cart);

		return cart;
	}

	// Service client to remove product from cart
	public Cart removeFromCart(CartContext cartContext) throws Exception {
		log.info("removeFromCart for {}", cartContext);

		if (cartContext == null) {
			throw new Exception("removeFromCart: Cart context is invalid!");
		}

		Cart cart = null;
		String serviceEndPoint = getBaseServiceUrl(env) + REMOVE_FROM_CART_URI;

		log.info("removeFromCart: Service End point: {}", serviceEndPoint);

		try {
			cart = getRestTemplate().postForObject(serviceEndPoint, cartContext, Cart.class);
		} catch (Exception e) {
			log.error("Error while removing {} from cart: {}!", cartContext, e);
		}

		log.info("Updated cart:: {}", cart);

		return cart;
	}

	// Service client to get wishlist for the customer
	public WishList getWishListForCustomer(String customerId) throws Exception {
		log.info("getWishListForCustomer for {}", customerId);

		if (StringUtils.isBlank(customerId)) {
			throw new Exception("getWishListForCustomer: Customer Id is invalid!");
		}

		WishList wishList = null;
		String serviceEndPoint = getBaseServiceUrl(env) + GET_WISHLIST_URI + customerId + "/";

		log.info("getWishListForCustomer: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<WishList> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<WishList>() {
			});
			wishList = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting wishList for: {}!", customerId, e);
		}

		log.info("WishList:: {}", wishList);

		return wishList;
	}

	// Service client to add product to the wishlist
	public WishList addToWishList(WishListContext wishListContext) throws Exception {
		log.info("addToWishList for {}", wishListContext);

		if (wishListContext == null) {
			throw new Exception("addToWishList: WishList context is invalid!");
		}

		WishList wishList = null;
		String serviceEndPoint = getBaseServiceUrl(env) + ADD_TO_WISHLIST_URI;

		log.info("addToWishList: Service End point: {}", serviceEndPoint);

		try {
			wishList = getRestTemplate().postForObject(serviceEndPoint, wishListContext, WishList.class);
		} catch (Exception e) {
			log.error("Error while adding to wish-list for: {}!", wishListContext, e);
		}

		log.info("Updated wishList:: {}", wishList);

		return wishList;
	}

	// Service client to remove product from wishlist
	public WishList removeFromWishList(WishListContext wishListContext) throws Exception {
		log.info("removeFromWishList for {}", wishListContext);

		if (wishListContext == null) {
			throw new Exception("removeFromWishList: WishList context is invalid!");
		}

		WishList wishList = null;
		String serviceEndPoint = getBaseServiceUrl(env) + REMOVE_FROM_WISHLIST_URI;

		log.info("removeFromWishList: Service End point: {}", serviceEndPoint);

		try {
			wishList = getRestTemplate().postForObject(serviceEndPoint, wishListContext, WishList.class);
		} catch (Exception e) {
			log.error("Error while removing {} from wishlist: {}!", wishListContext, e);
		}

		log.info("Updated wishlist:: {}", wishList);

		return wishList;
	}

	// Service client for purchase
	public Boolean purchase(Cart cart) throws Exception {
		log.info("addToCart for {}", cart);

		if (cart == null) {
			throw new Exception("purchase: Cart is invalid!");
		}

		Boolean isSuccessful = false;
		String serviceEndPoint = getBaseServiceUrl(env) + PURCHASE_URI;

		log.info("purchase: Service End point: {}", serviceEndPoint);

		try {
			isSuccessful = getRestTemplate().postForObject(serviceEndPoint, cart, Boolean.class);
		} catch (Exception e) {
			log.error("Error while executing the purchase: {}!", cart, e);
		}

		log.info("Purchase successful:: {}", isSuccessful);
		return isSuccessful;
	}

	public void mergeCarts(Cart anonymousCart, String customerId) {
		log.info("mergeCarts: {} : {}", customerId, anonymousCart);

		if (anonymousCart != null && StringUtils.isNotBlank(customerId)) {
			String serviceEndPoint = getBaseServiceUrl(env) + MERGE_CART_URI + customerId + "/";
			log.info("purchase: Service End point: {}", serviceEndPoint);

			try {
				Cart mergedCart = getRestTemplate().postForObject(serviceEndPoint, anonymousCart, Cart.class);
				log.info("Merged cart: {}", mergedCart);
			} catch (Exception e) {
				log.error("Error while merging the anonymous cart to customer: {}!", customerId, e);
			}
		}
	}
}
