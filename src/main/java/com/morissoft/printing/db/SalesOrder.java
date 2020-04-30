package com.morissoft.printing.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.SalesOrderPayload;
import com.morissoft.printing.utils.DateUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

//@Getter
//@Setter
//@AllArgsConstructor(access = AccessLevel.PACKAGE)
//@NoArgsConstructor(access = AccessLevel.PACKAGE)
//@ToString(callSuper = true)
//@Accessors(chain = true)
//@Builder
//@Entity
@Getter
@Setter
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "sales_order", schema = "yohana_printing")
public class SalesOrder extends BaseEntity{
	private String orderNumber;
	private LocalDateTime completedDate;
	private BigDecimal subTotal;
	private BigDecimal discount;
	private BigDecimal paymentNet;
	private BigDecimal totalPaid;
	private BigDecimal totalOutstanding;
	private String status;
	
	@ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;
	
	public SalesOrderPayload toValueObject() {
		return new SalesOrderPayload()
				.setCustomerId(this.customer.getId())
				.setDiscount(this.discount)
				.setCustomer(this.customer.toValueObject())
				.setId(this.getId())
				.setCompletedDate(completedDate.getDayOfMonth())
				.setCompletedMonth(completedDate.getMonthValue())
				.setCompletedYear(completedDate.getYear())
				.setOrderNumber(this.orderNumber)
				.setPaymentNet(this.paymentNet)
				.setTotalOutstanding(this.totalOutstanding)
				.setTotalPaid(this.totalPaid)
				.setStatus(this.status)
				.setSubTotal(this.subTotal);
	}
}
