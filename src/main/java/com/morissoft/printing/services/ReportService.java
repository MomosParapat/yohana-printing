package com.morissoft.printing.services;

import java.util.List;
import java.util.Map;

import com.morissoft.printing.payload.ISalesSummary;
import com.morissoft.printing.payload.PaymentPayload;
import com.morissoft.printing.payload.SalesOrderPayload;

public interface ReportService {
	List<PaymentPayload> findPaymentByPeriod(String dateFrom, String dateTo);

	List<SalesOrderPayload> findAccountReceivable();

	List<Map<String, Object>> findCagegoryReport();
	
	List<Map<String, Object>>  invoiceReport(Long id);
	
	List<ISalesSummary> findSalesByPeriod(String dateFrom, String dateTo);
	
	List<Map<String, Object>>  receiptReport(Long id);
	
}
