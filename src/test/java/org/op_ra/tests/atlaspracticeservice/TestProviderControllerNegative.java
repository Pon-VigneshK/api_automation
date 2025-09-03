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

public class TestProviderControllerNegative {

    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetProviders() {
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreateProvider() {
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        String validBody = "{\"firstName\":\"Dr. John\",\"lastName\":\"Smith\",\"specialty\":\"Cardiology\",\"npi\":\"1234567890\",\"licenseNumber\":\"LIC123456\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetProviderById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetProviders() {
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreateProvider() {
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        String validBody = "{\"firstName\":\"Dr. Jane\",\"lastName\":\"Doe\",\"specialty\":\"Neurology\",\"npi\":\"0987654321\",\"licenseNumber\":\"LIC654321\"}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC006_GET_Provider_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/providers/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC007_GET_InvalidProviderId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/providers/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC008_GET_InvalidProviderId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/providers/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC009_POST_Provider_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        String invalidBody = "{\"firstName\":\"Dr. John\"}"; // Missing required fields
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC010_PUT_InvalidAccessToken_UpdateProvider() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/%s", data.get("id"));
        String validBody = "{\"firstName\":\"Updated\",\"lastName\":\"Provider\",\"specialty\":\"Updated Specialty\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC011_POST_InvalidDataType_NPIAsString() {
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        String invalidBody = "{\"firstName\":\"Dr. John\",\"lastName\":\"Smith\",\"specialty\":\"Cardiology\",\"npi\":\"invalid-npi\",\"licenseNumber\":\"LIC123456\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }


    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC012_GET_InvalidLocationId_ForProvidersByLocation() {
        String fullEndpoint = ApiActions.formEndpoint("/providers/location/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC013_POST_DuplicateNPI_ConflictScenario() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and npi is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers");
        String duplicateBody = String.format("{\"firstName\":\"Dr. Duplicate\",\"lastName\":\"Provider\",\"specialty\":\"General\",\"npi\":\"%s\",\"licenseNumber\":\"LIC999999\"}", data.get("npi"));
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, duplicateBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "fullRegression"})
    public void TC014_GET_InvalidDateRange_ProviderSchedule() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/%s/schedule?startDate=invalid-date&endDate=invalid-date", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }
}
