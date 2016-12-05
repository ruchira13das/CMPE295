package com.sjsu.masterproject.transform;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.mahout.common.iterator.FileLineIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataTransformer {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public void transformDataFile() {
		log.info("xxxxx transformDataFile xxxxxx");
		File inputLoc = new File(getInputLoc());
		Map<String, String> fileNamesMap = new HashMap<>();
		if (inputLoc.isDirectory()) {
			for (File inputFile : inputLoc.listFiles()) {
				String inputFileName = inputFile.getName();
				String fileExtn = inputFileName.endsWith(".json") ? "json" : "csv";
				fileNamesMap.put(getInputLoc() + inputFileName,
						getOutputLoc() + inputFileName.split("\\.")[0] + "_ready." + fileExtn);
			}
		} else {
			log.info("Not a directory.....");
		}

		for (String inputFileLoc : fileNamesMap.keySet()) {
			long startTime = System.nanoTime();
			processFile(inputFileLoc, fileNamesMap.get(inputFileLoc));
			log.info("Processing time for {}: {} ms", inputFileLoc, (System.nanoTime() - startTime) / 1000000);
		}
	}

	protected void processFile(String inputFileLoc, String outputFileLoc) {
		log.info("Processing file: {}", inputFileLoc);

		FileWriter fileWriter = null;
		try {
			File inputDataFile = new File(inputFileLoc);
			fileWriter = new FileWriter(outputFileLoc);

			boolean isCsv = inputFileLoc.endsWith(".csv");

			String category = getCategory(inputDataFile.getName());

			if (inputDataFile != null && inputDataFile.isFile()) {
				int i = 0;
				for (String inputJson : new FileLineIterable(inputDataFile)) {
					i++;
					// CSV files are massive. Pick the top 100K lines
					if (isCsv && i > 100000) {
						break;
					}

					try {
						// Write to file
						fileWriter.append(getTransformedLine(inputJson, category));

						// Add the new line separator
						fileWriter.append("\n");
					} catch (Exception e) {
						log.error("Skip and move on! Error while processing data line: {}. Error: {}", inputJson,
								e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception while trying to process the inital input file: {}", inputFileLoc);
		} finally {
			if (fileWriter != null) {
				// FLush/Close the file writer
				try {
					fileWriter.flush();
					fileWriter.close();
				} catch (Exception e) {
					log.error("Exception while flushing/closing the filewriter for: {}", outputFileLoc);
				}
			}
		}
		log.info("Processing complete: {}", outputFileLoc);
	}

	private String getCategory(String fileName) {
		String category = "";
		if (StringUtils.isNotEmpty(fileName)) {
			if (fileName.startsWith("meta_Baby")) {
				category = "Baby";
			} else if (fileName.startsWith("meta_Beauty")) {
				category = "Beauty";
			} else if (fileName.startsWith("meta_Books")) {
				category = "Books";
			} else if (fileName.startsWith("meta_Cell")) {
				category = "Cell Phones & Accessories";
			} else if (fileName.startsWith("meta_Musical")) {
				category = "Musical Instruments";
			} else if (fileName.startsWith("meta_Toys")) {
				category = "Toys & Games";
			}
		}

		return category;
	}

	protected abstract String getTransformedLine(String input, String category) throws Exception;

	protected abstract String getTransformedLine(String input) throws Exception;

	protected abstract String getInputLoc();

	protected abstract String getOutputLoc();
}
