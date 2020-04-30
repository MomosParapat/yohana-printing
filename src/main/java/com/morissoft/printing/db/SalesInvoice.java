package com.morissoft.printing.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.SalesInvoicePayload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "sales_invoice", schema = "yohana_printing")
public class SalesInvoice extends BaseEntity {
	private LocalDateTime completedDate;
	private BigDecimal subTotal;
	private BigDecimal discount;
	private BigDecimal paymentNet;
	private String status;
	private String orderNumber;

	@OneToOne
	@JoinColumn(name = "orderId", nullable = false)
	private SalesOrder order;

	public SalesInvoicePayload toValueObject() {
		return new SalesInvoicePayload().setCompletedDate(completedDate).setDiscount(discount)
				.setOrder(order.toValueObject()).setOrderNumber(orderNumber).setPaymentNet(paymentNet).setStatus(status)
				.setSubTotal(subTotal).setId(this.getId());

	}

}
