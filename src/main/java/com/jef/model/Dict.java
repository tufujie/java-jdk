package com.jef.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义校验注解
 *
 * @author tufujie
 * @date 2023/12/7
 */
@Documented
// 标明这个校验注解是使用哪个校验器进行校验的，在这里指定或者在初始化的时候指定
@Constraint(validatedBy = {DictValidator.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})  // 作用位置
@Retention(RUNTIME)  // 运行时机
public @interface Dict {
    String type() default "";

    // 必须要，否则会报错
    String message() default "请检查业务数据字典是否正确配置";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}