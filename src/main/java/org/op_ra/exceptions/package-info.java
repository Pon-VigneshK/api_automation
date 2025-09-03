/**
 * The `org.op_ra.exceptions` package contains custom exceptions used throughout the testing framework.
 * These exceptions are designed to handle specific error conditions that may occur during test execution,
 * providing more context than standard Java exceptions.
 *
 * <p>This package includes the following key exceptions:</p>
 * <ul>
 *   <li>{@link org.op_ra.exceptions.FrameworkException} - A generic exception for framework-related errors.</li>
 *   <li>{@link org.op_ra.exceptions.InvalidPathForExcelException} - Thrown when the path to an Excel file is invalid.</li>
 *   <li>{@link org.op_ra.exceptions.InvalidPathForFilesException} - Thrown for general file path issues.</li>
 *   <li>{@link org.op_ra.exceptions.JsonExceptions} - Thrown for errors related to JSON processing.</li>
 *   <li>{@link org.op_ra.exceptions.PropertyFileUsageException} - Thrown for errors related to property file usage.</li>
 * </ul>
 *
 * <p>Use cases for exceptions in this package include:</p>
 * <ul>
 *   <li>Providing clear and specific error messages when a file path is incorrect.</li>
 *   <li>Handling issues related to reading or parsing configuration files.</li>
 *   <li>Signaling framework-specific problems that require special handling.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
package org.op_ra.exceptions;