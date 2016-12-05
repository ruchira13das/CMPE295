package com.sjsu.masterproject.service;

import java.util.List;

import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.entity.RecommendationActivityLog;

public interface RecommendationsService {
	public List<Product> getRecommendations(String customerId, String category) throws Exception;

	public List<Product> getRecommendations(String customerId) throws Exception;

	public boolean logActivity(RecommendationActivityLog activityLog) throws Exception;

	public boolean refreshRecommendations(String category) throws Exception;
}
