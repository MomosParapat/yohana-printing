package com.morissoft.printing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morissoft.printing.db.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
	Optional<Customer> findByEmail(String email);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE CONCAT(c.firstName,c.lastName,c.email,c.phoneNumber)"
			+ " = CONCAT(:firstName,:lastName,:email,:phoneNumber)")
	boolean isCustomerExists(@Param("firstName") String firstName, @Param("lastName") String lastName,
			@Param("email") String email, @Param("phoneNumber") String phoneNumber);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE CONCAT(c.firstName,c.lastName)= CONCAT(:firstName,:lastName)")
	boolean isCustomerNameExists(@Param("firstName") String firstName, @Param("lastName") String lastName);
	
	List<Customer> findByStatusOrderByFirstName(String status);
}
