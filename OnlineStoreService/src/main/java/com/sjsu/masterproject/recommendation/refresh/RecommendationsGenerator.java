package com.sjsu.masterproject.recommendation.refresh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import com.sjsu.masterproject.repository.RecommendationsRepository;
import com.sjsu.masterproject.util.Constants;

public class RecommendationsGenerator implements Runnable {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private Environment env;
	private String category;
	private RecommendationsRepository repository;

	public RecommendationsGenerator(Environment env, String category, RecommendationsRepository repository) {
		super();
		this.env = env;
		this.category = category;
		this.repository = repository;
	}

	@Override
	public void run() {
		log.info("RecommendationsGenerator: Running thread!");

		try {
			refreshRecommendationsRepository();
		} catch (Exception e) {
			log.error("Error while refreshing recommendations", e);
		}
	}

	private void refreshRecommendationsRepository() throws Exception {
		log.info("Start recommendations refresh for category: {}", category);
		env.getProperty("app.config.productCategory.baby");

		Recommender recommender = getRecommender();
		if (recommender != null) {
			log.info("Recommender Type: {}", recommender.getClass().getName());
			recommender.refreshRecommendations(repository);
		} else {
			log.error("Unable to  find a recommender for category: {}", category);
		}
	}

	private Recommender getRecommender() {
		Recommender recommender = null;

		if (category.equalsIgnoreCase(getCategoryConfigValue(Constants.CATEGORY_CONFIG_BABY))) {
			recommender = new BabyRecommender(env);
		} else if (category.equalsIgnoreCase(getCategoryConfigValue(Constants.CATEGORY_CONFIG_BEAUTY))) {
			recommender = new BeautyRecommender(env);
		} else if (category.equalsIgnoreCase(getCategoryConfigValue(Constants.CATEGORY_CONFIG_BOOKS))) {
			recommender = new BooksRecommender(env);
		} else if (category.equalsIgnoreCase(getCategoryConfigValue(Constants.CATEGORY_CONFIG_CELLPHONES))) {
			recommender = new CellPhoneAccessoriesRecommender(env);
		} else if (category.equalsIgnoreCase(getCategoryConfigValue(Constants.CATEGORY__CONFIG_TOYSANDGAMES))) {
			recommender = new ToysAndGamesRecommender(env);
		} else if (category.equalsIgnoreCase(getCategoryConfigValue(Constants.CATEGORY_CONFIG_MUSICALINSTRUMENTS))) {
			recommender = new MusicalInstrumentsRecommender(env);
		}

		return recommender;
	}

	private String getCategoryConfigValue(String config) {
		return env.getProperty(config);
	}

	public Environment getEnv() {
		return env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public RecommendationsRepository getRepository() {
		return repository;
	}

	public void setRepository(RecommendationsRepository repository) {
		this.repository = repository;
	}

}
