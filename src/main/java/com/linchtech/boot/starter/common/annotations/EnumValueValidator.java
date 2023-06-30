package com.linchtech.boot.starter.common.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<EnumValueValid, Object> {

    private EnumValueValid enumValueValid;

    @Override
    public void initialize(EnumValueValid enumValueValid) {
        this.enumValueValid = enumValueValid;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof String) {
            for (String validValue : this.enumValueValid.allowedValues()) {
                if (validValue.equals(value)) {
                    return true;
                }
            }
        }
        if (value instanceof Integer) {
            for (String validValue : this.enumValueValid.allowedValues()) {
                if (validValue.equals(value.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
