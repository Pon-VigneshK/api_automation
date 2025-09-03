/**
 * The `org.op_ra.requestbuilder` package contains classes that are responsible for building
 * and sending API requests. This package simplifies the process of creating and managing
 * API requests by providing a fluent API for setting up request specifications and performing
 * API actions.
 *
 * <p>This package includes the following key classes:</p>
 * <ul>
 *   <li>{@link org.op_ra.requestbuilder.RequestBuilder} - A class that provides a fluent API
 *       for building a {@link io.restassured.specification.RequestSpecification}. It allows
 *       for setting headers, query parameters, and the request body in a clean and readable way.</li>
 *   <li>{@link org.op_ra.requestbuilder.ApiActions} - A class that provides methods for performing
 *       common API actions, such as GET, POST, PUT, and DELETE. It uses the {@code RequestBuilder}
 *       to construct the request and then sends it to the specified endpoint.</li>
 *   <li>{@link org.op_ra.requestbuilder.AuthorizationManager} - A class that handles the
 *       authentication and authorization for API requests. It can manage different types of
 *       authentication, such as OAuth2 and basic authentication.</li>
 * </ul>
 *
 * <p>Use cases for classes in this package include:</p>
 * <ul>
 *   <li>Building and sending GET requests to retrieve data from an API.</li>
 *   <li>Constructing and sending POST requests with a JSON payload to create new resources.</li>
 *   <li>Managing authentication tokens and adding them to requests automatically.</li>
 * </ul>
 *
 * @version 1.0
 * @since 2023-10-27
 */
package org.op_ra.requestbuilder;
