package org.jasonsui.commons.util;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtil {
    public static void validator(Object modal, Class<?>... groups){
        final Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory()
                .getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(modal, groups);
        if(violations.size()>0){
            String message = violations.iterator().next().getMessage();
            throw new RuntimeException(message);
        }
    }
}
