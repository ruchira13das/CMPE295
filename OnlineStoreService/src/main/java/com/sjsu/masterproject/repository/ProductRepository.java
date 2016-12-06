package com.sjsu.masterproject.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.sjsu.masterproject.entity.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	public List<Product> findByCategory(String category);

	public List<Product> findByFeatured(String featuredFlag);

	public List<Product> findByFeaturedAndCategory(String featuredFlag, String category);
	
	public List<Product> findByFeaturedAndBrand(String featuredFlag, String brand);

	public List<Product> findByIdAsLongStringIn(@Param("idAsLongString") List<String> ids);
}
