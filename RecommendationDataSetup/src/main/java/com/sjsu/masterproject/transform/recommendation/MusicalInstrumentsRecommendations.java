package com.sjsu.masterproject.transform.recommendation;

import java.io.File;

public class MusicalInstrumentsRecommendations extends MerchandizeRecommendations {

	@Override
	protected File getInputFile() {
		return new File("data/recommendations/processed/ratings_Musical_Instruments_ready.csv");
	}

	@Override
	protected String getCategory() {
		return "Musical Instruments";
	}

}
