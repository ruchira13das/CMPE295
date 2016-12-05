package com.sjsu.masterproject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.entity.RecommendationActivityLog;
import com.sjsu.masterproject.service.RecommendationsService;

@BaseRestController
public class RecommendationController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RecommendationsService recommendationsService;

	@RequestMapping(value = "/recommendation/customer/{customer_id}/category/{category}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Product> getRecommendationsForUserByCategory(@PathVariable("customer_id") String customerId,
			@PathVariable("category") String category) throws Exception {
		log.info("getRecommendationsForUserByCategory: customer: {}, category: {}", customerId, category);
		return recommendationsService.getRecommendations(customerId, category);
	}

	@RequestMapping(value = "/recommendation/customer/{customer_id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public List<Product> getRecommendationsForUser(@PathVariable("customer_id") String customerId) throws Exception {
		log.info("getRecommendationsForUser: customer: {}", customerId);
		return recommendationsService.getRecommendations(customerId);
	}

	@RequestMapping(value = "/recommendation/refresh/{category}", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public boolean refreshRecommendations(@PathVariable("category") String category) throws Exception {
		return recommendationsService.refreshRecommendations(category);
	}

	@RequestMapping(value = "/recommendation/log", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean logActivity(@RequestBody RecommendationActivityLog activityLog) throws Exception {
		return recommendationsService.logActivity(activityLog);
	}
}
