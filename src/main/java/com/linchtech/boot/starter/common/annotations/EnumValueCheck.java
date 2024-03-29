package com.linchtech.boot.starter.common.annotations;

import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 107
 * @date 2019/3/12 10:06
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValuesValidator.class)
public @interface EnumValueCheck {

    String message() default "非法值";

    String[] allowedValues() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
