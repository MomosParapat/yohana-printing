package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.db.SalesOrderDetails;
import com.morissoft.printing.payload.SalesOrderDetailsPayload;
import com.morissoft.printing.payload.SalesOrderPayload;

public interface SalesOrderService {

	SalesOrder save(SalesOrderPayload command) throws Exception;

	SalesOrderDetails saveOrderDetails(SalesOrderDetailsPayload command) throws Exception;

	List<SalesOrder> findAll();

	List<SalesOrderDetails> findOrderDetailsByOrderId(Long orderId);

	SalesOrder findById(Long id);

	SalesOrderDetails findOrderDetailsById(Long id);

	List<SalesOrderPayload> findAllSalesOrder();

	SalesOrder findByOrderNumber(String orderNumber) throws Exception;

	SalesOrder delete(Long id);

	boolean existByNumber(String orderNumber);
	
	String generateOrderNumber();

}
