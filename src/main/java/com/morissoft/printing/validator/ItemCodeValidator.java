package com.morissoft.printing.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.morissoft.printing.db.Items;
import com.morissoft.printing.services.ItemService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemCodeValidator implements ConstraintValidator<ItemCodeConstraint, String> {

	@Autowired
	private ItemService service;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean bReturn = false;
		try {
			Items item = service.findByCode(value);
			if (item.getStatus().equalsIgnoreCase("DELETED")) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Item with code: " + value + " has been deleted")
						.addConstraintViolation();
				return false;
			}
			bReturn = item != null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error {} on {}", e.getMessage(), this.getClass().getName());

		}
		log.info("ItemCodeValidator return {}", bReturn);
		return bReturn;
	}

}
