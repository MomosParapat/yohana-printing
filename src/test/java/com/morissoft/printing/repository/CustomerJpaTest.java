package com.morissoft.printing.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

import com.morissoft.printing.db.Customer;
import com.morissoft.printing.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CustomerJpaTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private CustomerRepository customerRepository;

	Customer customerData;

	@Before
	public void setUp() {
		Customer customer = new Customer()
				.setFirstName("Momos").setLastName("Parapat").setAddress("Bekasi")
				.setEmail("iphonp@yahoo.com").setPhoneNumber("0812809903366").setStatus("NEW");
		customerData = testEntityManager.persist(customer);
		log.info("C U S T O M E R {}",customerData);
	}

	@Test
	public void WhenFindByName_thenReturnCustomer() {
		Customer _customer = customerRepository.findByEmail("iphonp@yahoo.com").get();
		log.info("Created By : {}", _customer.getCreatedBy());
		assertThat(_customer.getFirstName()).isEqualTo("Momos");
		assertThat(_customer.getLastName()).isEqualTo("Parapat");
		assertThat(_customer.getAddress()).isEqualTo("Bekasi");
		assertThat(_customer.getEmail()).isEqualTo("iphonp@yahoo.com");
		assertThat(_customer.getPhoneNumber()).isEqualTo("0812809903366");
	}

	@Test
	public void WhenIsCustomerExists_thenReturnTrue() {
		boolean exist = customerRepository.isCustomerExists("Momos", "Parapat", "iphonp@yahoo.com", "0812809903366");
		assertThat(exist).isEqualTo(true);
	}

	@Test
	public void WhenIsCustomerNotExists_thenReturnFalse() {
		boolean exist = customerRepository.isCustomerExists("Momo", "Parapat", "iphonp@yahoo.com", "0812809903366");
		assertThat(exist).isEqualTo(false);
	}

	@Test
	public void WhendFindAll_thenReturnCustomerList() {
		List<Customer> customers = customerRepository.findAll();
		assertThat(customers).hasSize(1);
	}

	@Test
	public void WhenDeleteByIdFromRepository_thenthenDeletingShouldBeSuccessful() {
		customerRepository.deleteById(customerData.getId());
		assertThat(customerRepository.count()).isEqualTo(0);
	}

	@Test
	public void WhenDeleteAllFromRepository_thenDeletingShouldBeEmpty() {
		customerRepository.deleteAll();
		assertThat(customerRepository.count()).isEqualTo(0);
	}
}
