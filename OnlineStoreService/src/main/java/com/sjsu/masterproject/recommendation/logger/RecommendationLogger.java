package com.sjsu.masterproject.recommendation.logger;

import java.io.File;
import java.io.FileWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecommendationLogger implements Runnable {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String logFilePath;
	private String logEntry;

	public RecommendationLogger(String logFilePath, String logEntry) {
		super();
		this.logFilePath = logFilePath;
		this.logEntry = logEntry;
	}

	@Override
	public void run() {
		log.info("Running thread to log the recommendation activity");

		try {
			appendToLog();
		} catch (Exception e) {
			log.error("Error while logging to {}. Entry: {}", logFilePath, logEntry, e);
		}
	}

	private synchronized void appendToLog() throws Exception {
		log.info("logFilePath: {}, logEntry: {}", logFilePath, logEntry);
		File logFile = new File(logFilePath);

		if (!logFile.exists()) {
			logFile.createNewFile();
		}

		// Writer in the append mode
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(logFile.getAbsolutePath(), true);
			fileWriter.append(logEntry);
		} catch (Exception e) {
			log.error("Exception while trying to append to the recommendation log: {}", logFilePath);
		} finally {
			if (fileWriter != null) {
				// FLush/Close the file writer
				try {
					fileWriter.flush();
					fileWriter.close();
				} catch (Exception e) {
					log.error("Exception while flushing/closing the filewriter for: {}", logFilePath);
				}
			}
		}

	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	public String getLogEntry() {
		return logEntry;
	}

	public void setLogEntry(String logEntry) {
		this.logEntry = logEntry;
	}

}
