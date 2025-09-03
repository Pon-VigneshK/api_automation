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

public class TestAdminController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"adminController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC001_POST_Admin_Reprocess() {
        // Get test data for reprocessing - only staff_portal_account is supported for admin reprocess
        data = DatabaseQueryExecutor.retrieveRowData("SELECT id FROM staff_portal_account WHERE fk_organization_id = " + getValue(ORGANIZATIONIDHEADER) + " AND id IS NOT NULL LIMIT 1");

        if (data.isEmpty() || data.get("id") == null) {
            ExtentLogger.skip("No staff portal account data found for organization " +
                    getValue(ORGANIZATIONIDHEADER) + ". Skipping test.");
            throw new SkipException("No staff portal account data found for organization " +
                    getValue(ORGANIZATIONIDHEADER) + ". Skipping test.");
        }

        // Create proper request body for admin reprocess endpoint with valid data
        String tableName = "staff_portal_account";
        String recordId = data.get("id");
        String requestBody = String.format("{\"tableName\":\"%s\",\"ids\":[%s]}", tableName, recordId);

        String fullEndpoint = ApiActions.formEndpoint("/admin/reprocess");
        Response response = RequestBuilder.requestPostCall(fullEndpoint, requestBody);


        // Admin reprocess should return 200 with success message
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }
}
