/**
 * The `org.op_ra.reports` package contains classes responsible for generating and managing
 * test reports in the framework. This package is essential for providing detailed and
 * informative feedback on test execution.
 *
 * <p>This package includes the following key classes:</p>
 * <ul>
 *   <li>{@link org.op_ra.reports.ExtentReport} - The main class for initializing and flushing
 *       the ExtentReports instance. It also creates test entries and adds author and category
 *       information.</li>
 *   <li>{@link org.op_ra.reports.ExtentManager} - A thread-safe class for managing the
 *       {@link com.aventstack.extentreports.ExtentTest} instance. It ensures that each test
 *       has its own report entry, even when tests are run in parallel.</li>
 *   <li>{@link org.op_ra.reports.ExtentLogger} - A utility class that provides methods for
 *       logging messages at different levels (pass, fail, skip, info) in the Extent Report.</li>
 *   <li>{@link org.op_ra.reports.FrameworkLogger} - A generic logging utility that can be used
 *       to log messages to both the console and the Extent Report.</li>
 * </ul>
 *
 * <p>Use cases for classes in this package include:</p>
 * <ul>
 *   <li>Generating comprehensive HTML reports for test runs.</li>
 *   <li>Logging detailed steps, assertions, and errors for each test case.</li>
 *   <li>Managing report generation in a multithreaded environment.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
package org.op_ra.reports;