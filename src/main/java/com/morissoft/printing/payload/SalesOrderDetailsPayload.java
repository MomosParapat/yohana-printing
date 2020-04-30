package com.morissoft.printing.payload;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import com.morissoft.printing.db.SalesOrderDetails;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SalesOrderDetailsPayload {
	private Long id = 0L;
	private Long orderId = 0L;
	@Min(1)
	private Integer qty = 0;
	private BigDecimal price = BigDecimal.ZERO;
	private BigDecimal subTotal = BigDecimal.ZERO;
	private BigDecimal lineDisc = BigDecimal.ZERO;
	private BigDecimal lineTotal = BigDecimal.ZERO;
	private String type = "RETAIL";
	private Double width = Double.valueOf(0);
	private Double length = Double.valueOf(0);

	private ItemsPayload items;

	public SalesOrderDetails toValueEntity() {
		SalesOrderDetails details = new SalesOrderDetails().setOrderId(this.orderId).setLineDisc(this.lineDisc)
				.setLineTotal(this.lineTotal).setPrice(this.price).setQty(this.qty).setSubTotal(this.subTotal)
				.setWidth(width).setLength(length).setType(type);
		details.setId(this.getId());
		return details;
	}
}
