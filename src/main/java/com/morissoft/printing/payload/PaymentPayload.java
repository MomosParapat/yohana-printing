package com.morissoft.printing.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.Min;

import com.morissoft.printing.db.Payments;
import com.morissoft.printing.validator.OrderNumberConstraint;

import groovy.transform.ToString;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class PaymentPayload {
	private Long id = 0L;
	private String paymentNumber;
	@Min(1000)
	private BigDecimal paymentAmount = BigDecimal.ZERO;
	private String status = "NEW";	
	@OrderNumberConstraint
	private String orderNumber;	
	private Integer paymentMethod;
	private LocalDateTime createdAt;
	private String createdBy;
	
	
	private SalesInvoicePayload invoice;

	public Payments toValueEntity() {
		Payments payments = new Payments()
				.setStatus(status)
				.setPaymentAmount(paymentAmount)
				.setPaymentMethod(this.paymentMethod)
				.setPaymentNumber(paymentNumber);
		payments.setId(this.getId());
		return payments;
	}
}
