package com.sjsu.masterproject.service.impl;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sjsu.masterproject.controller.exception.RecordNotFoundException;
import com.sjsu.masterproject.controller.exception.ServiceException;
import com.sjsu.masterproject.entity.Customer;
import com.sjsu.masterproject.repository.CustomerRepository;
import com.sjsu.masterproject.service.CustomerManagementService;

@Service
public class CustomerManagementServiceImpl implements CustomerManagementService {

	@Autowired
	private CustomerRepository repository;

	@Override
	public boolean login(Customer customer) throws Exception {
		if (customer == null || StringUtils.isEmpty(customer.getId()) || StringUtils.isEmpty(customer.getPassword())) {
			throw new ServiceException("Invalid login request. Id/Password are not provided/invalid");
		}

		Customer userInDb = repository.findOne(customer.getId());
		if (userInDb != null && userInDb.getPassword().equals(customer.getPassword())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Customer getCustomer(String customerId) throws Exception {
		if (StringUtils.isEmpty(customerId)) {
			throw new ServiceException("Invalid request. Customer_id is not provided/invalid");
		}

		return repository.findOne(customerId);
	}

	@Override
	public Customer updateCustomer(Customer customer) throws Exception {
		if (customer == null) {
			throw new ServiceException("Invalid request. User is not provided/invalid");
		}

		if (!repository.exists(customer.getId())) {
			throw new RecordNotFoundException("Cannot update user: " + customer.getId() + ". Record not found in DB.");
		}

		Customer updatedCustomer = repository.save(customer);
		updatedCustomer.setPassword("xxxxxx");

		return updatedCustomer;
	}

	@Override
	public Customer createNewCustomer(Customer customer) throws Exception {
		if (customer == null) {
			throw new ServiceException("Invalid request. User is not provided/invalid");
		}

		MemoryIDMigrator migrator = new MemoryIDMigrator();
		customer.setIdAsLongString(String.valueOf(migrator.toLongID(customer.getId())));
		Customer newCustomer = repository.save(customer);
		newCustomer.setPassword("xxxxxxx");

		return newCustomer;
	}

	@Override
	public Customer getCustomerByLongId(String customerLongId) throws Exception {
		if (StringUtils.isEmpty(customerLongId)) {
			throw new ServiceException("Invalid request. customerLongId is not provided/invalid");
		}

		Customer customer = repository.findByIdAsLongString(customerLongId);
		// Obfuscate the password
		customer.setPassword("xxxxxx");

		return customer;
	}

}
