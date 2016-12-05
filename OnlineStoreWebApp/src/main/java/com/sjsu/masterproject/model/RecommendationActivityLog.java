package com.sjsu.masterproject.model;

import java.io.Serializable;

import org.springframework.util.StringUtils;

public class RecommendationActivityLog implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String productId;
	private String category;
	private String preference;

	public RecommendationActivityLog() {
		super();
	}

	public RecommendationActivityLog(String userId, String productId, String category, String preference) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.preference = preference;
		this.category = category;
	}

	@Override
	public String toString() {
		return "RecommendationActivityLog [userId=" + userId + ", productId=" + productId + ", preference=" + preference
				+ ", category=" + category + "]";
	}

	public boolean isValid() {
		if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId) || StringUtils.isEmpty(category)
				|| StringUtils.isEmpty(preference)) {
			return false;
		} else {
			return true;
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
