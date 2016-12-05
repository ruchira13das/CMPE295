package com.sjsu.masterproject.transform.recommendation;

import java.io.File;

public class BeautyProductsRecommendations extends MerchandizeRecommendations {

	@Override
	protected File getInputFile() {
		return new File("data/recommendations/processed/ratings_Beauty_ready.csv");
	}

	@Override
	protected String getCategory() {
		return "Beauty";
	}

}
