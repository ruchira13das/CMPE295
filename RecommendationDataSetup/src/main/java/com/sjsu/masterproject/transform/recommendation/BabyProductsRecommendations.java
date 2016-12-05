package com.sjsu.masterproject.transform.recommendation;

import java.io.File;

public class BabyProductsRecommendations extends MerchandizeRecommendations {

	@Override
	protected File getInputFile() {
		return new File("data/recommendations/processed/ratings_Baby_ready.csv");
	}

	@Override
	protected String getCategory() {
		return "Baby";
	}

}
