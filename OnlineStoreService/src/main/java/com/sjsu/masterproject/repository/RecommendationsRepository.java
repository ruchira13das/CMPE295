package com.sjsu.masterproject.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.sjsu.masterproject.entity.Recommendations;

public interface RecommendationsRepository extends MongoRepository<Recommendations, String> {

	public Recommendations findByUserIdAndCategory(String userId, String category);

	public List<Recommendations> findByUserId(String userId);

	public List<Recommendations> deleteByCategory(String category);
}
