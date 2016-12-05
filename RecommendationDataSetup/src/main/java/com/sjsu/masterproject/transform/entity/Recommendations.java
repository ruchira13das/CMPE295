package com.sjsu.masterproject.transform.entity;

import java.util.List;

public class Recommendations {

	public String userId;
	public String category;
	public List<String> recommendations;

	public Recommendations() {
		super();
	}

	public Recommendations(String userId, String category, List<String> recommendations) {
		super();
		this.userId = userId;
		this.category = category;
		this.recommendations = recommendations;
	}

	@Override
	public String toString() {
		return "Recommendations [userId=" + userId + ", category=" + category + ", recommendations="
				+ recommendations + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<String> recommendations) {
		this.recommendations = recommendations;
	}
}
