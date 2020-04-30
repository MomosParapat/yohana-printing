package com.morissoft.printing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morissoft.printing.db.SalesInvoice;
import com.morissoft.printing.db.SalesOrder;

public interface SalesInvoiceRepository extends JpaRepository<SalesInvoice, Long> {

	Optional<SalesInvoice> findByOrder(SalesOrder order);

	Optional<SalesInvoice> findByOrderNumber(String orderNumber);

	List<SalesInvoice> findByStatus(String status);

	Optional<SalesInvoice> findByOrderAndStatus(SalesOrder order, String status);

	@Query(value = "select * from yohana_printing.sales_invoice where date(created_at) BETWEEN :dateFrom AND :dateTo", nativeQuery = true)
	List<SalesInvoice> findByPeriod(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
}
