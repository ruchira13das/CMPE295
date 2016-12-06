package com.sjsu.masterproject.client;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.sjsu.masterproject.model.Customer;

@Component
public class CustomerManagementServiceClient extends OnlineStoreServiceClient {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment env;

	private static final String SIGN_IN_URI = "/customer/login";
	private static final String SIGN_UP_URI = "/customer/signup";
	private static final String GET_CUSTOMER_URI = "/customer/";

	public Boolean signIn(Customer customer) throws Exception {
		log.info("signIn {}", customer);

		if (customer == null || StringUtils.isBlank(customer.getId()) || StringUtils.isBlank(customer.getPassword())) {
			throw new Exception("signIn: Customer info is invalid!");
		}

		Boolean isSignedIn = false;
		String serviceEndPoint = getBaseServiceUrl(env) + SIGN_IN_URI;

		log.info("signIn: Service End point: {}", serviceEndPoint);

		try {
			isSignedIn = getRestTemplate().postForObject(serviceEndPoint, customer, Boolean.class);
		} catch (Exception e) {
			log.error("Error while signing in: {}!", customer, e);
		}

		log.info("isSignedIn:: {}", isSignedIn);

		return isSignedIn;
	}
	
	public Customer signUp(Customer customer) throws Exception {
		log.info("signUp {}", customer);

		if (customer == null || !customer.isValidNewCustomer()) {
			throw new Exception("signIn: Customer info is invalid!");
		}

		Customer newCustomer = new Customer();
		
		String serviceEndPoint = getBaseServiceUrl(env) + SIGN_UP_URI;
		log.info("signUp: Service End point: {}", serviceEndPoint);

		try {
			newCustomer = getRestTemplate().postForObject(serviceEndPoint, customer, Customer.class);
		} catch (Exception e) {
			log.error("Error while signing up: {}!", customer, e);
		}

		log.info("Successfully created customer:: {}", newCustomer);

		return newCustomer;
	}
	
	public Customer getCustomer(String customerId) throws Exception {
		log.info("getCustomer for {}", customerId);

		if (StringUtils.isBlank(customerId)) {
			throw new Exception("getCustomer: Customer id is invalid!");
		}

		Customer customer = null;
		
		String serviceEndPoint = getBaseServiceUrl(env) + GET_CUSTOMER_URI + customerId;
		log.info("getCustomer: Service End point: {}", serviceEndPoint);

		try {
			ResponseEntity<Customer> response = getRestTemplate().exchange(serviceEndPoint, HttpMethod.GET, null,
					new ParameterizedTypeReference<Customer>() {
			});
			customer = response.getBody();
		} catch (Exception e) {
			log.error("Error while fetching customer for: {}!", customerId, e);
		}

		log.info("Customer found: {}", customer);

		return customer;
	}
}
