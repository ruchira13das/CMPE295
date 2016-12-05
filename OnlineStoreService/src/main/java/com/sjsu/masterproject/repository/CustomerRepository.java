package com.sjsu.masterproject.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.masterproject.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {
	public Customer findByIdAsLongString(String idAsLongString);
}
