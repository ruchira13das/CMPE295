package com.sjsu.masterproject.transform.recommendation;

import java.io.File;

public class CellPhoneAccessoriesRecommendations extends MerchandizeRecommendations {

	@Override
	protected File getInputFile() {
		return new File("data/recommendations/processed/ratings_Cell_Phones_and_Accessories_ready.csv");
	}

	@Override
	protected String getCategory() {
		return "Cell Phones & Accessories";
	}

}
