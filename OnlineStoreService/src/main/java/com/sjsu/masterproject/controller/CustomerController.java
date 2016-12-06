package com.sjsu.masterproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sjsu.masterproject.entity.Customer;
import com.sjsu.masterproject.service.CustomerManagementService;

@BaseRestController
public class CustomerController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerManagementService customerService;

	@RequestMapping(value = "/customer/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public boolean login(@RequestBody Customer customer) throws Exception {
		log.info("Login. Id: {}, Pwd: {}", customer.getId(), customer.getPassword());
		return customerService.login(customer);
	}

	@RequestMapping(value = "/customer/{customer_id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Customer getCustomer(@PathVariable("customer_id") String customerId) throws Exception {
		log.info("getCustomer. Customer: {}", customerId);
		return customerService.getCustomer(customerId);
	}

	@RequestMapping(value = "/customer/update", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Customer updateCustomer(@RequestBody Customer customer) throws Exception {
		log.info("updateCustomer. Customer: {}", customer.toString());
		return customerService.updateCustomer(customer);
	}

	@RequestMapping(value = "/customer/create", method = RequestMethod.PUT, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public Customer createCustomer(@RequestBody Customer customer) throws Exception {
		log.info("createCustomer. Customer: {}", customer.toString());
		return customerService.createNewCustomer(customer);
	}

	@RequestMapping(value = "/customer/signup", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Customer signup(@RequestBody Customer customer) throws Exception {
		log.info("signup. Customer: {}", customer.toString());
		return customerService.createNewCustomer(customer);
	}
}
