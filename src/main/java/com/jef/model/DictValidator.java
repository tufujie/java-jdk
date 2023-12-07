package com.jef.model;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author tufujie
 * @date 2023/12/7
 */
@Slf4j
public class DictValidator implements ConstraintValidator<Dict, String> {

    private String type;

    @Override
    public void initialize(Dict constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null && !value.isEmpty()) {
            DictEnum dictLabel = DictEnum.getByType(value);
            return dictLabel != null;
        }
        return false;
    }
}