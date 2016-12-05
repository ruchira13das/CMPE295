package com.sjsu.masterproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.masterproject.entity.Purchase;

public interface PurchaseRepository extends MongoRepository<Purchase, String> {

}
