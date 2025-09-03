# Atlas Practice Service Testing Framework

This is an end-to-end testing framework for the Atlas Practice Service API. It is built using Java, TestNG, and Rest-Assured.

## Table of Contents

- [Overview](#overview)
- [Core Components](#core-components)
- [Project Setup](#project-setup)
- [Creating a New Test Case](#creating-a-new-test-case)
- [Running Test Cases](#running-test-cases)

## Overview

This framework is designed to provide a robust and maintainable solution for testing the Atlas Practice Service API. It includes features for:

-   **API Request Building:** A fluent API for creating and sending API requests.
-   **Authentication:** Support for various authentication methods, including OAuth2.
-   **Reporting:** Detailed test reports using ExtentReports.
-   **Data-Driven Testing:** Support for data-driven tests using JSON files.
-   **TestNG Integration:** Seamless integration with TestNG for test management and execution.
-   **Logging:** Comprehensive logging using Log4j2.

## Core Components

The framework is organized into several key packages:

-   **`org.op_ra.annotations`**: Contains custom annotations for annotating test methods with metadata, such as author and category.
-   **`org.op_ra.constants`**: Defines constant values used throughout the framework, such as file paths and report names.
-   **`org.op_ra.enums`**: Contains enumerations that define a set of named constants for configuration properties, log types, and more.
-   **`org.op_ra.exceptions`**: Includes custom exceptions for handling specific error conditions that may occur during test execution.
-   **`org.op_ra.listeners`**: Contains TestNG listeners that are used to customize the behavior of the framework, such as retrying failed tests and generating reports.
-   **`org.op_ra.reports`**: Responsible for generating and managing test reports using ExtentReports.
-   **`org.op_ra.requestbuilder`**: Provides classes for building and sending API requests.
-   **`org.op_ra.utils`**: A collection of utility classes that provide various helper functions to support the framework's core operations.
-   **`org.op_ra.tests`**: Contains the actual test cases for the Atlas Practice Service.

## Project Setup

To set up the project, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    ```
2.  **Install dependencies:**
    This is a Maven project, so all dependencies are managed in the `pom.xml` file. To install the dependencies, run the following command from the root of the project:
    ```bash
    mvn clean install
    ```
3.  **Configure the environment:**
    The framework uses a configuration file to manage environment-specific settings. The main configuration file is located at `src/test/resources/config/atlas_config.properties`. You can modify this file to configure the base URL, authentication details, and other settings.

    The framework also uses a JSON file for database configuration, located at `src/main/resources/configuration/DataBaseConfig.json`, and another for cloud database configuration at `src/main/resources/configuration/CloudDatabaseConfig.json`.

## Creating a New Test Case

To create a new test case, follow these steps:

1.  **Create a new test class:**
    Create a new Java class in the `src/test/java/org/op_ra/tests/atlaspracticeservice` package. The class name should be descriptive of the functionality being tested (e.g., `TestNewFeatureController.java`).

2.  **Write the test method:**
    Create a new public method in the test class and annotate it with `@Test`. The test method should follow the Arrange-Act-Assert pattern.

    -   **Arrange:** Set up the test data and preconditions. You can use the `DatabaseQueryExecutor` to fetch data from the database or the `RandomUtils` to generate random data.
    -   **Act:** Perform the API call using the `RequestBuilder` class.
    -   **Assert:** Verify the response using the `Assertions` class from AssertJ and the helper methods in the `ApiActions` class.

3.  **Add annotations:**
    Add the `@FrameworkAnnotation` to the test method to specify the author and category of the test. This information will be used in the test report.

Here is an example of a new test case:

```java
package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.testng.annotations.Test;

public class TestNewFeatureController {

    @FrameworkAnnotation(author = {"Your Name"}, category = {CategoryType.SMOKE})
    @Test(groups = {"newFeatureController", "smoke"})
    public void TC001_GET_Read_NewFeature() {
        // Arrange
        String fullEndpoint = ApiActions.formEndpoint("/new-feature/%s", "123");

        // Act
        Response response = RequestBuilder.requestGetCall(fullEndpoint);

        // Assert
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }
}
```

## Running Test Cases

To run the test cases, you can use the TestNG suite XML file located at `src/test/resources/testrunner/atlaspractice.xml`.

You can run the tests using Maven from the command line:

```bash
mvn test -P atlas-tests
```

You can also run specific test groups by passing the `testng.groups` property:

```bash
mvn test -P atlas-tests -Dtestng.groups=smoke
```

This will run all the tests that belong to the "smoke" group.
