package org.op_ra.annotations;

import org.op_ra.enums.CategoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface FrameworkAnnotation {
    /**
     * Store the authors who created the tests in a String array.
     */
    String[] author() default {};

    /**
     * Stores the category in the form of an Enum Array. Include the possible values in {@link CategoryType}.
     */
    CategoryType[] category() default {};

}
