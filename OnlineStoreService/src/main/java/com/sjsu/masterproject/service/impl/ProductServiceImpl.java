package com.sjsu.masterproject.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sjsu.masterproject.controller.exception.ServiceException;
import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.repository.ProductRepository;
import com.sjsu.masterproject.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String PRODUCT_CATEGORIES_CONFIG_NAME = "app.config.productCategories";

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private Environment env;

	@Override
	@Cacheable("brandsByCategory")
	public List<String> getBrandsForCategory(String category) throws Exception {
		log.info("getBrandsForCategory: {}", category);

		List<Product> products = productRepository.findByCategory(category);

		// Get unique brands
		Set<String> brands = new HashSet<>();
		for (Product product : products) {
			String brand = product.getBrand();
			if (!StringUtils.isEmpty(brand)) {
				brands.add(brand);
			}
		}

		log.info("Category: {} has {} different brands.", category, brands.size());
		return new ArrayList<>(brands);
	}

	@Override
	@Cacheable("productCategories")
	public List<String> getCategories() throws Exception {
		log.info("getCategories");

		@SuppressWarnings("unchecked")
		List<String> categories = env.getProperty(PRODUCT_CATEGORIES_CONFIG_NAME, List.class);

		log.info("categories: {}", categories);
		return categories;
	}

	@Override
	// @Cacheable("featuredProducts")
	public List<Product> getFeaturedProducts() throws Exception {
		log.info("getFeaturedProducts");

		List<Product> featured = productRepository.findByFeatured("true");

		return featured;
	}

	@Override
	// @Cacheable("featuredProducts")
	public List<Product> getFeaturedProductsByCategory(String category) throws Exception {
		log.info("getFeaturedProductsByCategory");

		List<Product> featured = productRepository.findByFeaturedAndCategory("true", category);

		return featured;
	}

	@Override
	public List<Product> findByCategory(String category) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> findByBrand(String brand) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Cacheable("products")
	public List<Product> findProductsByIdAsLongString(List<String> idAsLongStringList) throws Exception {
		log.info("findProductsByIdAsLongString for: {}", idAsLongStringList);

		if (idAsLongStringList == null || idAsLongStringList.size() <= 0) {
			throw new ServiceException("Invalid request. List of product ids is invalid/null.");
		}

		return productRepository.findByIdAsLongStringIn(idAsLongStringList);
	}

	@Override
	@Cacheable("products")
	public Product getProduct(String productId) throws Exception {
		log.info("getProduct for: {}", productId);

		if (StringUtils.isEmpty(productId)) {
			throw new ServiceException("Invalid request. Product id is invalid/null.");
		}

		return productRepository.findOne(productId);
	}

}
