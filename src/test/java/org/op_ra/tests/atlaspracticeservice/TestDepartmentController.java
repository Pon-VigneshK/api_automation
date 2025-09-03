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

/**
 * This class contains tests for the Department Controller of the Atlas Practice Service.
 * It includes tests for reading and creating departments.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestDepartmentController {
    Map<String, String> data = new HashMap<>();

    /**
     * This test verifies reading a department by its ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_DepartmentById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading a department by its local ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_DepartmentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading departments with pagination.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_DepartmentsWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/departments?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    /**
     * This test verifies searching for departments by name.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_DepartmentSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/search?name=%s", data.get("name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies the advanced search for departments.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_DepartmentAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies creating a new department.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC006_POST_Create_Department() {
        String requestBody = String.format("{\"name\":\"Test Department %d\",\"description\":\"Test department for automation\",\"isActive\":true,\"organizationId\":%s}",
                System.currentTimeMillis(), PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER));

        String fullEndpoint = ApiActions.formEndpoint("/departments");

        Response response = RequestBuilder.requestPostCallWithPracticeId(fullEndpoint, requestBody, PropertyUtils.getValue(ConfigProperties.PRACTICEIDHEADER));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);

        Object departmentId = response.jsonPath().get("id");
        Object departmentName = response.jsonPath().get("name");
        System.out.println("Created Department - ID: " + departmentId + ", Name: " + departmentName + ", Status: " + response.getStatusCode());
    }
}
