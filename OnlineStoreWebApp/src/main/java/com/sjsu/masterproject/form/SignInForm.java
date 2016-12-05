package com.sjsu.masterproject.form;

import javax.validation.constraints.NotNull;

public class SignInForm {

	@NotNull(message = "Invalid customerId!")
	private String customerId;

	@NotNull(message = "Invalid password!")
	private String password;

	private String forwardAction;

	private String message;

	public SignInForm(String customerId, String password, String forwardAction, String message) {
		super();
		this.customerId = customerId;
		this.password = password;
		this.forwardAction = forwardAction;
		this.message = message;
	}

	public SignInForm() {
		super();
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

	public String getForwardAction() {
		return forwardAction;
	}

	public void setForwardAction(String forwardAction) {
		this.forwardAction = forwardAction;
	}

	@Override
	public String toString() {
		return "SignInForm [customerId=" + customerId + ", password=" + password + ", forwardAction=" + forwardAction
				+ ", message=" + message + "]";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
