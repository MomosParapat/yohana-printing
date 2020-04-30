package com.morissoft.printing.db;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.PaymentEnum;
import com.morissoft.printing.payload.PaymentPayload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "payment", schema = "yohana_printing")
public class Payments extends BaseEntity {
	private String paymentNumber;
	private BigDecimal paymentAmount;
	private String status;
	private Integer paymentMethod;
	@ManyToOne
	@JoinColumn(name = "invoiceId", nullable = false)
	private SalesInvoice invoice;

	public PaymentPayload toValueObject() {
		PaymentPayload payload = new PaymentPayload();
		payload.setId(this.getId());
		payload.setOrderNumber(this.getInvoice().getOrder().getOrderNumber());
		payload.setPaymentAmount(this.getPaymentAmount());
		payload.setPaymentNumber(this.getPaymentNumber());
		payload.setStatus(this.getStatus());
		payload.setInvoice(this.invoice.toValueObject());
		payload.setPaymentMethod(this.paymentMethod);
		payload.setCreatedAt(createdAt);
		payload.setCreatedBy(createdBy);
		return payload;
	}
}
