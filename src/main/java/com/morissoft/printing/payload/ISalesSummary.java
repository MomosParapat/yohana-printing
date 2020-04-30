package com.morissoft.printing.payload;

import java.math.BigDecimal;

public interface ISalesSummary {
	public BigDecimal getSubTotal();

	public BigDecimal getDiscount();

	public BigDecimal getPaymentNet();

	public String getCreatedBy();
}
