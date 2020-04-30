package com.morissoft.printing.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.morissoft.printing.db.SalesOrder;
import com.morissoft.printing.services.SalesOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderNumberValidator implements ConstraintValidator<OrderNumberConstraint, String> {

	@Autowired
	private SalesOrderService service;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		try {
			SalesOrder so = service.findByOrderNumber(value);
			if (so.getStatus().equalsIgnoreCase("DELETED")) {
				return false;
			}
			if (so.getTotalOutstanding().doubleValue() <= 0L) {
				return false;
			}
		} catch (Exception e) {
			log.error("error {} on {}", e.getMessage(), this.getClass().getName());
		}
		log.info("OrderNumberValidator return {}", true);
		return true;
	}

}
