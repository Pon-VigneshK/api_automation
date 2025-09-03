package org.op_ra.annotations;

import org.op_ra.enums.CategoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * {@code @FrameworkAnnotation} is a custom annotation used to mark test methods with metadata,
 * such as the author and category of the test. This information can be used for reporting,
 * filtering, and test management.
 *
 * <p>This annotation should be applied to test methods to provide additional context. The information
 * can be retrieved at runtime using reflection to create more detailed test reports or to
 * control test execution based on categories.</p>
 *
 * <p><b>Use Case:</b></p>
 * <p>You can use this annotation to specify the author and category for a TestNG test method.
 * This information can then be used by a TestNG listener to log additional details or to
* dynamically include or exclude tests from a run.</p>
 *
 * <pre>{@code
 * import org.op_ra.annotations.FrameworkAnnotation;
 * import org.op_ra.enums.CategoryType;
 * import org.testng.annotations.Test;
 *
 * public class SampleTest {
 *
 *     @Test
 *     @FrameworkAnnotation(author = {"John Doe"}, category = {CategoryType.SMOKE, CategoryType.REGRESSION})
 *     public void myTestMethod() {
 *         // Test implementation
 *     }
 * }
 * }</pre>
 *
 * @see org.op_ra.listeners.ListenerClass
 * @see org.op_ra.enums.CategoryType
 * @version 1.0
 * @since 2023-10-27
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface FrameworkAnnotation {

    /**
     * The author(s) of the test case. This can be used to identify the owner of a test.
     * It is an array of strings, so multiple authors can be specified.
     *
     * @return An array of author names.
     */
    String[] author() default "";

    /**
     * The category or categories of the test case. This can be used to group tests by functionality
     * or type (e.g., SMOKE, REGRESSION).
     *
     * @return An array of {@link CategoryType} enums.
     */
    CategoryType[] category();

}
