package com.sjsu.masterproject.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sjsu.masterproject.controller.exception.ServiceException;
import com.sjsu.masterproject.entity.Customer;
import com.sjsu.masterproject.entity.Product;
import com.sjsu.masterproject.entity.RecommendationActivityLog;
import com.sjsu.masterproject.entity.Recommendations;
import com.sjsu.masterproject.recommendation.logger.RecommendationLogger;
import com.sjsu.masterproject.recommendation.refresh.RecommendationsGenerator;
import com.sjsu.masterproject.repository.RecommendationsRepository;
import com.sjsu.masterproject.service.CustomerManagementService;
import com.sjsu.masterproject.service.ProductService;
import com.sjsu.masterproject.service.RecommendationsService;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {

	@Autowired
	private RecommendationsRepository recommendationsRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerManagementService customerService;

	@Autowired
	private Environment env;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String RECOMMENDATION_LOG_BASE_PATH = "app.config.recommendation.log.path";
	private static final String RECOMMENDATION_LOG_FILE_BEAUTY = "app.config.recommendation.log.beauty";
	private static final String RECOMMENDATION_LOG_FILE_BABY = "app.config.recommendation.log.baby";
	private static final String RECOMMENDATION_LOG_FILE_BOOKS = "app.config.recommendation.log.books";
	private static final String RECOMMENDATION_LOG_FILE_CELLPHONES = "app.config.recommendation.log.cellphone";
	private static final String RECOMMENDATION_LOG_FILE_MUSICALINSTRUMENTS = "app.config.recommendation.log.musicalinstruments";
	private static final String RECOMMENDATION_LOG_FILE_TOYSANDGAMES = "ratings_Toys_and_Games_ready.csv";

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	@Override
	public List<Product> getRecommendations(String customerId, String category) throws Exception {
		if (StringUtils.isEmpty(customerId) || StringUtils.isEmpty(category)) {
			throw new ServiceException("Invalid request. User_Id/category are not provided/invalid");
		}

		// CustomerId is alphanumeric. Get the long representation for DB query
		MemoryIDMigrator migrator = new MemoryIDMigrator();
		String customerIdAsLongString = String.valueOf(migrator.toLongID(customerId));

		List<Product> recommendedProducts = null;
		Recommendations recommendations = recommendationsRepository.findByUserIdAndCategory(customerIdAsLongString,
				category);
		if (recommendations != null && recommendations.getRecommendations() != null
				&& recommendations.getRecommendations().size() > 0) {
			recommendedProducts = productService.findProductsByIdAsLongString(recommendations.getRecommendations());

			// Filter recommendations. Exclude if the user has already purchased
			// the product
			try {
				recommendedProducts = filterRecommendations(recommendedProducts, customerId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (log.isDebugEnabled()) {
				for (Product product : recommendedProducts) {
					log.debug("Product:: {}", product);
				}
			}
		}

		return recommendedProducts;
	}

	private List<Product> filterRecommendations(List<Product> recommendedProducts, String customerId) throws Exception {
		Customer customer = customerService.getCustomer(customerId);
		List<String> purchaseList = customer.getProductsPurchased();
		List<String> preferences = customer.getPreferences();

		Map<String, Product> recommendedProductsMap = new HashMap<String, Product>();
		for (Product product : recommendedProducts) {
			recommendedProductsMap.put(product.getId(), product);
		}

		// Filter out the products already purchased
		if (purchaseList != null && purchaseList.size() > 0) {
			for (String productId : purchaseList) {
				if (recommendedProductsMap.containsKey(productId)) {
					recommendedProductsMap.remove(productId);
					log.info("Removed product Id from the recommendations list: {}", productId);
				}
			}
		}

		// Filter based on user preferences
		if (preferences != null && preferences.size() > 0) {
			for (String productId : recommendedProductsMap.keySet()) {
				// User preference does not include this product category.
				if (!preferences.contains(recommendedProductsMap.get(productId).getCategory())) {
					recommendedProductsMap.remove(productId);
					log.info("Removed product Id from the recommendations list: {}", productId);
				}
			}
		}

		List<Product> filteredList = new ArrayList<Product>();
		filteredList.addAll(recommendedProductsMap.values());

		return filteredList;
	}

	@Override
	public List<Product> getRecommendations(String customerId) throws Exception {
		if (StringUtils.isEmpty(customerId)) {
			throw new ServiceException("Invalid request. User_Id is not provided/invalid");
		}

		// CustomerId is alphanumeric. Get the long representation for DB query
		MemoryIDMigrator migrator = new MemoryIDMigrator();
		String customerIdAsLongString = String.valueOf(migrator.toLongID(customerId));

		List<Product> recommendedProducts = new ArrayList<Product>();
		List<Recommendations> recommendationsList = recommendationsRepository.findByUserId(customerIdAsLongString);

		if (recommendationsList != null && recommendationsList.size() > 0) {
			for (Recommendations recommendations : recommendationsList) {
				if (recommendations != null && recommendations.getRecommendations() != null
						&& recommendations.getRecommendations().size() > 0) {
					recommendedProducts
					.addAll(productService.findProductsByIdAsLongString(recommendations.getRecommendations()));
				}
			}

			// Filter recommendations. Exclude if the user has already purchased
			// the product
			try {
				recommendedProducts = filterRecommendations(recommendedProducts, customerId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (log.isDebugEnabled()) {
				for (Product product : recommendedProducts) {
					log.debug("Product:: {}", product);
				}
			}
		}

		return recommendedProducts;
	}

	@Override
	public boolean logActivity(RecommendationActivityLog activityLog) throws Exception {
		if (activityLog == null || !activityLog.isValid()) {
			throw new ServiceException("Invalid request. RecommendationActivityLog is not provided/invalid");
		}

		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Runnable recommendationLogger = new RecommendationLogger(getTargetLogFilePath(activityLog),
					buildLogFileEntry(activityLog));
			executor.execute(recommendationLogger);
		} catch (Exception e) {
			log.error("Error while logging the recommendation activity!", e);
			throw new ServiceException("Error while logging the recommendation activity!");
		} finally {
			executor.shutdown();
		}

		return true;
	}

	private String buildLogFileEntry(RecommendationActivityLog activityLog) throws Exception {
		StringBuffer logEntry = new StringBuffer();
		logEntry.append(activityLog.getUserId()).append(COMMA_DELIMITER).append(activityLog.getProductId())
		.append(COMMA_DELIMITER).append(activityLog.getPreference()).append(COMMA_DELIMITER)
		.append(System.currentTimeMillis() / 1000L).append(NEW_LINE_SEPARATOR);

		log.info("buildLogFileEntry:: {}", logEntry);

		return logEntry.toString();
	}

	private String getTargetLogFilePath(RecommendationActivityLog activityLog) {
		StringBuffer logFilePath = new StringBuffer(env.getProperty(RECOMMENDATION_LOG_BASE_PATH));

		switch (activityLog.getCategory().toLowerCase()) {
		case "baby":
			logFilePath.append(env.getProperty(RECOMMENDATION_LOG_FILE_BABY));
			break;
		case "beauty":
			logFilePath.append(env.getProperty(RECOMMENDATION_LOG_FILE_BEAUTY));
			break;
		case "books":
			logFilePath.append(env.getProperty(RECOMMENDATION_LOG_FILE_BOOKS));
			break;
		case "cell phones & accessories":
			logFilePath.append(env.getProperty(RECOMMENDATION_LOG_FILE_CELLPHONES));
			break;
		case "musical instruments":
			logFilePath.append(env.getProperty(RECOMMENDATION_LOG_FILE_MUSICALINSTRUMENTS));
			break;
		case "toys & games":
			logFilePath.append(env.getProperty(RECOMMENDATION_LOG_FILE_TOYSANDGAMES));
			break;
		}

		log.info("getTargetLogFilePath: {}", logFilePath.toString());

		return logFilePath.toString();
	}

	@Override
	public boolean refreshRecommendations(String category) throws Exception {
		if (StringUtils.isEmpty(category)) {
			throw new ServiceException("Invalid request. RecommendationActivityLog is not provided/invalid");
		}

		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			Runnable recommendationsGenerator = new RecommendationsGenerator(env, category, recommendationsRepository);
			executor.execute(recommendationsGenerator);
		} catch (Exception e) {
			log.error("Error while refreshing the recommendations data for category: {}", category, e);
			throw new ServiceException("Error while refreshing the recommendations data for category: " + category);
		} finally {
			executor.shutdown();
		}

		return true;
	}

}
