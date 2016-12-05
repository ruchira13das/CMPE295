package com.sjsu.masterproject.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Product implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String idAsLongString;
	private String description;
	private String shortTitle;
	private String title;
	private double price;
	private String imUrl;
	private String brand;
	private String featured;
	private String category;

	public Product() {
		super();
	}

	public Product(String id, String idAsLongString, String category, String description, String title,
			double price, String imUrl, String brand, String featured) {
		super();
		this.id = id;
		this.idAsLongString = idAsLongString;
		this.category = category;
		this.description = description;
		this.title = title;
		this.price = price;
		this.imUrl = imUrl;
		this.brand = brand;
		this.featured = featured;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the idAsLongString
	 */
	public String getIdAsLongString() {
		return idAsLongString;
	}

	/**
	 * @param idAsLongString
	 *            the idAsLongString to set
	 */
	public void setIdAsLongString(String idAsLongString) {
		this.idAsLongString = idAsLongString;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return Math.round(price * 100.0) / 100.0;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the imUrl
	 */
	public String getImUrl() {
		return imUrl;
	}

	/**
	 * @param imUrl
	 *            the imUrl to set
	 */
	public void setImUrl(String imUrl) {
		this.imUrl = imUrl;
	}

	/**
	 * @return the brand
	 */
	public String getBrand() {
		return brand;
	}

	/**
	 * @param brand
	 *            the brand to set
	 */
	public void setBrand(String brand) {
		this.brand = brand;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", idAsLong=" + idAsLongString + ", description=" + description + ", title="
				+ title + ", price=" + price + ", imUrl=" + imUrl + ", brand=" + brand + ", category=" + category
				+ ", featured=" + featured + "]";
	}

	public String getFeatured() {
		return featured;
	}

	public void setFeatured(String featured) {
		this.featured = featured;
	}

	public String getShortTitle() {
		shortTitle = title;
		if (StringUtils.isNotBlank(title) && title.length() > 25) {
			shortTitle = title.substring(0, 24).concat("...");
		}
		return shortTitle;
	}
}
