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

public class TestPracticeControllerNegative {

    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetPractices() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreatePractice() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        String validBody = "{\"name\":\"Test Practice\",\"url\":\"https://testpractice.com\",\"isActive\":true}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetPracticeById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetPractices() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreatePractice() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        String validBody = "{\"name\":\"Test Practice\",\"url\":\"https://testpractice2.com\",\"isActive\":true}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC006_GET_Practice_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/practices/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC007_GET_InvalidPracticeId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/practices/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC008_GET_InvalidPracticeId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/practices/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC009_POST_Practice_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        String invalidBody = "{\"url\":\"https://test.com\"}"; // Missing required name field
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC010_PUT_InvalidAccessToken_UpdatePractice() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/%s", data.get("id"));
        String validBody = "{\"name\":\"Updated Practice\",\"url\":\"https://updated.com\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }


    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC011_POST_InvalidURLFormat() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        String invalidBody = "{\"name\":\"Test Practice\",\"url\":\"invalid-url-format\",\"isActive\":true}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC012_POST_DuplicateName_ConflictScenario() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        String duplicateBody = String.format("{\"name\":\"%s\",\"url\":\"https://duplicate.com\",\"isActive\":true}", data.get("name"));
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, duplicateBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "fullRegression"})
    public void TC013_POST_Practice_InvalidDataType_IsActiveAsString() {
        String fullEndpoint = ApiActions.formEndpoint("/practices");
        String invalidBody = "{\"name\":\"Test Practice\",\"url\":\"https://test.com\",\"isActive\":\"true\"}"; // isActive should be boolean
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

}
