# API Testing Framework

This project is an end-to-end API testing framework built using Java, Maven, and TestNG. It provides a robust and scalable solution for automating API tests.

## Table of Contents

- [Architecture](#architecture)
- [Key Components](#key-components)
- [Directory Structure](#directory-structure)
- [Setup and Configuration](#setup-and-configuration)
- [Running Tests](#running-tests)
- [Test Data Management](#test-data-management)
- [Reporting](#reporting)

## Architecture

The framework follows a modular design, separating concerns for better maintainability and scalability. Key aspects of the architecture include:

- **TestNG:** Used as the testing framework for test case management, execution, and assertions.
- **RestAssured:** Leveraged for simplified API testing in Java, providing a DSL for writing expressive and readable tests.
- **ExtentReports:** Integrated for generating comprehensive and interactive HTML reports.
- **Data-Driven Testing:** Supports data-driven testing using JSON files and Excel sheets.
- **Configuration Management:** Externalizes configuration using property files.

## Key Components

- **`pom.xml`:** Defines project dependencies, plugins, and build configurations.
- **`src/main/java`:** Contains the core framework code, including:
    - **`org.op_ra.annotations`:** Custom annotations for test metadata.
    - **`org.op_ra.constants`:** Framework-level constants.
    - **`org.op_ra.enums`:** Enumerations for configuration properties, log types, etc.
    - **`org.op_ra.exceptions`:** Custom exception classes.
    - **`org.op_ra.listeners`:** TestNG listeners for customizing test execution and reporting.
    - **`org.op_ra.reports`:** Classes related to report generation (ExtentReports).
    - **`org.op_ra.requestbuilder`:** Logic for building and sending API requests.
    - **`org.op_ra.utils`:** Utility classes for various tasks like data handling, JSON manipulation, database interaction, etc.
- **`src/main/resources`:** Contains configuration files and test runner lists.
    - **`configuration`:** Property files for different environments.
    - **`runnerlist`:** Excel files defining test execution suites.
- **`src/test/java`:** Contains the actual test scripts.
- **`src/test/resources`:** Contains test-specific resources like:
    - **`config`:** Test environment configurations.
    - **`log4j2.xml`:** Logging configuration.
    - **`payload`:** Request payload templates (JSON).
    - **`testdata`:** Test data files (Excel, JSON).
    - **`testrunner`:** TestNG XML suite files.
- **`extent-test-output`:** Default directory for storing ExtentReports.

## Directory Structure

```
API_Testing_Framework/
├── extent-test-output/         # Default directory for ExtentReports
├── pom.xml                     # Maven Project Object Model file
├── src/
│   ├── main/
│   │   ├── java/org/op_ra/     # Core framework code
│   │   │   ├── annotations/
│   │   │   ├── constants/
│   │   │   ├── enums/
│   │   │   ├── exceptions/
│   │   │   ├── listeners/
│   │   │   ├── reports/
│   │   │   ├── requestbuilder/
│   │   │   └── utils/
│   │   └── resources/
│   │       ├── configuration/  # Environment-specific configuration files
│   │       └── runnerlist/     # Test execution suite definitions (Excel)
│   └── test/
│       ├── java/org/op_ra/     # Test scripts
│       └── resources/
│           ├── config/         # Test environment configurations
│           ├── log4j2.xml      # Logging configuration
│           ├── payload/        # Request payload templates (JSON)
│           ├── testdata/       # Test data files (Excel, JSON)
│           └── testrunner/     # TestNG XML suite files
└── README.md                   # This file
```

## Setup and Configuration

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 11 or higher.
    *   Apache Maven installed and configured.
2.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    ```
3.  **Navigate to the project directory:**
    ```bash
    cd API_Testing_Framework
    ```
4.  **Build the project:**
    ```bash
    mvn clean install
    ```
5.  **Configuration:**
    - Environment-specific configurations are located in `src/main/resources/configuration/`.
    - The active environment can be set (e.g., in a CI/CD pipeline or via command-line arguments).
    - Database connection details, API endpoints, and credentials should be configured in these files.

## Running Tests

Tests are executed using TestNG. You can run tests in a few ways:

1.  **Via Maven:**
    - To run a specific TestNG suite file (e.g., `open.xml` defined in `pom.xml` profiles):
      ```bash
      mvn test -Popen-tests
      ```
    - To run other suite files, you might need to modify the `maven-surefire-plugin` configuration in `pom.xml` or specify the suite file directly:
      ```bash
      mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testrunner/your_suite_file.xml
      ```
2.  **Via IDE (e.g., IntelliJ IDEA, Eclipse):**
    - Most IDEs provide direct support for running TestNG tests. Right-click on a TestNG XML file or a test class and select "Run".

## Test Data Management

The framework supports managing test data using:

-   **JSON Files:** Test data can be stored in JSON files located in `src/test/resources/testdata/`. The `DataProviderUtils` and `JsonUtils` classes provide utilities for reading and processing this data.
-   **Excel Sheets:** Test data can also be managed in Excel files. `JsonUtils` includes methods to read data from Excel and convert it to JSON for framework consumption. This is particularly useful for managing larger datasets or when business users prefer Excel.
-   **Dynamic Payloads:** The `JsonUtils` class provides methods (`generatePayload`, `updatePayload`, `modifyAppointmentJson`, `createPaymentReconciliationPayload`) to create and modify JSON request payloads dynamically using template files and test-specific data. These template JSON files are typically stored in `src/test/resources/payload/`.

## Reporting

-   **ExtentReports:** The framework uses ExtentReports to generate detailed HTML reports after test execution.
-   **Report Location:** Reports are saved in the `extent-test-output/` directory by default. The exact path and filename can be configured.
-   **Report Content:** Reports include test status (pass/fail/skip), execution time, steps, and any logged messages or exceptions.
-   **Automatic Opening:** After a test run, the main report is automatically opened in the default web browser.
-   **Failed Test Case Report:** A separate report for only failed test cases is also generated for quick analysis.
-   **Email Notification:** The framework is configured to send email notifications with test results after the suite execution. (Requires `EmailConfig` to be set up).
-   **Database Logging:** Test results (method name, status) are also logged to a database, configured via `ReportDatabaseController`.

---

This README provides a comprehensive overview of the API testing framework. For more detailed information, refer to the JavaDoc comments within the codebase.
