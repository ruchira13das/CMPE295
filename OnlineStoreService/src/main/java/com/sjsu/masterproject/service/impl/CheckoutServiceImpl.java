package com.sjsu.masterproject.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sjsu.masterproject.controller.exception.ServiceException;
import com.sjsu.masterproject.entity.Cart;
import com.sjsu.masterproject.entity.CartContext;
import com.sjsu.masterproject.entity.Customer;
import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.entity.ProductContext;
import com.sjsu.masterproject.entity.Purchase;
import com.sjsu.masterproject.entity.WishList;
import com.sjsu.masterproject.entity.WishListContext;
import com.sjsu.masterproject.repository.CartRepository;
import com.sjsu.masterproject.repository.PurchaseRepository;
import com.sjsu.masterproject.repository.WishListRepository;
import com.sjsu.masterproject.service.CheckoutService;
import com.sjsu.masterproject.service.CustomerManagementService;
import com.sjsu.masterproject.service.RecommendationsService;
import com.sjsu.masterproject.util.Constants;
import com.sjsu.masterproject.util.Util;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private RecommendationsService recommendationsService;

	@Autowired
	private CustomerManagementService customerService;

	@Override
	public Cart addToCart(CartContext cartContext) throws Exception {
		if (!cartContext.isValid()) {
			throw new ServiceException("Invalid request. No valid product/cart found for this operation!");
		}

		// Get the DB snapshot of the cart to merge
		Cart cart = getCart(cartContext.getCart().getCustomer().getId());

		// Add to Cart
		List<ProductContext> productsInCart = cart.getProductsInCart() != null ? cart.getProductsInCart()
				: new ArrayList<ProductContext>();

		// Update cart items
		boolean isMerged = false;
		for (ProductContext productContext : productsInCart) {
			if (cartContext.getProductForCart().getProduct().getId().equals(productContext.getProduct().getId())) {
				// Product already added in cart. Just update the quantity
				productContext
				.setQuantity(productContext.getQuantity() + cartContext.getProductForCart().getQuantity());
				isMerged = true;
			}
		}

		// New product. No merging. Add to cart.
		if (!isMerged) {
			productsInCart.add(cartContext.getProductForCart());
		}

		cart.setProductsInCart(productsInCart);
		cart.calculateTotal();

		Cart udpatedCart = null;
		try {
			// Save to DB
			udpatedCart = cartRepository.save(cart);
		} finally {
			// Log recommendation activity
			List<Product> productList = new ArrayList<Product>();
			productList.add(cartContext.getProductForCart().getProduct());
			Util.logRecommendationActivity(recommendationsService, productList,
					udpatedCart.getCustomer().getIdAsLongString(), Constants.RECOMMENDATION_PREFERENCE_CART);
		}

		return udpatedCart;
	}

	@Override
	public Cart getCart(String customerId) throws Exception {
		Cart cart = cartRepository.findByCustomerId(customerId);
		if (cart == null) {
			log.info("Cart not found in DB for customer id: {}", customerId);
			// Not found in DB. Create a new one.
			cart = new Cart();

			Customer customer = customerService.getCustomer(customerId);
			if (customer == null) {
				// Not a registered customer. Create a pseudo customer for cart
				customer = new Customer();
				customer.setId(customerId);
			}

			cart.setCustomer(customer);
			cart.setProductsInCart(new ArrayList<>());
			cart.calculateTotal();

			// Save it to the DB
			cart = cartRepository.save(cart);
		}

		return cart;
	}

	@Override
	public WishList addToWishList(WishListContext wishListContext) throws Exception {
		if (!wishListContext.isValid()) {
			throw new ServiceException("Invalid request. No valid product/wish-list found for this operation!");
		}

		// Get the DB snapshot
		WishList wishList = getWishList(wishListContext.getWishList().getCustomer().getId());

		// Add to Wishlist
		List<Product> productList = wishList.getProducts() != null ? wishList.getProducts() : new ArrayList<>();

		// Check if it is already included in the wishlist
		boolean isIncluded = false;
		for (Product product : productList) {
			if (product.getId().equals(wishListContext.getProductForWishList().getId())) {
				isIncluded = true;
				break;
			}
		}

		if (!isIncluded) {
			productList.add(wishListContext.getProductForWishList());
		}

		wishList.setProducts(productList);

		WishList updatedWishList = null;
		try {
			// Save it to the DB
			updatedWishList = wishListRepository.save(wishList);
		} finally {
			// Log recommendation activity
			productList = new ArrayList<Product>();
			productList.add(wishListContext.getProductForWishList());
			Util.logRecommendationActivity(recommendationsService, productList,
					updatedWishList.getCustomer().getIdAsLongString(), Constants.RECOMMENDATION_PREFERENCE_WISHLIST);
		}

		return updatedWishList;
	}

	@Override
	public WishList getWishList(String customerId) throws Exception {
		log.info("getWishList for customer: {}", customerId);
		if (StringUtils.isBlank(customerId)) {
			// Customer is null. Cannot get wishlist.
			throw new ServiceException("Invalid request. No valid customer for getWishList!");
		}

		WishList wishList = wishListRepository.findByCustomerId(customerId);
		if (wishList == null) {
			log.info("Wishlist not found in DB for customer id: {}", customerId);

			// Not found in DB. Create a new one.
			wishList = new WishList();
			Customer customer = customerService.getCustomer(customerId);
			if (customer == null) {
				// Not a registered customer. Create a pseudo customer for cart
				customer = new Customer();
				customer.setId(customerId);
			}
			wishList.setCustomer(customer);
			wishList.setProducts(new ArrayList<Product>());

			// Save it to the DB
			wishList = wishListRepository.save(wishList);
		}

		return wishList;
	}

	@Override
	public boolean purchase(Cart cart) throws Exception {
		log.info("checkout cart: {}", cart);
		if (cart == null || !cart.isValid() || cart.getProductsInCart() == null
				|| cart.getProductsInCart().size() <= 0) {
			throw new ServiceException("Invalid request. The cart is invalid to complete the purchase!");
		}

		// Ensure the cart total is correct
		cart.calculateTotal();

		Purchase purchase = new Purchase();
		purchase.setCart(cart);
		purchase.setPurchaseDate(String.valueOf(System.currentTimeMillis()));

		try {
			// Save the purchase
			log.info("Saving purchase!");
			purchaseRepository.save(purchase);

			// Update the customer for the new purchase
			log.info("Updating customer record!");
			customerService.updateCustomer(updateCustomerPurchaseList(cart));

			// Now drop the cart from DB
			log.info("Deleting the cart!");
			cartRepository.delete(cart);
		} catch (Exception e) {
			log.error("Error in purchase!", e);
		} finally {
			// Log the recommendation activity
			Util.logRecommendationActivity(recommendationsService, cart.extractProductsInCart(),
					cart.getCustomer().getIdAsLongString(), Constants.RECOMMENDATION_PREFERENCE_PURCHASE);
		}

		return true;
	}

	private Customer updateCustomerPurchaseList(Cart cart) throws Exception {

		// Get the DB snapshot of the customer
		Customer customer = customerService.getCustomer(cart.getCustomer().getId());

		// Get the products list from cart
		List<Product> productsInCart = cart.extractProductsInCart();
		if (productsInCart != null) {
			List<String> productsPurchased = customer.getProductsPurchased();
			if (productsPurchased == null) {
				productsPurchased = new ArrayList<String>();
			}

			for (Product product : productsInCart) {
				productsPurchased.add(product.getId());
			}

			// Update customer record for the purchase
			customer.setProductsPurchased(productsPurchased);
		}

		return customer;
	}

	@Override
	public Cart removeFromCart(CartContext cartContext) throws Exception {
		if (!cartContext.isValid()) {
			throw new ServiceException(
					"Invalid request for removeFromCart. No valid product/cart found for this operation!");
		}

		log.info("removeFromCart: customer: {}", cartContext.getCart().getCustomer().getId());

		// Get the DB snapshot of the cart to merge
		Cart cart = getCart(cartContext.getCart().getCustomer().getId());

		log.info("removeFromCart: DB cart: {}", cart);

		// Add to Cart
		List<ProductContext> productsInCart = cart.getProductsInCart() != null ? cart.getProductsInCart()
				: new ArrayList<ProductContext>();

		List<ProductContext> productsInCartTrimmed = new ArrayList<>();
		productsInCartTrimmed.addAll(productsInCart);

		// Update cart items
		for (ProductContext productContext : productsInCart) {
			if (cartContext.getProductForCart().getProduct().getId().equals(productContext.getProduct().getId())) {
				log.info("Match found.....");
				// Remove the product from the list
				productsInCartTrimmed.remove(productContext);
			}
		}

		log.info("Products in Cart:: {}", productsInCartTrimmed);

		cart.setProductsInCart(productsInCartTrimmed);
		cart.calculateTotal();

		return cartRepository.save(cart);
	}

	@Override
	public WishList removeFromWishList(WishListContext wishListContext) throws Exception {
		if (!wishListContext.isValid()) {
			throw new ServiceException(
					"Invalid request for removeFromWishList. No valid product/wishlist found for this operation!");
		}

		log.info("removeFromCart: customer: {}", wishListContext.getWishList().getCustomer());

		// Get the DB snapshot of the wishlist to merge
		WishList wishList = getWishList(wishListContext.getWishList().getCustomer().getId());

		log.info("removeFromCart: DB wishlist: {}", wishList);

		// Add to Cart
		List<Product> productsInWishList = wishList.getProducts() != null ? wishList.getProducts()
				: new ArrayList<Product>();

		List<Product> productsInWishListTrimmed = new ArrayList<>();
		productsInWishListTrimmed.addAll(productsInWishList);

		// Update wishlist items
		for (Product product : productsInWishList) {
			if (wishListContext.getProductForWishList().getId().equals(product.getId())) {
				log.info("Match found.....");
				// Remove the product from the list
				productsInWishListTrimmed.remove(product);
			}
		}

		log.info("Products in WishList:: {}", productsInWishListTrimmed);
		wishList.setProducts(productsInWishListTrimmed);

		return wishListRepository.save(wishList);
	}

	@Override
	public Cart mergeCarts(Cart cart, String customerId) throws Exception {
		log.info("mergeCarts: {}", customerId);

		// Get the customer
		Customer customer = customerService.getCustomer(customerId);

		// Get the cart for the customer - customerId
		Cart masterCart = cartRepository.findByCustomerId(customerId);

		// Declare the merged cart
		Cart mergedCart = new Cart();

		if (masterCart == null) {
			log.info("No cart found in DB for customer: {}. Assign the other cart to the customer.", customerId);
			mergedCart.setCustomer(customer);
			mergedCart.setProductsInCart(cart.getProductsInCart());
			mergedCart.calculateTotal();
		} else {
			// Merge cart to masterCart
			mergedCart.setCustomer(customer);
			List<ProductContext> mergedProductContexts = new ArrayList<>();

			for (ProductContext productContext : cart.getProductsInCart()) {
				ProductContext matchingMasterProductContext = findMatchingProductContextInCart(masterCart,
						productContext.getProduct());

				if (matchingMasterProductContext != null) {
					// Merge the two productContexts
					ProductContext mergedProductContext = mergeProductContexts(matchingMasterProductContext,
							productContext);
					mergedProductContexts.add(mergedProductContext);
				} else {
					// No match found. Add to the master project
					mergedProductContexts.add(productContext);
				}
			}

			mergedCart.setProductsInCart(mergedProductContexts);
			mergedCart.calculateTotal();
		}

		// Persist to DB
		mergedCart = cartRepository.save(mergedCart);
		log.info("Merged cart add to DB...");

		// Drop the cart and master cart
		cartRepository.delete(cart);
		if (masterCart != null) {
			cartRepository.delete(masterCart);
		}
		log.info("Deleted the participating carts!");

		return mergedCart;
	}

	private ProductContext mergeProductContexts(ProductContext matchingMasterProductContext,
			ProductContext productContext) {
		log.info("mergeProductContexts");
		ProductContext mergedContext = new ProductContext();
		mergedContext.setProduct(matchingMasterProductContext.getProduct());
		mergedContext.setQuantity(matchingMasterProductContext.getQuantity() + productContext.getQuantity());

		return mergedContext;
	}

	private ProductContext findMatchingProductContextInCart(Cart cart, Product product) throws Exception {
		ProductContext match = null;

		for (ProductContext productContext : cart.getProductsInCart()) {
			if (product.getId().equals(productContext.getProduct().getId())) {
				// Match Found
				match = productContext;
				break;
			}
		}

		return match;
	}

}
