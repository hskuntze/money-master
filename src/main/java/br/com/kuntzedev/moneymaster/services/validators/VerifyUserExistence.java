package br.com.kuntzedev.moneymaster.services.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = VerifyUserExistenceValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyUserExistence {
	String message() default "User already exists!";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}