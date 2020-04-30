package com.morissoft.printing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.payload.ISalesSummary;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
	@Query(value = "SELECT a.*,concat(b.first_name,' ',b.last_name) customerName FROM yohana_printing.sales_order a left join yohana_printing.customers b on a.customer_id=b.id", nativeQuery = true)
	List<SalesOrder> findAllSalesOrder();

	Optional<SalesOrder> findByOrderNumber(String orderNumber);

	@Query(value = "SELECT CONCAT('SO',LPAD(SUBSTRING(COALESCE(MAX(order_number),'SO0000'),4,4)+1,4,'0')) FROM yohana_printing.sales_order", nativeQuery = true)
	String generateOrderNumber();

	@Query(value = "SELECT * FROM yohana_printing.sales_order WHERE status = 'ISSUED' AND total_outstanding>0", nativeQuery = true)
	List<SalesOrder> findAccountReceivableReport();

	@Query(value = "SELECT SUM(sub_total) subTotal,SUM(discount) as discount, SUM(payment_net) as paymentNet,created_by as createdBy FROM yohana_printing.sales_order "
			+ " WHERE date(created_at) BETWEEN :dateFrom AND :dateTo AND status ='ISSUED'"
			+ " GROUP BY created_by", nativeQuery = true)
	List<ISalesSummary> findSalesByPeriod(@Param("dateFrom") String dateFrom, @Param("dateTo") String dateTo);
}
