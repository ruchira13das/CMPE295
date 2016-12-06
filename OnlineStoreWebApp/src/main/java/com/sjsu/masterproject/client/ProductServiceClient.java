package com.sjsu.masterproject.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sjsu.masterproject.model.Product;

@Component
public class ProductServiceClient extends OnlineStoreServiceClient {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	private static final String GET_CATEGORIES_URI = "/store/categories";
	private static final String GET_FEATURED_PRODUCTS_URI = "/store/featured";
	private static final String GET_FEATURED_PRODUCTS_BY_CATEGORY_URI = "/category/";
	private static final String GET_FEATURED_PRODUCTS_BY_BRAND_URI = "/brands/";
	private static final String GET_PRODUCT_DETAILS_BASE_URI = "/store/product/";
	private static final String GET_PRODUCT_DETAILS_CUSTOMER_URI = "/customer/";
	private static final String GET_BRANDS_BY_CATEGORY_URI = "/store/brands";
	private static final String GET_BRANDS_FOR_FEATURED_URI = "/store/brands/featured";

	@Cacheable("categories")
	public List<String> getCategories() {
		log.info("getCategories....");

		List<String> categories = new ArrayList<>();
		String serviceEndPoint = getBaseServiceUrl(env) + GET_CATEGORIES_URI;

		log.info("getCategories: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<String>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
			});
			categories = response.getBody();
			categories.add("all");
		} catch (Exception e) {
			log.error("Error while getting the product categories!", e);
		}

		log.info("Categories: {}", categories);

		return categories;
	}

	@Cacheable("brandsForFeaturedProducts")
	public List<String> getBrandsForFeaturedProducts() {
		log.info("getBrandsForFeaturedProducts");

		List<String> brands = new ArrayList<>();
		String serviceEndPoint = getBaseServiceUrl(env) + GET_BRANDS_FOR_FEATURED_URI;

		log.info("getBrandsForFeaturedProducts: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<String>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
			});
			brands = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting the brands for the featured products!", e);
		}

		log.info("Brands for featured products: {}", brands);

		return brands;
	}
	
	@Cacheable("brands")
	public List<String> getBrands(String category) {
		log.info("getBrands for category: {}", category);

		List<String> brands = new ArrayList<>();
		String serviceEndPoint = getBaseServiceUrl(env) + GET_BRANDS_BY_CATEGORY_URI + "/" + category;

		log.info("getBrands: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<String>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<String>>() {
			});
			brands = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting the product brands for category: {}!", category, e);
		}

		log.info("Brands for category: {} :: {}", category, brands);

		return brands;
	}

	public Product getProductDetails(String productId, String customerId) {
		log.info("getProductDetails for: {}, user: {}", productId, customerId);

		Product product = null;
		String serviceEndPoint = getBaseServiceUrl(env) + GET_PRODUCT_DETAILS_BASE_URI + productId
				+ GET_PRODUCT_DETAILS_CUSTOMER_URI + customerId;

		log.info("getProductDetails: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<Product> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<Product>() {
			});
			product = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting the details for product: {}!", productId, e);
		}

		log.info("product: {}", product);

		return product;
	}

	public List<Product> getFeaturedProducts() {
		log.info("getFeaturedProducts...");

		List<Product> featuredProducts = new ArrayList<>();
		String serviceEndPoint = getBaseServiceUrl(env) + GET_FEATURED_PRODUCTS_URI;

		log.info("getFeaturedProducts: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<Product>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Product>>() {
			});
			featuredProducts = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting the store's featured products!", e);
		}

		log.info("Featured products: {}", featuredProducts);

		return featuredProducts;
	}

	public List<Product> getFeaturedProductsByCategory(String category) {
		log.info("getFeaturedProductsByCategory for: {}", category);

		List<Product> featuredProducts = new ArrayList<>();
		String serviceEndPoint = getBaseServiceUrl(env) + GET_FEATURED_PRODUCTS_URI + GET_FEATURED_PRODUCTS_BY_CATEGORY_URI + category;

		log.info("getFeaturedProductsByCategory: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<Product>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Product>>() {
			});
			featuredProducts = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting the store's featured products for: {}!", category, e);
		}

		log.info("Featured products: {}", featuredProducts);

		return featuredProducts;
	}
	
	public List<Product> getFeaturedProductsByBrand(String brand) {
		log.info("getFeaturedProductsByBrand for: {}", brand);

		List<Product> featuredProducts = new ArrayList<>();
		String serviceEndPoint = getBaseServiceUrl(env) + GET_FEATURED_PRODUCTS_URI + GET_FEATURED_PRODUCTS_BY_BRAND_URI+ brand;

		log.info("getFeaturedProductsByBrand: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<Product>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Product>>() {
			});
			featuredProducts = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting the store's featured products for: {}!", brand, e);
		}

		log.info("Featured products: {}", featuredProducts);

		return featuredProducts;
	}

}
