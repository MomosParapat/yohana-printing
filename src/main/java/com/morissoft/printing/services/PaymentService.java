package com.morissoft.printing.services;

import java.util.List;

import com.morissoft.printing.db.Payments;
import com.morissoft.printing.db.SalesInvoice;
import com.morissoft.printing.payload.PaymentPayload;

public interface PaymentService {

	List<Payments> findAll();

	Payments save(PaymentPayload command) throws Exception;

	Payments findById(Long id) throws Exception;

	Payments voidPayment(Long id);

	SalesInvoice processByInvoice(Long Id);
	
	Payments processByPaymentId(Long id);
	
	String generatePaymentNumber();
}
