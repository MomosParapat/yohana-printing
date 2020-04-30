package com.morissoft.printing.services.bean;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.morissoft.printing.db.Customer;
import com.morissoft.printing.db.Items;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.CustomerPayload;
import com.morissoft.printing.payload.SalesOrderDetailsPayload;
import com.morissoft.printing.repository.CustomerRepository;
import com.morissoft.printing.services.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Component
@Transactional
public class CustomerServiceBean implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public List<Customer> findAll() {
		return customerRepository.findAll();
	}

	@Override
	public Customer save(CustomerPayload command) throws Exception {
		Customer customerOptional = findById(command.getId());
		if (customerOptional != null) {
			return customerRepository.saveAndFlush(toEntityEdit(command, customerOptional));
		} else {
			if (customerRepository.isCustomerNameExists(command.getFirstName(), command.getLastName())) {
				throw new Exception("customer with name :" + command.getFirstName() + " " + command.getLastName() + "/"
						+ command.getPhoneNumber() + " is already exist");
			}
			return customerRepository.save(command.toValueEntity());
		}
	}

	@Override
	public Customer findById(Long id) {
		log.info("Custome findById {}", id);
		Optional<Customer> cust = customerRepository.findById(id);
		if (cust.isPresent()) {
			return cust.get();
		}
		return null;
	}

	@Override
	public Customer delete(Long id) {
		Optional<Customer> findById = customerRepository.findById(id);
		if (findById.isPresent()) {
			Customer cust = findById.get();
			cust.setStatus("DELETED");
			return customerRepository.saveAndFlush(cust);
		}
		// TODO Auto-generated method stub
		return null;
	}

	Customer toEntityEdit(CustomerPayload form, Customer detail) {
		BeanUtils.copyProperties(form, detail);
		return detail;
	}

	@Override
	public List<Customer> findActiveCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findByStatusOrderByFirstName("ACTIVE");
	}

}
