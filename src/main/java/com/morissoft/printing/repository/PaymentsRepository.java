package com.morissoft.printing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morissoft.printing.db.Payments;
import com.morissoft.printing.db.SalesInvoice;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {
	List<Payments> findByInvoice(SalesInvoice invoice);

	@Query(value = "SELECT CONCAT('KWI-',LPAD(SUBSTRING(COALESCE(MAX(payment_number),'KWI-00000'),5,5)+1,5,'0')) FROM yohana_printing.payment", nativeQuery = true)
	String generatePaymentNumber();
	
	@Query(value = "SELECT * FROM yohana_printing.payment WHERE date(created_at) BETWEEN :dateFrom AND :dateTo AND status <>'VOIDED'", nativeQuery = true)
	List<Payments> findByPeriod(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
	
}
