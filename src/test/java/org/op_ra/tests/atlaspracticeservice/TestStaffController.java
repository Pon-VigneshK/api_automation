package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.PropertyUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestStaffController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_StaffById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "firstName", data.get("first_name"), "lastName", data.get("last_name"), "initials", data.get("initials"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_StaffByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "firstName", data.get("first_name"), "lastName", data.get("last_name"), "initials", data.get("initials"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_ExternalStaff() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/external");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_StaffByLocations() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT sl.*, l.id as location_id FROM staff_location sl JOIN location l ON sl.fk_location_id = l.id where l.fk_organization_id = %s limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/locations/search?locationIds=%s", data.get("fk_location_id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_StaffByDepartments() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/departments/search?departmentIds=%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_StaffAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and first_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String searchQuery = String.format("id:%s", data.get("id"));
        String fullEndpoint = ApiActions.formEndpoint("/staff/advanced-search?search=%s", searchQuery);
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "firstName", data.get("first_name"), "lastName", data.get("last_name"), "initials", data.get("initials"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC007_GET_Read_StaffSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and first_name is not null and last_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/search?firstName=%s&lastName=%s", data.get("first_name"), data.get("last_name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "firstName", data.get("first_name"), "lastName", data.get("last_name"), "initials", data.get("initials"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC008_HEAD_Check_StaffByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestHeadCall(fullEndpoint), 204)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC009_POST_Create_Staff() {
        // Enhanced payload with required organizationId field and proper structure
        String requestBody = String.format("{\"localId\":\"TEST_STAFF_%d\",\"firstName\":\"Test\",\"lastName\":\"Staff\",\"email\":\"test.staff@example.com\",\"isActive\":true,\"organizationId\":%s}",
                System.currentTimeMillis(), PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER));

        String fullEndpoint = ApiActions.formEndpoint("/staff");

        // Use enhanced POST method with x-practiceid header (required by Staff endpoint)
        Response response = RequestBuilder.requestPostCallWithPracticeId(fullEndpoint, requestBody, PropertyUtils.getValue(ConfigProperties.PRACTICEIDHEADER));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);

        // Extract response data only if successful
        Object staffId = response.jsonPath().get("id");
        Object staffLocalId = response.jsonPath().get("localId");
        System.out.println("Created Staff - ID: " + staffId + ", LocalID: " + staffLocalId + ", Status: " + response.getStatusCode());
    }
}
