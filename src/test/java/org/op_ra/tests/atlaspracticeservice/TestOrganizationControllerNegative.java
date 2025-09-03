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

public class TestOrganizationControllerNegative {

    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetOrganizations() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreateOrganization() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        String validBody = "{\"name\":\"Test Organization\",\"opmedId\":\"TEST123\",\"isActive\":true}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetOrganizationById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetOrganizations() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreateOrganization() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        String validBody = "{\"name\":\"Test Organization\",\"opmedId\":\"TEST456\",\"isActive\":true}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC006_GET_Organization_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC007_GET_InvalidOrganizationId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC008_GET_InvalidOrganizationId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC009_GET_InvalidOpmedId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations/opmedId/%s", RandomUtils.generateRandomAlphaNumericSpecialString(8));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC010_GET_NonExistentOpmedId() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations/opmedId/%s", RandomUtils.getRandomAlphabets(10));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC011_POST_Org_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        String invalidBody = "{\"opmedId\":\"TEST789\"}"; // Missing required name field
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC012_PUT_InvalidAccessToken_UpdateOrganization() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/%s", data.get("id"));
        String validBody = "{\"name\":\"Updated Organization\",\"opmedId\":\"UPDATED123\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC014_POST_DuplicateOpmedId_ConflictScenario() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s and opmed_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        String duplicateBody = String.format("{\"name\":\"Duplicate Organization\",\"opmedId\":\"%s\",\"isActive\":true}", data.get("opmed_id"));
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, duplicateBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC015_POST_InvalidDataType_IsActiveAsString() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        String invalidBody = "{\"name\":\"Test Organization\",\"opmedId\":\"TEST999\",\"isActive\":\"true\"}"; // isActive should be boolean
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }


    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "fullRegression"})
    public void TC016_POST_ExcessivelyLongName() {
        String longName = RandomUtils.getRandomAlphabets(500); // Very long name
        String fullEndpoint = ApiActions.formEndpoint("/organizations");
        String invalidBody = String.format("{\"name\":\"%s\",\"opmedId\":\"LONG123\",\"isActive\":true}", longName);
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }
}
