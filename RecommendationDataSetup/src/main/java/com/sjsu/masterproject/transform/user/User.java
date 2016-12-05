package com.sjsu.masterproject.transform.user;

import java.util.List;

public class User {

	private String _id;
	private String idAsLongString;
	private String password;
	private String firstName;
	private String lastName;
	private List<String> preferences;
	private List<String> productsPurchased;

	public User(String _id, String idAsLongString, String password, String firstName, String lastName,
			List<String> preferences, List<String> productsPurchased) {
		super();
		this._id = _id;
		this.idAsLongString = idAsLongString;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.preferences = preferences;
		this.productsPurchased = productsPurchased;
	}

	public User() {
		super();
	}

	@Override
	public String toString() {
		return "User [_id=" + _id + ", idAsLongString=" + idAsLongString + ", password=" + password + ", firstName="
				+ firstName
				+ ", lastName=" + lastName + ", preferences=" + preferences + ", productsPurchased=" + productsPurchased
				+ "]";
	}

	public String get_Id() {
		return _id;
	}

	public void set_Id(String _id) {
		this._id = _id;
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
