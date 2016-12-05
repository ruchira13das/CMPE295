package com.sjsu.masterproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.service.ProductService;
import com.sjsu.masterproject.service.RecommendationsService;
import com.sjsu.masterproject.util.Constants;
import com.sjsu.masterproject.util.Util;

@BaseRestController
public class StoreController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProductService productService;

	@Autowired
	RecommendationsService recommendationsService;

	@RequestMapping(value = "/store/categories", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<String> getCategories() throws Exception {
		return productService.getCategories();
	}

	@RequestMapping(value = "/store/featured", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Product> getFeaturedProducts() throws Exception {
		return productService.getFeaturedProducts();
	}

	@RequestMapping(value = "/store/featured/{category}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Product> getFeaturedProductsByCategorys(@PathVariable("category") String category) throws Exception {
		return productService.getFeaturedProductsByCategory(category);
	}

	@RequestMapping(value = "/store/brands/{category}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<String> getBrands(@PathVariable("category") String category) throws Exception {
		return productService.getBrandsForCategory(category);
	}

	@RequestMapping(value = "/store/product/{product_id}/customer/{customer_id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Product getProduct(@PathVariable("product_id") String productId,
			@PathVariable("customer_id") String customerId) throws Exception {

		// Get the product
		Product product = productService.getProduct(productId);

		// Log recommendation activity
		try {
			MemoryIDMigrator migrator = new MemoryIDMigrator();
			ArrayList<Product> productsList = new ArrayList<>();
			productsList.add(product);
			Util.logRecommendationActivity(recommendationsService, productsList,
					String.valueOf(migrator.toLongID(customerId)), Constants.RECOMMENDATION_PREFERENCE_VIEW);
		} catch (Exception e) {
			// Log and ignore
			log.error("Error while logging recommendation activity for: {}", productId, e);
		}

		return product;
	}

	@RequestMapping(value = "/store/search/{param}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Product> searchProducts(@PathVariable("param") String param) {
		return null;
	}
}
