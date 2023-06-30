package com.linchtech.boot.starter.common.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValueValid {

    String message() default "非法值";

    String[] allowedValues() default {};
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
