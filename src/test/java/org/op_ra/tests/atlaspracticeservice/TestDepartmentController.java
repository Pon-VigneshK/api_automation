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

public class TestDepartmentController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_DepartmentById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_DepartmentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_DepartmentsWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/departments?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_DepartmentSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/search?name=%s", data.get("name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_DepartmentAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC006_POST_Create_Department() {
        // Enhanced payload with required organizationId field and proper structure
        String requestBody = String.format("{\"name\":\"Test Department %d\",\"description\":\"Test department for automation\",\"isActive\":true,\"organizationId\":%s}",
                System.currentTimeMillis(), PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER));

        String fullEndpoint = ApiActions.formEndpoint("/departments");

        // Use enhanced POST method with x-practiceid header (required by Department endpoint)
        Response response = RequestBuilder.requestPostCallWithPracticeId(fullEndpoint, requestBody, PropertyUtils.getValue(ConfigProperties.PRACTICEIDHEADER));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);

        // Extract response data only if successful
        Object departmentId = response.jsonPath().get("id");
        Object departmentName = response.jsonPath().get("name");
        System.out.println("Created Department - ID: " + departmentId + ", Name: " + departmentName + ", Status: " + response.getStatusCode());
    }

}
