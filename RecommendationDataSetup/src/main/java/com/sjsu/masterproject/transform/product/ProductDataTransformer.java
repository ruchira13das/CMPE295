package com.sjsu.masterproject.transform.product;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;

import com.google.gson.Gson;
import com.sjsu.masterproject.transform.DataTransformer;

public class ProductDataTransformer extends DataTransformer {

	@Override
	protected final String getTransformedLine(String inputJson, String category) throws Exception {
		MemoryIDMigrator migrator = new MemoryIDMigrator();
		Gson gson = new Gson();

		RawJsonProduct rawJsonProduct = gson.fromJson(inputJson, RawJsonProduct.class);

		Product product = new Product();
		product.set_Id(rawJsonProduct.getAsin());
		product.setIdAsLongString(String.valueOf(migrator.toLongID(rawJsonProduct.getAsin())));
		product.setBrand(rawJsonProduct.getBrand());
		product.setTitle(rawJsonProduct.getTitle());
		product.setDescription(rawJsonProduct.getDescription());
		product.setImUrl(rawJsonProduct.getImUrl());
		product.setPrice(rawJsonProduct.getPrice());
		product.setCategory(category);

		return gson.toJson(product);
	}

	@Override
	protected final String getInputLoc() {
		return "data/product/raw/";
	}

	@Override
	protected final String getOutputLoc() {
		return "data/product/processed/";
	}

	class RawJsonProduct {
		private String asin;
		private String description;
		private String title;
		private double price;
		private String imUrl;
		private String brand;
		private String category;

		public RawJsonProduct() {
			super();
		}

		public RawJsonProduct(String asin, String description, String title, long price, String imUrl, String brand,
				String category) {
			super();
			this.asin = asin;
			this.description = description;
			this.title = title;
			this.price = price;
			this.imUrl = imUrl;
			this.brand = brand;
			this.category = category;
		}

		@Override
		public String toString() {
			return "AmazonProduct [asin=" + asin + ", description=" + description + ", title=" + title + ", price="
					+ price + ", imUrl=" + imUrl + ", brand=" + brand + ", category=" + category + "]";
		}

		public String getAsin() {
			return asin;
		}

		public void setAsin(String asin) {
			this.asin = asin;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public String getImUrl() {
			return imUrl;
		}

		public void setImUrl(String imUrl) {
			this.imUrl = imUrl;
		}

		public String getBrand() {
			return brand;
		}

		public void setBrand(String brand) {
			this.brand = brand;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}
	}

	@Override
	protected String getTransformedLine(String input) throws Exception {
		// No impl required
		return null;
	}

}
