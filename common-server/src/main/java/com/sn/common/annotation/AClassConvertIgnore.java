package com.sn.common.annotation;

import java.lang.annotation.*;

/**
 * @author: songning
 * @date: 2020/2/20 22:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface AClassConvertIgnore {
}
