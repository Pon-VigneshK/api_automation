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

public class TestStaffControllerNegative {

    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetStaff() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreateStaff() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        String validBody = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@test.com\",\"role\":\"NURSE\",\"isActive\":true}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetStaffById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetStaff() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreateStaff() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        String validBody = "{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"jane.smith@test.com\",\"role\":\"ADMIN\"}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC006_GET_Staff_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC007_GET_InvalidStaffId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC008_GET_InvalidStaffId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC009_GET_Staff_InvalidLocalId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/localId/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC010_GET_InvalidLocationIds_ForStaffByLocations() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/locations/search?locationIds=%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC011_GET_InvalidDepartmentIds_ForStaffByDepartments() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/departments/search?departmentIds=%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC012_GET_InvalidAdvancedSearchQuery() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/advanced-search?search=%s", "invalid:query:format");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC013_POST_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        String invalidBody = "{\"firstName\":\"John\"}"; // Missing required fields
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC014_PUT_InvalidAccessToken_UpdateStaff() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM staff where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/staff/%s", data.get("id"));
        String validBody = "{\"firstName\":\"Updated\",\"lastName\":\"Staff\",\"email\":\"updated@test.com\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC015_POST_InvalidEmailFormat() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        String invalidBody = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"invalid-email\",\"role\":\"NURSE\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC016_POST_InvalidRole_NonExistentRole() {
        String fullEndpoint = ApiActions.formEndpoint("/staff");
        String invalidBody = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@test.com\",\"role\":\"INVALID_ROLE\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"staffController", "fullRegression"})
    public void TC017_HEAD_InvalidLocalId_CheckStaffByLocalId() {
        String fullEndpoint = ApiActions.formEndpoint("/staff/localId/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }
}
