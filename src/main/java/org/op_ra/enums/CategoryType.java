package org.op_ra.enums;

/**
 * {@code CategoryType} is an enumeration that defines the categories for grouping test cases.
 * This enum is used in the {@link org.op_ra.annotations.FrameworkAnnotation} to specify the
 * category of a test method. Using categories helps in organizing tests and allows for
 * selective test execution based on the category.
 *
 * <p><b>Usage:</b></p>
 * <p>You can assign a category to a test method using the {@code @FrameworkAnnotation}.</p>
 *
 * <pre>{@code
 * @FrameworkAnnotation(category = {CategoryType.SMOKE, CategoryType.REGRESSION})
 * @Test
 * public void sampleTest() {
 *     // Test logic
 * }
 * }</pre>
 *
 * <p>The available categories are:</p>
 * <ul>
 *   <li>{@link #HIGH_LEVEL_TESTING} - For high-level, end-to-end tests.</li>
 *   <li>{@link #SMOKE} - For basic functionality tests to ensure the application is stable.</li>
 *   <li>{@link #FULL_REGRESSION} - For comprehensive regression tests covering all major features.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
public enum CategoryType {

    /**
     * Represents high-level, end-to-end tests that verify the overall functionality of the application.
     * These tests are typically broad in scope and cover major user workflows.
     */
    HIGH_LEVEL_TESTING,

    /**
     * Represents smoke tests, which are a subset of tests that cover the most important functionality
     * of a component or system. Smoke tests are used to quickly determine if a new build is stable
     * enough for further testing.
     */
    SMOKE,

    /**
     * Represents a full regression suite, which includes a comprehensive set of tests that cover
     * all major features of the application. These tests are run to ensure that new changes have
     * not introduced any regressions in existing functionality.
     */
    FULL_REGRESSION

}
