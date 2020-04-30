package com.morissoft.printing.examples;

import com.morissoft.printing.validator.ItemPriceQtyConstraint;

@ItemPriceQtyConstraint(fieldName = "status", fieldValue = "Canceled", dependFieldName = "fieldOne")
@ItemPriceQtyConstraint(fieldName = "status", fieldValue = "Canceled", dependFieldName = "fieldTwo")
public class DependendOtherFieldForm {
	private String status;
	private String fieldOne;
	private String fieldTwo;
}
