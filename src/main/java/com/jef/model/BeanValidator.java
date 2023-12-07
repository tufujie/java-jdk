package com.jef.model;

import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * @author tufujie
 * @date 2023/12/7
 */
public class BeanValidator {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public BeanValidator() {
    }

    public static <T> void validate(T source) throws Exception {
        Set<ConstraintViolation<T>> validators = validator.validate(source, new Class[0]);
        if (CollectionUtils.isNotEmpty(validators)) {
            StringBuilder detailMessages = new StringBuilder(256);
            StringBuilder simlpeTipMessages = new StringBuilder(128);

            ConstraintViolation validate;
            for (Iterator var4 = validators.iterator(); var4.hasNext(); simlpeTipMessages.append(validate.getMessage())) {
                validate = (ConstraintViolation) var4.next();
                detailMessages.append(validate.getRootBeanClass().getName()).append(".").append(validate.getPropertyPath()).append(":").append(validate.getMessage()).append("\t\n");
                if (simlpeTipMessages.length() > 0) {
                    simlpeTipMessages.append("，");
                }
            }

            throw new Exception(simlpeTipMessages.toString());
        }
    }
}