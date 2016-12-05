package com.sjsu.masterproject.transform.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.masterproject.transform.entity.Recommendations;


public interface RecommendationsRepository extends MongoRepository<Recommendations, String> {

}
