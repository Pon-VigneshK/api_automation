package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

/**
 * This class contains tests for the Admin Controller of the Atlas Practice Service.
 * It includes tests for reprocessing data.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestAdminController {
    Map<String, String> data = new HashMap<>();

    /**
     * This test verifies the admin reprocess endpoint.
     * It fetches a staff portal account from the database, creates a request body,
     * and sends a POST request to the reprocess endpoint.
     * It asserts that the response status code is 200.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"adminController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC001_POST_Admin_Reprocess() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT id FROM staff_portal_account WHERE fk_organization_id = " + getValue(ORGANIZATIONIDHEADER) + " AND id IS NOT NULL LIMIT 1");

        if (data.isEmpty() || data.get("id") == null) {
            ExtentLogger.skip("No staff portal account data found for organization " +
                    getValue(ORGANIZATIONIDHEADER) + ". Skipping test.");
            throw new SkipException("No staff portal account data found for organization " +
                    getValue(ORGANIZATIONIDHEADER) + ". Skipping test.");
        }

        String tableName = "staff_portal_account";
        String recordId = data.get("id");
        String requestBody = String.format("{\"tableName\":\"%s\",\"ids\":[%s]}", tableName, recordId);

        String fullEndpoint = ApiActions.formEndpoint("/admin/reprocess");
        Response response = RequestBuilder.requestPostCall(fullEndpoint, requestBody);

        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }
}
