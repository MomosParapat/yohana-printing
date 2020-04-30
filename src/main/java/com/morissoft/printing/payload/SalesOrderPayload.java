package com.morissoft.printing.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.validator.OrderNumberViolation;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@OrderNumberViolation
public class SalesOrderPayload {
	private Long id = 0L;
	private String orderNumber;
	private Long customerId;
	private Integer completedDate;
	private Integer completedMonth;
	private Integer completedYear;
	private BigDecimal subTotal = BigDecimal.ZERO;
	private BigDecimal discount = BigDecimal.ZERO;
	private BigDecimal paymentNet = BigDecimal.ZERO;
	private BigDecimal totalPaid = BigDecimal.ZERO;
	private BigDecimal totalOutstanding = BigDecimal.ZERO;
	private String status = "NEW";
	private CustomerPayload customer;

	public SalesOrder toValueEntity() {
		SalesOrder so = new SalesOrder()
				.setCompletedDate(LocalDateTime.of(completedYear, completedMonth, completedDate, 0, 0, 0))
				.setDiscount(discount).setOrderNumber(orderNumber).setPaymentNet(paymentNet).setStatus(status)
				.setSubTotal(subTotal).setTotalOutstanding(totalOutstanding).setTotalPaid(totalPaid);
		so.setId(this.getId());
		return so;
	}
}
