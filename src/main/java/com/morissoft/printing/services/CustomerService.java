package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.db.Customer;
import com.morissoft.printing.payload.CustomerPayload;

public interface CustomerService {

	List<Customer> findAll();

	Customer save(CustomerPayload command) throws Exception;

	Customer findById(Long id);

	Customer delete(Long id);

	List<Customer> findActiveCustomers();

}
