package com.ninesky.framework;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogWrite {
    /**
     *@param 模块名字 
     */
    String modelName() default "";
     
    /**
     *@param 操作类型 
     */
    String option();
}