package com.morissoft.printing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morissoft.printing.db.SalesOrderDetails;

public interface SalesOrderDetailsRepository extends JpaRepository<SalesOrderDetails, Long> {
	List<SalesOrderDetails> findByOrderId(Long orderId);
}
