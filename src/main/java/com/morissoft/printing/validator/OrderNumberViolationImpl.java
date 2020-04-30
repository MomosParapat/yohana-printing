package com.morissoft.printing.validator;

import java.lang.reflect.InvocationTargetException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.morissoft.printing.services.SalesOrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderNumberViolationImpl implements ConstraintValidator<OrderNumberViolation, Object> {

	@Autowired
	private SalesOrderService salesOrderService;

	//	@Override
//	public boolean isValid(String value, ConstraintValidatorContext context) {
//		log.info("Check OrderNumberViolation {}", value);
//		return !salesOrderService.existByNumber(value);
//	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			String status = BeanUtils.getProperty(value, "status");
			String orderNumber = BeanUtils.getProperty(value, "orderNumber");
			log.info("check OrderNumberViolation {},{}", orderNumber, status);
			if (status.equalsIgnoreCase("NEW")) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
						.addNode("orderNumber").addConstraintViolation();
				return !salesOrderService.existByNumber(orderNumber);
			}
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
			log.error("OrderNumberViolation error {}", ex.getMessage());
			throw new RuntimeException(ex);
		}
		return true;
	}
}
