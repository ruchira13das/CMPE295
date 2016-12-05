package com.sjsu.masterproject.service;

import java.util.List;

import com.sjsu.masterproject.entity.Product;

public interface ProductService {

	public List<String> getBrandsForCategory(String category) throws Exception;

	public List<String> getCategories() throws Exception;

	public List<Product> getFeaturedProducts() throws Exception;

	public List<Product> getFeaturedProductsByCategory(String category) throws Exception;

	public List<Product> findByCategory(String category) throws Exception;

	public List<Product> findByBrand(String brand) throws Exception;

	public List<Product> findProductsByIdAsLongString(List<String> idAsLongStringList) throws Exception;

	public Product getProduct(String productId) throws Exception;
}
