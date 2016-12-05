package com.sjsu.masterproject.recommendation.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sjsu.masterproject.service.RecommendationsService;
import com.sjsu.masterproject.util.Constants;

@Component
public class RecommendationRefreshScheduler {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RecommendationsService recommendationsService;

	@Autowired
	private Environment env;

	public void refreshRecommendations(String category) throws Exception {
		log.info("Running scheduled recommendation task for category: {}", category);
		recommendationsService.refreshRecommendations(category);
	}

	@Scheduled(cron = "00 00 00 * * *")
	public void refreshRecommendationsForBaby() throws Exception {
		refreshRecommendations(env.getProperty(Constants.CATEGORY_CONFIG_BABY));
	}

	@Scheduled(cron = "00 10 00 * * *")
	public void refreshRecommendationsForBeauty() throws Exception {
		refreshRecommendations(env.getProperty(Constants.CATEGORY_CONFIG_BEAUTY));
	}

	@Scheduled(cron = "00 20 00 * * *")
	public void refreshRecommendationsForBooks() throws Exception {
		refreshRecommendations(env.getProperty(Constants.CATEGORY_CONFIG_BOOKS));
	}

	@Scheduled(cron = "00 30 00 * * *")
	public void refreshRecommendationsForCellPhonesAndAccessories() throws Exception {
		refreshRecommendations(env.getProperty(Constants.CATEGORY_CONFIG_CELLPHONES));
	}

	@Scheduled(cron = "00 40 00 * * *")
	public void refreshRecommendationsForMusicalInstruments() throws Exception {
		refreshRecommendations(env.getProperty(Constants.CATEGORY_CONFIG_MUSICALINSTRUMENTS));
	}

	@Scheduled(cron = "00 50 00 * * *")
	public void refreshRecommendationsForToysAndGames() throws Exception {
		refreshRecommendations(env.getProperty(Constants.CATEGORY__CONFIG_TOYSANDGAMES));
	}
}
