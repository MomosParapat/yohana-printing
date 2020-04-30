package com.morissoft.printing.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.morissoft.printing.base.BaseEntity;
import com.morissoft.printing.payload.SalesOrderDetailsPayload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
@Table(name = "sales_order_detail", schema = "yohana_printing")
public class SalesOrderDetails extends BaseEntity {
	@Column(nullable = false)
	private Long orderId;
	
	@Column(nullable = false)
	private Integer qty;
	
	@Column(nullable = false)
	private BigDecimal price;
	
	@Column(nullable = false)
	private BigDecimal subTotal;
	
	private BigDecimal lineDisc;
	
	@Column(nullable = false)
	private BigDecimal lineTotal;
	
	private String type;

	private Double width;
	
	private Double length;

	@ManyToOne
	@JoinColumn(name = "itemId", nullable = false)
	private Items items;

	public SalesOrderDetailsPayload toValueObject() {
		return new SalesOrderDetailsPayload().setId(this.getId()).setItems(this.items.toValueObject())
				.setLineDisc(this.lineDisc).setLineTotal(this.lineTotal).setOrderId(this.orderId).setPrice(this.price)
				.setQty(this.qty).setSubTotal(this.subTotal).setWidth(this.width).setLength(length).setType(type);
	}

}
