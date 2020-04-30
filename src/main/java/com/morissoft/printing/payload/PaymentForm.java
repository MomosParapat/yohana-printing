package com.morissoft.printing.payload;

import com.morissoft.printing.validator.OrderNumberConstraint;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaymentForm {
	@OrderNumberConstraint
	private String orderNumber;
}
