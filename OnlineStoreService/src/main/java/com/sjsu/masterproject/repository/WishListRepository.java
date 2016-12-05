package com.sjsu.masterproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.masterproject.entity.WishList;

public interface WishListRepository extends MongoRepository<WishList, String> {
	public WishList findByCustomerId(String customerId);
}
