package com.sjsu.masterproject.form;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;

public class SignUpForm {
	@NotNull(message = "Invalid customerId!")
	private String customerId;

	@NotNull(message = "Invalid password!")
	private String password;

	@NotNull(message = "Invalid first name!")
	private String firstName;

	@NotNull(message = "Invalid last name!")
	private String lastName;

	@NotNull(message = "Invalid preferences!")
	private List<String> preferences;

	private String forwardAction;

	private String message;

	public SignUpForm(String customerId, String password, String firstName, String lastName, List<String> preferences,
			String forwardAction, String message) {
		super();
		this.customerId = customerId;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.preferences = preferences;
		this.forwardAction = forwardAction;
		this.message = message;
	}

	public SignUpForm() {
		super();
	}

	@Override
	public String toString() {
		return "SignUpForm [customerId=" + customerId + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", preferences=" + preferences + ", forwardAction=" + forwardAction
				+ ", message=" + message + "]";
	}

	public boolean isValid() {
		boolean isValid = false;

		if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(password)
				&& StringUtils.isNotBlank(customerId)) {
			isValid = true;
		}

		return isValid;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

	public String getForwardAction() {
		return forwardAction;
	}

	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
