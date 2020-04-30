package com.morissoft.printing.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SalesInvoicePayload {
	private Long id;
	private LocalDateTime completedDate;
	private BigDecimal subTotal = BigDecimal.ZERO;
	private BigDecimal discount = BigDecimal.ZERO;
	private BigDecimal paymentNet = BigDecimal.ZERO;
	private String status = "NEW";
	private String orderNumber;

	private SalesOrderPayload order;

}
