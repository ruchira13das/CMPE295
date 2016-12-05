package com.sjsu.masterproject.client.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.masterproject.client.OnlineStoreServiceClient;
import com.sjsu.masterproject.model.RecommendationActivityLog;

public class RecommendationLogger implements Runnable {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String serviceEndPoint;
	private RecommendationActivityLog activityLog;

	public RecommendationLogger(String serviceEndPoint, RecommendationActivityLog activityLog) {
		super();
		this.serviceEndPoint = serviceEndPoint;
		this.activityLog = activityLog;
	}

	@Override
	public void run() {
		log.info("Running thread to log the recommendation activity");

		try {
			logActivity();
		} catch (Exception e) {
			log.error("Error while logging to {}. Entry: {}", serviceEndPoint, activityLog, e);
		}
	}

	private void logActivity() throws Exception {
		log.info("RecommendationLogger: Service End point: {}", serviceEndPoint);

		boolean isSuccessful = OnlineStoreServiceClient.getRestTemplate().postForObject(serviceEndPoint, activityLog,
				Boolean.class);
		log.info("Log activity: isSuccessful:: {}", isSuccessful);
	}

	public String getServiceEndPoint() {
		return serviceEndPoint;
	}

	public void setServiceEndPoint(String serviceEndPoint) {
		this.serviceEndPoint = serviceEndPoint;
	}

	public RecommendationActivityLog getActivityLog() {
		return activityLog;
	}

	public void setActivityLog(RecommendationActivityLog activityLog) {
		this.activityLog = activityLog;
	}

}
