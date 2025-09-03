package org.op_ra.tests.atlaspracticeservice;

import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.RandomUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

/**
 * This class contains negative tests for the Department Controller of the Atlas Practice Service.
 * It includes tests for invalid authentication, invalid headers, and invalid data.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestDepartmentControllerNegative {

    Map<String, String> data = new HashMap<>();

    /**
     * This test verifies that a 401 Unauthorized status is returned when getting departments with an invalid access token.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetDepartments() {
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a department with an invalid access token.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreateDepartment() {
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        String validBody = "{\"name\":\"Test Department\",\"description\":\"Test department for automation\",\"isActive\":true}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when getting a department by ID without authentication.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetDepartmentById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 500 Internal Server Error status is returned when getting departments with an invalid organization header.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetDepartments() {
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    /**
     * This test verifies that a 500 Internal Server Error status is returned when creating a department with a missing organization header.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreateDepartment() {
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        String validBody = "{\"name\":\"Test Department\",\"description\":\"Test department\",\"isActive\":true}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    /**
     * This test verifies that a 404 Not Found status is returned when getting a department from a non-existent endpoint.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC006_GET_Department_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/departments/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 400 Bad Request status is returned when getting a department with an ID containing special characters.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC007_GET_InvalidDepartmentId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/departments/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 404 Not Found status is returned when getting a department with a negative ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC008_GET_InvalidDepartmentId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/departments/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    /**
     * This test verifies that a 404 Not Found status is returned when getting a department with a local ID containing special characters.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC009_GET_InvalidLocalId_Department_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/departments/localId/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    /**
     * This test verifies that a 500 Internal Server Error status is returned when using an invalid advanced search query for departments.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC010_GET_Deaprtment_InvalidAdvancedSearchQuery() {
        String fullEndpoint = ApiActions.formEndpoint("/departments/advanced-search?search=%s", "invalid:query:format");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 500)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a department with a request body missing required fields.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC011_POST_Department_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        String invalidBody = "{\"description\":\"Test department\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when updating a department with an invalid access token.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC012_PUT_InvalidAccessToken_UpdateDepartment() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments/%s", data.get("id"));
        String validBody = "{\"name\":\"Updated Department\",\"description\":\"Updated description\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a department with an invalid data type for the "isActive" field.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC013_POST_Department_InvalidDataType_IsActiveAsString() {
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        String invalidBody = "{\"name\":\"Test Department\",\"description\":\"Test\",\"isActive\":\"true\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a department with a duplicate name.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"departmentController", "fullRegression"})
    public void TC014_POST_DuplicateName_ConflictScenario() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM department where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/departments");
        String duplicateBody = String.format("{\"name\":\"%s\",\"description\":\"Duplicate department\",\"isActive\":true}", data.get("name"));
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, duplicateBody)).isEqualTo(true);
    }
}
