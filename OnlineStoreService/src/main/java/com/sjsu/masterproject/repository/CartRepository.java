package com.sjsu.masterproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.masterproject.entity.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {

	public Cart findByCustomerId(String customerId);
}
