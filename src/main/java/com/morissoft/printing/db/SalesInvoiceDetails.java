package com.morissoft.printing.db;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@Entity
@Table(name = "sales_invoice_detail", schema = "yohana_printing")
public class SalesInvoiceDetails extends BaseEntity {
	private Long invoiceId;
	private Long itemId;
	private Integer qty;
	private BigDecimal price;
	private BigDecimal subTotal;
	private BigDecimal lineDisc;
	private BigDecimal lineTotal;
}
