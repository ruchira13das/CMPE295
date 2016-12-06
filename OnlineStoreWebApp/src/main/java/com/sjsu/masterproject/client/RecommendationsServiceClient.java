package com.sjsu.masterproject.client;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sjsu.masterproject.client.logger.RecommendationLogger;
import com.sjsu.masterproject.model.Product;
import com.sjsu.masterproject.model.RecommendationActivityLog;

@Component
public class RecommendationsServiceClient extends OnlineStoreServiceClient {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	private static final String GET_RECOMMENDATIONS_URI = "/recommendation/customer/";
	private static final String GET_RECOMMENDATIONS_BY_CATEGORY_URI = "/category/";
	private static final String GET_RECOMMDENDATION_ACTIVITY_LOG_URI = "/recommendation/log";

	public List<Product> getRecommendationsForCustomer(String customerId) {
		log.info("getRecommendationsForCustomer for: {}", customerId);

		List<Product> recommendations = null;
		String serviceEndPoint = getBaseServiceUrl(env) + GET_RECOMMENDATIONS_URI + customerId + "/";

		log.info("getCategories: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<Product>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Product>>() {
			});
			recommendations = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting recommendations for: {}!", customerId, e);
		}

		log.info("Recommendations:: {}", recommendations);

		return recommendations;
	}

	public List<Product> getRecommendationsForCustomerByCategory(String customerId, String category) {
		log.info("getRecommendationsForCustomerByCategory for: {} and category: {}", customerId, category);

		List<Product> recommendations = null;
		String serviceEndPoint = getBaseServiceUrl(env) + GET_RECOMMENDATIONS_URI + customerId
				+ GET_RECOMMENDATIONS_BY_CATEGORY_URI + category;

		log.info("getRecommendationsForCustomerByCategory: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<List<Product>> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Product>>() {
			});
			recommendations = response.getBody();
		} catch (Exception e) {
			log.error("Error while getting recommendations for: {} and category: {}!", customerId, category, e);
		}

		log.info("Recommendations:: {}", recommendations);

		return recommendations;
	}

	public void logActivity(RecommendationActivityLog activityLog) {
		log.info("logActivity: {}", activityLog);
		String serviceEndPoint = getBaseServiceUrl(env) + GET_RECOMMDENDATION_ACTIVITY_LOG_URI;

		if (activityLog != null) {
			// Log the activity asynchronously
			ExecutorService executor = Executors.newSingleThreadExecutor();
			try {
				Runnable recommendationsLogger = new RecommendationLogger(serviceEndPoint, activityLog);
				executor.execute(recommendationsLogger);
			} catch (Exception e) {
				log.error("Error while logging the recommendation activity: {}", e);
			} finally {
				executor.shutdown();
			}
		}
	}

}
