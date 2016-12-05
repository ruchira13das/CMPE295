package com.sjsu.masterproject.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.entity.RecommendationActivityLog;
import com.sjsu.masterproject.service.RecommendationsService;

public class Util {
	private static final Logger log = LoggerFactory.getLogger(Util.class);

	public static void logRecommendationActivity(RecommendationsService recommendationsService,
			List<Product> productsList,
			String customerId, String preference) throws Exception {
		log.info("Start: Logging recommendation activity...");

		if (productsList != null && StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(preference)) {
			for (Product product : productsList) {
				RecommendationActivityLog activityLog = new RecommendationActivityLog();
				activityLog.setCategory(product.getCategory());
				activityLog.setProductId(product.getIdAsLongString());
				activityLog.setUserId(customerId);
				activityLog.setPreference(preference);

				// Log activity
				recommendationsService.logActivity(activityLog);
				log.info("Recommendation activity logged: {}", activityLog);
			}
		}
	}

}
