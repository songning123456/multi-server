package com.sn.common.annotation;

import java.lang.annotation.*;

/**
 * @author songning
 * @date 2020/1/21
 * description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ClassConvertIgnore {

}
