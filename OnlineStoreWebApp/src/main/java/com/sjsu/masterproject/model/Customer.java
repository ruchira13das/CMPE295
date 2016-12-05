package com.sjsu.masterproject.model;

import java.io.Serializable;
import java.util.List;

public class Customer implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String idAsLongString;
	private String password;
	private String firstName;
	private String lastName;
	private List<String> preferences;
	private List<String> productsPurchased;

	public Customer(String id, String idAsLongString, String password, String firstName, String lastName,
			List<String> preferences, List<String> productsPurchased) {
		super();
		this.id = id;
		this.idAsLongString = idAsLongString;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.preferences = preferences;
		this.productsPurchased = productsPurchased;
	}

	public Customer() {
		super();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", idAsLongString=" + idAsLongString + ", password=" + password + ", firstName="
				+ firstName
				+ ", lastName=" + lastName + ", preferences=" + preferences + ", productsPurchased=" + productsPurchased
				+ "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdAsLongString() {
		return idAsLongString;
	}

	public void setIdAsLongString(String idAsLongString) {
		this.idAsLongString = idAsLongString;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<String> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}

	public List<String> getProductsPurchased() {
		return productsPurchased;
	}

	public void setProductsPurchased(List<String> productsPurchased) {
		this.productsPurchased = productsPurchased;
	}
}
