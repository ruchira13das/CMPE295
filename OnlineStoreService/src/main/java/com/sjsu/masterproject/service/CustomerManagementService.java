package com.sjsu.masterproject.service;

import com.sjsu.masterproject.entity.Customer;

public interface CustomerManagementService {

	public boolean login(Customer customer) throws Exception;

	public Customer getCustomer(String customerId) throws Exception;

	public Customer getCustomerByLongId(String customerLongId) throws Exception;

	public Customer updateCustomer(Customer customer) throws Exception;

	public Customer createNewCustomer(Customer customer) throws Exception;

}
