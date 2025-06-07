package org.op_ra.utils;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utility class focused on fetching data using predefined SQL queries.
 * This class acts as a higher-level abstraction over {@link DatabaseQueryExecutor}
 * by potentially using named queries or query keys to retrieve data, rather than
 * requiring raw SQL strings directly in test scripts.
 * <p>
 * It might interact with {@link JsonUtils} to get SQL query strings mapped to keys from a JSON file.
 * </p>
 * <p>
 * <b>Note:</b> The current content of this file is largely a placeholder.
 * It should be implemented to provide convenient methods for fetching specific datasets
 * needed by tests.
 * </p>
 * Example Usage:
 * <pre>{@code
 * List<Map<String, Object>> activeUsers = FetchDataFromQuery.getActiveUsers();
 * Map<String, Object> productDetails = FetchDataFromQuery.getProductDetailsById("prod123");
 * }</pre>
 */
public final class FetchDataFromQuery {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FetchDataFromQuery() {
        // Private constructor
    }

    /**
     * Fetches data from the database using a SQL query string obtained from {@link JsonUtils#getQueryDetails(String)}.
     * The specific query is identified by the {@code queryKey} within the group of queries defined by {@code queryType}.
     *
     * @param queryType The type or group of queries (e.g., "select", "user_queries") as defined in the SQL query JSON file.
     * @param queryKey  The specific key or name of the query within the given {@code queryType}.
     * @return A {@code List<Map<String, Object>>} containing the results of the executed query.
     *         Returns an empty list if the query is not found, fails to execute, or returns no data.
     * @throws RuntimeException if the underlying database query execution fails.
     * @see JsonUtils#getQueryDetails(String)
     * @see DatabaseQueryExecutor#executeSelectQuery(String)
     */
    public static List<Map<String, Object>> fetchData(String queryType, String queryKey) {
        Map<String, Object> queries = JsonUtils.getQueryDetails(queryType);
        String sqlQuery = (String) queries.get(queryKey);

        if (sqlQuery == null || sqlQuery.trim().isEmpty()) {
            System.err.println("FetchDataFromQuery: SQL query not found for type '" + queryType + "' and key '" + queryKey + "'.");
            // FrameworkLogger.log(LogType.WARN, "SQL query not found for type '" + queryType + "' and key '" + queryKey + "'.");
            return new ArrayList<>(); // Return empty list if query is not found
        }

        // FrameworkLogger.log(LogType.INFO, "Executing query for key '" + queryKey + "': " + sqlQuery);
        return DatabaseQueryExecutor.executeSelectQuery(sqlQuery);
    }

    /**
     * Example method: Fetches all active users from the database.
     * This method would look up a predefined query (e.g., key "getActiveUsers" in type "userQueries")
     * and execute it.
     *
     * @return A list of maps, where each map represents an active user'''s data.
     * @deprecated This is an example placeholder. Implement with actual query lookup and execution.
     */
    @Deprecated
    public static List<Map<String, Object>> getActiveUsers() {
        // Example: return fetchData("userQueries", "getActiveUsersSQL");
        System.out.println("FetchDataFromQuery.getActiveUsers is a placeholder.");
        return new ArrayList<>();
    }

    /**
     * Example method: Fetches details for a specific product by its ID.
     * This method might construct a query dynamically or use a parameterized query.
     *
     * @param productId The ID of the product to fetch.
     * @return A map containing the product details, or null/empty map if not found.
     * @deprecated This is an example placeholder. Implement with actual query logic.
     */
    @Deprecated
    public static Map<String, Object> getProductDetailsById(String productId) {
        // Example:
        // String query = "SELECT * FROM products WHERE id = '" + productId + "'"; // Vulnerable to SQL injection
        // List<Map<String, Object>> results = DatabaseQueryExecutor.executeSelectQuery(query);
        // if (!results.isEmpty()) {
        //     return results.get(0);
        // }
        System.out.println("FetchDataFromQuery.getProductDetailsById is a placeholder. ProductID: " + productId);
        return new HashMap<>();
    }
}
