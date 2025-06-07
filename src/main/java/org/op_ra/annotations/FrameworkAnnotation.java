package org.op_ra.annotations;

import org.op_ra.enums.CategoryType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to provide metadata for TestNG test methods.
 * This annotation allows specifying authors and categories for test cases,
 * which can be used for reporting and test filtering purposes.
 * <p>
 * Usage example:
 * <pre>
 * {@code
 * @FrameworkAnnotation(author = {"JohnDoe", "JaneDoe"}, category = {CategoryType.SMOKE, CategoryType.REGRESSION})
 * @Test
 * public void myTestCase() {
 *     // test logic
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME) // Annotation should be available at runtime for listeners to read
@Target(ElementType.METHOD)       // Annotation can only be applied to methods
public @interface FrameworkAnnotation {

    /**
     * Specifies the author(s) of the test case.
     *
     * @return An array of strings representing the author names. Defaults to "Unknown".
     */
    public String[] author() default "Unknown";

    /**
     * Specifies the category/categories of the test case.
     * Uses the {@link CategoryType} enum for predefined categories.
     *
     * @return An array of {@link CategoryType} enums. Defaults to {CategoryType.REGRESSION}.
     */
    public CategoryType[] category() default {CategoryType.REGRESSION}; // Default to REGRESSION if not specified
}
