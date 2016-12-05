package com.sjsu.masterproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sjsu.masterproject.entity.Cart;
import com.sjsu.masterproject.entity.CartContext;
import com.sjsu.masterproject.entity.WishList;
import com.sjsu.masterproject.entity.WishListContext;
import com.sjsu.masterproject.service.CheckoutService;

@BaseRestController
public class CheckoutController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CheckoutService checkoutService;

	@RequestMapping(value = "/checkout/cart/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Cart addToCart(@RequestBody CartContext cartContext) throws Exception {
		log.info("addToCart. CartContext: {}", cartContext.toString());
		return checkoutService.addToCart(cartContext);
	}

	@RequestMapping(value = "/checkout/cart/get/{customer_id}/", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Cart getCart(@PathVariable("customer_id") String customerId) throws Exception {
		log.info("Get cart for customer: {}", customerId);
		return checkoutService.getCart(customerId);
	}

	@RequestMapping(value = "/checkout/cart/remove", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Cart removeFromCart(@RequestBody CartContext cartContext) throws Exception {
		log.info("removeFromCart. CartContext: {}", cartContext.toString());
		return checkoutService.removeFromCart(cartContext);
	}

	@RequestMapping(value = "/checkout/wishlist/add", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public WishList addToWishList(@RequestBody WishListContext wishListContext) throws Exception {
		log.info("addToWishList. Product: {}", wishListContext.toString());
		return checkoutService.addToWishList(wishListContext);
	}

	@RequestMapping(value = "/checkout/wishlist/get/{customer_id}/", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public WishList getWishList(@PathVariable("customer_id") String customerId) throws Exception {
		log.info("Get cart for customer: {}", customerId);
		return checkoutService.getWishList(customerId);
	}

	@RequestMapping(value = "/checkout/wishlist/remove", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public WishList removeFromWishList(@RequestBody WishListContext wishListContext) throws Exception {
		log.info("removeFromWishList: WishListContext: {}", wishListContext.toString());
		return checkoutService.removeFromWishList(wishListContext);
	}

	@RequestMapping(value = "/checkout/cart/merge/{customer_id}/", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Cart mergeCarts(@RequestBody Cart cart, @PathVariable("customer_id") String customerId) throws Exception {
		log.info("Get cart for customer: {}", customerId);
		return checkoutService.mergeCarts(cart, customerId);
	}

	@RequestMapping(value = "/checkout/purchase", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean purchase(@RequestBody Cart cart) throws Exception {
		log.info("Checking out cart: {}", cart.toString());
		return checkoutService.purchase(cart);
	}

}
