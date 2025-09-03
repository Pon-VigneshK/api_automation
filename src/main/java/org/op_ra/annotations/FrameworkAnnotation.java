package org.op_ra.annotations;

import org.op_ra.enums.CategoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface FrameworkAnnotation {
    String[] author() default "";

    CategoryType[] category();

}
