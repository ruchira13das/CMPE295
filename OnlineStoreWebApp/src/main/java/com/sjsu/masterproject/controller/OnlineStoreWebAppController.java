package com.sjsu.masterproject.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sjsu.masterproject.client.CheckoutServiceClient;
import com.sjsu.masterproject.client.CustomerManagementServiceClient;
import com.sjsu.masterproject.client.ProductServiceClient;
import com.sjsu.masterproject.client.RecommendationsServiceClient;
import com.sjsu.masterproject.form.AddToCartForm;
import com.sjsu.masterproject.form.SignInForm;
import com.sjsu.masterproject.form.SignUpForm;
import com.sjsu.masterproject.model.Cart;
import com.sjsu.masterproject.model.CartContext;
import com.sjsu.masterproject.model.Customer;
import com.sjsu.masterproject.model.Product;
import com.sjsu.masterproject.model.ProductContext;
import com.sjsu.masterproject.model.RecommendationActivityLog;
import com.sjsu.masterproject.model.WishList;
import com.sjsu.masterproject.model.WishListContext;
import com.sjsu.masterproject.util.CookieUtil;
import com.sjsu.masterproject.util.Util;

@Controller
public class OnlineStoreWebAppController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ProductServiceClient productServiceClient;

	@Autowired
	private RecommendationsServiceClient recommendationsServiceClient;

	@Autowired
	private CheckoutServiceClient checkoutServiceClient;

	@Autowired
	private CustomerManagementServiceClient customerServiceClient;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private Environment env;

	@GetMapping(value = "/")
	public String home(Model model) throws Exception {

		// Get categories
		List<String> categories = productServiceClient.getCategories();
		model.addAttribute("categories", categories);

		// Get brands
		List<String> brands = new ArrayList<>();
		for (String category : categories) {
			List<String> brandsForCategory = productServiceClient.getBrands(category);
			if (brandsForCategory != null && brandsForCategory.size() > 3) {
				brands.addAll(productServiceClient.getBrands(category).subList(0, 3));
			}
		}

		// Add cookie to response - max-age: 30 days
		Util.addCustomerIdCookieToResponse(response, request);

		Util.setSignInStatus(model, request);
		model.addAttribute("brands", brands);

		return "home";
	}

	@GetMapping(value = "/product/{product_id}")
	public String getProductPage(@PathVariable("product_id") String productId, Model model) throws Exception {
		log.info("getProductPage: {}", productId);

		if (StringUtils.isBlank(productId)) {
			throw new Exception("No product id is provided!");
		}

		Product product = productServiceClient.getProductDetails(productId, Util.getCustomerFromRequest(request));
		model.addAttribute("product", product);

		// Initialize form defaults
		AddToCartForm addToCartForm = new AddToCartForm(product.getId(), 1);
		model.addAttribute("addToCartForm", addToCartForm);

		// Add cookie to response - max-age: 30 days
		Util.addCustomerIdCookieToResponse(response, request);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "product-details";
	}

	@GetMapping(value = "/cart")
	public String showCart(Model model) throws Exception {
		log.info("showCart...");

		Cart cart = checkoutServiceClient.getCartForCustomer(Util.getCustomerFromRequest(request));
		model.addAttribute("cart", cart);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "cart";
	}

	@GetMapping(value = "/cart/remove/{product_id}")
	public String removeFromCart(@PathVariable("product_id") String productId, Model model) throws Exception {
		log.info("removeFromCart...{}", productId);

		if (StringUtils.isBlank(productId)) {
			throw new Exception("removeFromCart: No product-id is provided!");
		}

		String customerId = Util.getCustomerFromRequest(request);

		Customer customer = new Customer();
		customer.setId(customerId);

		Cart cart = new Cart();
		cart.setCustomer(customer);

		// Quantity is defaulted to 1
		ProductContext productForCart = new ProductContext(
				productServiceClient.getProductDetails(productId, customerId), 1);

		CartContext cartContext = new CartContext();
		cartContext.setCart(cart);
		cartContext.setProductForCart(productForCart);

		Cart updatedCart = checkoutServiceClient.removeFromCart(cartContext);
		model.addAttribute("cart", updatedCart);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "cart";
	}

	@PostMapping(value = "/cart/add")
	public String addToCart(@Valid AddToCartForm addToCartForm, BindingResult bindingResult, Model model)
			throws Exception {

		String productId = addToCartForm.getProductId();
		int quantity = addToCartForm.getQuantity();

		log.info("addToCart: {}, quantity: {}", productId, quantity);

		if (StringUtils.isBlank(productId) && quantity > 0) {
			throw new Exception("No product-id / invalid quantity is provided!");
		}

		String customerId = Util.getCustomerFromRequest(request);

		Customer customer = new Customer();
		customer.setId(customerId);

		Cart cart = new Cart();
		cart.setCustomer(customer);

		ProductContext productForCart = new ProductContext(
				productServiceClient.getProductDetails(productId, customerId), quantity);

		CartContext cartContext = new CartContext();
		cartContext.setCart(cart);
		cartContext.setProductForCart(productForCart);

		Cart updatedCart = checkoutServiceClient.addToCart(cartContext);
		model.addAttribute("cart", updatedCart);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "cart";
	}

	@GetMapping(value = "/cart/add/{product_id}")
	public String addToCart(@PathVariable("product_id") String productId, Model model) throws Exception {
		log.info("addToCart: {}, quantity: {}", productId);

		// Quantity defaulted to 1 for GET API
		int quantity = 1;
		if (StringUtils.isBlank(productId) && quantity > 0) {
			throw new Exception("No product-id / invalid quantity is provided!");
		}

		String customerId = Util.getCustomerFromRequest(request);

		Customer customer = new Customer();
		customer.setId(customerId);

		Cart cart = new Cart();
		cart.setCustomer(customer);

		ProductContext productForCart = new ProductContext(
				productServiceClient.getProductDetails(productId, customerId), quantity);

		CartContext cartContext = new CartContext();
		cartContext.setCart(cart);
		cartContext.setProductForCart(productForCart);

		Cart updatedCart = checkoutServiceClient.addToCart(cartContext);
		model.addAttribute("cart", updatedCart);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "cart";
	}

	@GetMapping(value = "/wishlist")
	public String showWishList(Model model) throws Exception {
		log.info("showWishList...");

		WishList wishList = checkoutServiceClient.getWishListForCustomer(Util.getCustomerFromRequest(request));
		model.addAttribute("wishlist", wishList);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "wishlist";
	}

	@GetMapping(value = "/wishlist/remove/{product_id}")
	public String removeFromWishList(@PathVariable("product_id") String productId, Model model) throws Exception {
		log.info("removeFromWishList...{}", productId);

		if (StringUtils.isBlank(productId)) {
			throw new Exception("removeFromWishList: No product-id is provided!");
		}

		String customerId = Util.getCustomerFromRequest(request);

		Customer customer = new Customer();
		customer.setId(customerId);

		WishList wishList = new WishList();
		wishList.setCustomer(customer);

		Product product = productServiceClient.getProductDetails(productId, customerId);


		WishListContext wishListContext = new WishListContext();
		wishListContext.setWishList(wishList);
		wishListContext.setProductForWishList(product);


		WishList updatedWishList = checkoutServiceClient.removeFromWishList(wishListContext);
		model.addAttribute("wishlist", updatedWishList);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "wishlist";
	}

	@GetMapping(value = "/wishlist/add/{product_id}")
	public String addToWishlist(@PathVariable("product_id") String productId, Model model) throws Exception {
		log.info("addToWishlist: {}", productId);

		if (StringUtils.isBlank(productId)) {
			throw new Exception("No product-id quantity is provided!");
		}

		String customerId = Util.getCustomerFromRequest(request);

		Customer customer = new Customer();
		customer.setId(customerId);

		WishList wishList = new WishList();
		wishList.setCustomer(customer);

		WishListContext wishListContext = new WishListContext();
		wishListContext.setWishList(wishList);
		wishListContext.setProductForWishList(productServiceClient.getProductDetails(productId, customerId));

		WishList updatedWishList = checkoutServiceClient.addToWishList(wishListContext);
		model.addAttribute("wishlist", updatedWishList);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "wishlist";
	}

	@GetMapping(value = "/recommendations/{category}")
	public String getRecommendations(@PathVariable("category") String category, Model model) throws Exception {
		log.info("getRecommendations....{}", category);

		// Get Recommendations
		String customerId = Util.getCustomerFromRequest(request);
		// customerId = "A17SLR18TUMULM";
		log.info("Customer id: {}, category: {}", customerId, category);

		List<Product> recommendations = new ArrayList<>();
		if ("all".equalsIgnoreCase(category)) {
			recommendations = recommendationsServiceClient.getRecommendationsForCustomer(customerId);
		} else {
			recommendations = recommendationsServiceClient.getRecommendationsForCustomerByCategory(customerId,
					category);
		}

		// Stack up the recommendation data into response in a template-ready
		// format
		if (recommendations != null && recommendations.size() > 0) {
			model = Util.addRecommendationsToResponse(model, recommendations);
		}

		return "recommendations";
	}

	@GetMapping(value = "/category/{category}")
	public String featuredProductsByCategory(@PathVariable("category") String category, Model model) {

		log.info("featuredProductsByCategory....{}", category);

		// Get featured products
		List<Product> featuredProducts = new ArrayList<>();
		if ("all".equalsIgnoreCase(category)) {
			featuredProducts = productServiceClient.getFeaturedProducts();
		} else {
			featuredProducts = productServiceClient.getFeaturedProductsByCategory(category);
		}

		model.addAttribute("featuredProducts", featuredProducts);

		return "featured";
	}

	@GetMapping(value = "/{brand}")
	public String catalogByBrand(Model model) throws Exception {

		// Get categories
		List<String> categories = productServiceClient.getCategories();
		model.addAttribute("categories", categories);

		// Get brands
		List<String> brands = new ArrayList<>();
		for (String category : categories) {
			List<String> brandsForCategory = productServiceClient.getBrands(category);
			if (brandsForCategory != null && brandsForCategory.size() > 3) {
				brands.addAll(productServiceClient.getBrands(category).subList(0, 3));
			}
		}
		model.addAttribute("brands", brands);

		// Get featured products
		List<Product> featuredProducts = new ArrayList<>();
		featuredProducts = productServiceClient.getFeaturedProducts();

		model.addAttribute("featuredProducts", featuredProducts);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "home";
	}

	@GetMapping(value = "/login")
	public String showLogin(@RequestParam Map<String, String> queryParameters, Model model) throws Exception {

		log.info("login:: query params :: {}", queryParameters);
		String forward = "home";
		if (queryParameters != null && queryParameters.containsKey("frwd")) {
			forward = queryParameters.get("frwd").toString();
		}

		// Initialize form defaults
		SignInForm signInForm = new SignInForm();
		signInForm.setForwardAction(forward);

		SignUpForm signUpForm = new SignUpForm();
		signUpForm.setForwardAction(forward);

		model.addAttribute("forward", forward);
		model.addAttribute("preferences", env.getProperty(Util.CUSTOMER_PREFERENCES_CONFIG, List.class));
		model.addAttribute("signInForm", signInForm);
		model.addAttribute("signUpForm", signUpForm);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "login";
	}

	@PostMapping(value = "/signin")
	public String signInAndForward(@Valid SignInForm signInForm, BindingResult bindingResult, Model model)
			throws Exception {

		String customerId = signInForm.getCustomerId();
		String password = signInForm.getPassword();
		String forwardAction = signInForm.getForwardAction();

		log.info("signInAndForward: customerId: {}, password: {}, forwardAction: {}", customerId, password,
				forwardAction);

		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(password)) {
			model = Util.setModelForSignInAction(model, forwardAction, signInForm, "Invalid login!", env);

			// Go back to login with error message
			return "login";
		}

		try {
			// Get the anonymous cart to merge
			Cart anonymousCart = checkoutServiceClient.getCartForCustomer(Util.getCustomerFromRequest(request));

			Customer customer = new Customer();
			customer.setId(customerId);
			customer.setPassword(password);
			boolean isSignedIn = customerServiceClient.signIn(customer);

			if (isSignedIn) {
				// Add customerId cookie to response - max-age: 30 days
				CookieUtil.create(response, "eshopper_customer", customerId, 30 * 24 * 3600);

				// Add isLoggedIn cookie to response - max-age: session
				CookieUtil.create(response, "eshopper_loggedin", customerId, -1);

				// Set sign-in status
				Util.setSignInStatus(model, request);

				// Merge carts
				if (anonymousCart != null) {
					checkoutServiceClient.mergeCarts(anonymousCart, customerId);
				}

				String pageForward = Util.getForwardPage(forwardAction);

				String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":"
						+ request.getServerPort() + "/" + pageForward;
				log.info("redirectUrl: {}", redirectUrl);

				return "redirect:" + redirectUrl;
			} else {
				// SignIn failed
				model = Util.setModelForSignInAction(model, "login", signInForm, "Sign-in failed!", env);

				// Go back to login with error message
				return "login";
			}
		} catch (Exception e) {
			// SignIn Error
			log.error("Error in sign-in.", e);

			// SignIn error
			model = Util.setModelForSignInAction(model, "login", signInForm, "Sign-in error!", env);

			// Go back to login with error message
			return "login";
		}
	}

	@GetMapping(value = "/signout")
	public String signOut(Model model) throws Exception {

		log.info("signOut...");
		if (Util.isCustomerLoggedIn(request)) {
			// Drop the eshopper_loggedin cookie
			CookieUtil.clear(response, "eshopper_loggedin");
			log.info("login cookie is dropped!");
		}

		return "home";
	}

	@GetMapping(value = "/checkout")
	public String checkout(Model model) throws Exception {
		log.info("checkout...");

		if (!Util.isCustomerLoggedIn(request)) {
			// Customer not logged in. Redirect to login page
			String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ "/login.html?frwd=checkout";
			log.info("redirectUrl: {}", redirectUrl);
			return "redirect:" + redirectUrl;
		}

		Cart cart = checkoutServiceClient.getCartForCustomer(Util.getCustomerFromRequest(request));
		model.addAttribute("cart", cart);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		// Log recommendation activity
		if (cart != null) {
			List<ProductContext> productContextList = cart.getProductsInCart();
			for (ProductContext productContext : productContextList) {
				Product product = productContext.getProduct();
				RecommendationActivityLog activityLog = new RecommendationActivityLog();
				activityLog.setProductId(product.getIdAsLongString());
				activityLog.setUserId(cart.getCustomer().getIdAsLongString());
				activityLog.setCategory(product.getCategory());
				// Preference for checkout
				activityLog.setPreference("4.0");

				recommendationsServiceClient.logActivity(activityLog);
				log.info("Checkout recommendation activity logged: {}", activityLog);
			}
		}

		return "checkout";
	}

	@GetMapping(value = "/checkout/remove/{product_id}")
	public String removeFromCartInCheckout(@PathVariable("product_id") String productId, Model model) throws Exception {
		log.info("removeFromCartInCheckout...{}", productId);

		if (StringUtils.isBlank(productId)) {
			throw new Exception("removeFromCartInCheckout: No product-id is provided!");
		}

		String customerId = Util.getCustomerFromRequest(request);

		Customer customer = new Customer();
		customer.setId(customerId);

		Cart cart = new Cart();
		cart.setCustomer(customer);

		// Quantity is defaulted to 1
		ProductContext productForCart = new ProductContext(
				productServiceClient.getProductDetails(productId, customerId), 1);

		CartContext cartContext = new CartContext();
		cartContext.setCart(cart);
		cartContext.setProductForCart(productForCart);

		Cart updatedCart = checkoutServiceClient.removeFromCart(cartContext);
		model.addAttribute("cart", updatedCart);

		// Set sign-in status
		Util.setSignInStatus(model, request);

		return "checkout";
	}

	@GetMapping(value = "/purchase")
	public String purchase(Model model) throws Exception {
		log.info("purchase...");

		Cart cart = checkoutServiceClient.getCartForCustomer(Util.getCustomerFromRequest(request));
		model.addAttribute("cart", cart);

		// Execute the purchase
		if (checkoutServiceClient.purchase(cart)) {
			// Set sign-in status
			Util.setSignInStatus(model, request);

			return "thank-you";
		} else {
			// Error page
			return "error";
		}
	}

	// @GetMapping(value = "/error")
	// public String showErrorPage(Model model) throws Exception {
	// log.info("purchase...");
	//
	// Cart cart =
	// checkoutServiceClient.getCartForCustomer(Util.getCustomerFromRequest(request));
	// model.addAttribute("cart", cart);
	//
	// // Execute the purchase
	// if (checkoutServiceClient.purchase(cart)) {
	// // Redirect to thank-you page
	// String redirectUrl = request.getScheme() + "://" +
	// request.getServerName() + ":" + request.getServerPort()
	// + "/thank-you.html";
	// log.info("redirectUrl: {}", redirectUrl);
	//
	// return redirectUrl;
	// } else {
	// // Error page
	// return "error";
	// }
	// }
}
