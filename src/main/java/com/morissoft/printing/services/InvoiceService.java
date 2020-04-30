package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.db.SalesInvoice;
import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.payload.SalesInvoicePayload;
import com.morissoft.printing.payload.SalesOrderPayload;

public interface InvoiceService {

	SalesInvoice save(SalesOrderPayload command) throws Exception;

//	SalesInvoiceDetails saveOrderDetails(SalesInvoiceDetailsPayload command) throws Exception;

	List<SalesInvoice> findAll();

//	List<SalesInvoiceDetails> findOrderDetailsByOrderId(Long orderId);

	SalesInvoice findById(Long id);

//	SalesInvoiceDetails findOrderDetailsById(Long id);

	List<SalesInvoicePayload> findAllSalesOrder();

	SalesInvoice findByOrderNumber(String orderNumber) throws Exception;

	SalesInvoice delete(Long id);

	SalesInvoice deliver(Long id);

	List<SalesInvoice> findSalesReportByPeriod(String dateFrom, String dateTo);


	SalesInvoice findByActiveStatus(SalesOrder order);

}
