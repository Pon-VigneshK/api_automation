package org.op_ra.tests.atlaspracticeservice;

import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.PropertyUtils;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestStaffPortalAccountController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffPortalAccountController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_StaffPortalAccountRecord() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff_portal_account where fk_organization_id =%s and id is not null limit 1;", PropertyUtils.getValue(ORGANIZATIONIDHEADER)));

        if (data.isEmpty() || data.get("id") == null || data.get("id").equals("null")) {
            ExtentLogger.skip("No staff portal account data found for organization " +
                    getValue(ORGANIZATIONIDHEADER) + ". Skipping test.");
            throw new SkipException("No staff portal account data found for organization " +
                    getValue(ORGANIZATIONIDHEADER) + ". Skipping test.");
        }
        String fullEndpoint = ApiActions.formEndpoint("/staff-portal-account/record/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);

    }
}
