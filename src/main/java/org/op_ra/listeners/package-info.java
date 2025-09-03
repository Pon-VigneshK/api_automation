/**
 * The `org.op_ra.listeners` package contains TestNG listeners that are used to customize
 * the behavior of the testing framework. These listeners allow for actions to be taken at
 * various points during the test execution lifecycle, such as before a test starts, after a
 * test finishes, or when a test fails.
 *
 * <p>This package includes the following key listeners:</p>
 * <ul>
 *   <li>{@link org.op_ra.listeners.ListenerClass} - The main listener class that handles test
 *       start, success, failure, and skip events. It is responsible for logging and reporting.</li>
 *   <li>{@link org.op_ra.listeners.AnnotationTransformer} - A listener that can modify TestNG
 *       annotations at runtime, allowing for dynamic changes to test configurations.</li>
 *   <li>{@link org.op_ra.listeners.MethodInterceptor} - A listener that can alter the list of
 *       test methods that TestNG is about to run, enabling dynamic test filtering.</li>
 *   <li>{@link org.op_ra.listeners.RetryFailedTests} - A listener that implements the
 *       {@link org.testng.IRetryAnalyzer} interface to automatically retry failed tests.</li>
 * </ul>
 *
 * <p>Use cases for listeners in this package include:</p>
 * <ul>
 *   <li>Creating detailed test reports with ExtentReports.</li>
 *   <li>Automatically retrying failed tests to handle flaky tests.</li>
 *   <li>Dynamically enabling or disabling tests based on external data.</li>
 *   <li>Filtering which tests to run based on specific criteria.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
package org.op_ra.listeners;