package org.op_ra.enums;

/**
 * Enumeration of test category types.
 * Used by the {@link org.op_ra.annotations.FrameworkAnnotation} to classify test cases.
 * Categories help in organizing tests and can be used for selective test execution or reporting.
 */
public enum CategoryType {
    /** Represents smoke tests, typically run to verify basic functionality. */
    SMOKE,
    /** Represents regression tests, run to ensure existing functionality remains intact after changes. */
    REGRESSION,
    /** Represents sanity tests, a subset of regression tests focusing on core features. */
    SANITY,
    /** Represents end-to-end tests, verifying entire workflows or user scenarios. */
    E2E,
    /** Represents functional tests, verifying specific functions or features of the application. */
    FUNCTIONAL,
    /** Represents performance tests, evaluating the responsiveness and stability under load. */
    PERFORMANCE,
    /** Represents security tests, focused on identifying vulnerabilities. */
    SECURITY,
    /** Represents usability tests, evaluating the user-friendliness of the application. */
    USABILITY,
    /** Represents API specific tests. */
    API,
    /** Represents UI specific tests. */
    UI,
    /** Represents tests that are currently failing and need attention. */
    FAILING,
    /** Represents tests that are currently skipped. */
    SKIPPED,
    /** Represents BDD (Behavior Driven Development) tests. */
    BDD
}
