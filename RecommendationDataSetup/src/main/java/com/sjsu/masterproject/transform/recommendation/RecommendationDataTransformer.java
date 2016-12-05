package com.sjsu.masterproject.transform.recommendation;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;

import com.sjsu.masterproject.transform.DataTransformer;

public class RecommendationDataTransformer extends DataTransformer {

	// Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	// private static final String NEW_LINE_SEPARATOR = "\n";

	@Override
	protected String getTransformedLine(String input) throws Exception {
		String[] fragments = input.split(",");

		String userIdAsString = fragments[0];
		String productIdAsString = fragments[1];

		MemoryIDMigrator migrator = new MemoryIDMigrator();

		StringBuilder builder = new StringBuilder();

		// Convert the user id and product id
		builder.append(String.valueOf(migrator.toLongID(userIdAsString)));
		builder.append(COMMA_DELIMITER);
		builder.append(String.valueOf(migrator.toLongID(productIdAsString)));
		builder.append(COMMA_DELIMITER);

		// Append the preferences
		builder.append(fragments[2]);
		builder.append(COMMA_DELIMITER);
		builder.append(fragments[3]);

		// Add the new line separator
		// builder.append(NEW_LINE_SEPARATOR);

		return builder.toString();
	}

	@Override
	protected String getInputLoc() {
		return "data/recommendations/raw/";
	}

	@Override
	protected String getOutputLoc() {
		return "data/recommendations/processed/";
	}

	@Override
	protected String getTransformedLine(String input, String category) throws Exception {
		return getTransformedLine(input);
	}

}
