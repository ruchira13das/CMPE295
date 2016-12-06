package com.sjsu.masterproject.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sjsu.masterproject.model.Purchase;

public class CustomerForm {
	
	private String firstName;
	private String lastName;
	private String password;
	private String customerId;
	private List<String> preferences;
	
	private List<Purchase> purchases;
	
	private String message;

	public CustomerForm() {
		super();
	}

	public CustomerForm(String firstName, String lastName, String password, String customerId, List<String> preferences,
			List<Purchase> purchases, String message) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.customerId = customerId;
		this.preferences = preferences;
		this.purchases = purchases;
		this.message = message;
	}

	@Override
	public String toString() {
		return "CustomerForm [firstName=" + firstName + ", lastName=" + lastName + ", password=" + password
				+ ", customerId=" + customerId + ", preferences=" + preferences + ", purchases=" + purchases
				+ ", message=" + message + "]";
	}
	
	public boolean isValid() {
		boolean isValid = false;
		if(StringUtils.isNotBlank(firstName) || StringUtils.isNotBlank(lastName)) {
			isValid = true;
		}
		
		return isValid;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<String> getPreferences() {
		return preferences;
	}

	public void setPreferences(List<String> preferences) {
		this.preferences = preferences;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
