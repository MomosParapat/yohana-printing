package com.morissoft.printing.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ItemPriceQtyConstraint.List.class) // only with hibernate-validator >= 6.x
@Constraint(validatedBy = ItemPriceQtyValidator.class)
@Documented
public @interface ItemPriceQtyConstraint {
	String fieldName();

	String fieldValue();

	String dependFieldName();

	String message() default "{NotNullIfAnotherFieldHasValue.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		ItemPriceQtyConstraint[] value();
	}
}
