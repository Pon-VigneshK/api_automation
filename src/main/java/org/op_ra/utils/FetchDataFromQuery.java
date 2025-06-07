package org.op_ra.utils;

import java.util.HashMap;
import java.util.Map;

public class FetchDataFromQuery {
    public static Map<String, String> fetchDataFromQuery(String query) {
        Map<String, String> data = new HashMap<>();
        data = DatabaseQueryExecutor.retrieveRowData(query);
        return data;
    }
}
