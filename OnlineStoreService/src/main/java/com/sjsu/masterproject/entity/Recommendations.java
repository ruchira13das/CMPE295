package com.sjsu.masterproject.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Recommendations implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	public String id;

	public String userId;
	public String category;
	public List<String> recommendations;

	public Recommendations() {
		super();
	}

	public Recommendations(String id, String userId, String category, List<String> recommendations) {
		super();
		this.id = id;
		this.userId = userId;
		this.category = category;
		this.recommendations = recommendations;
	}

	@Override
	public String toString() {
		return "Recommendations [id=" + id + ", userId=" + userId + ", category=" + category + ", recommendations="
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
