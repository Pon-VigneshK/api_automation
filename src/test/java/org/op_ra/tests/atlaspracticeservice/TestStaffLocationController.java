package org.op_ra.tests.atlaspracticeservice;

import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestStaffLocationController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffLocationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_StaffLocationById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff_location where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff-locations/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "staffId", data.get("fk_staff_id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffLocationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_StaffLocationByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff_location where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff-locations/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "staffId", data.get("fk_staff_id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }
}
