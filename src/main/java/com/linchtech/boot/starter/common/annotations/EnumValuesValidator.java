package com.linchtech.boot.starter.common.annotations;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @author 107
 * @date 2019/3/12 10:08
 */
public class EnumValuesValidator implements ConstraintValidator<EnumValueCheck,Object> {

    private EnumValueCheck enumValueCheck;

    @Override
    public void initialize(EnumValueCheck enumValueCheck) {
        this.enumValueCheck = enumValueCheck;
    }

    /**
     * Implements the validation logic.
     * The state of {@code value} must not be altered.
     * <p>
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return {@code false} if {@code value} does not pass the constraint
     */
    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext context) {
        if (value instanceof String) {
            for (String allowedValue : this.enumValueCheck.allowedValues()) {
                if (value.toString().equals(allowedValue)) {
                    return true;
                }
            }
        }
        if (value instanceof Integer) {
            for (String allowedValue : this.enumValueCheck.allowedValues()) {
                if (value.toString().equals(allowedValue)) {
                    return true;
                }
            }
        }
        // 判断集合类型参数
        if (value instanceof Collection) {
            Collection collection = (Collection) value;
            for (String allowedValue : this.enumValueCheck.allowedValues()) {
                if (!collection.contains(allowedValue)) {
                    return false;
                }
            }
        }
        return false;
    }
}
