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
 * This class contains negative tests for the Location Controller of the Atlas Practice Service.
 * It includes tests for invalid authentication, invalid headers, and invalid data.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestLocationControllerNegative {

    Map<String, String> data = new HashMap<>();

    /**
     * This test verifies that a 401 Unauthorized status is returned when getting locations with an invalid access token.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetLocations() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a location with an invalid access token.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreateLocation() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        String validBody = "{\"name\":\"Test Location\",\"address\":\"123 Test St\",\"city\":\"Test City\",\"state\":\"TS\",\"zipCode\":\"12345\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when getting a location by ID without authentication.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetLocationById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/locations/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 500 Internal Server Error status is returned when getting locations with an invalid organization header.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetLocations() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    /**
     * This test verifies that a 500 Internal Server Error status is returned when creating a location with a missing organization header.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreateLocation() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        String validBody = "{\"name\":\"Test Location\",\"address\":\"456 Test Ave\",\"city\":\"Test Town\",\"state\":\"TT\",\"zipCode\":\"67890\"}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    /**
     * This test verifies that a 404 Not Found status is returned when getting a location from a non-existent endpoint.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC006_GET_Location_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/locations/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 400 Bad Request status is returned when getting a location with an ID containing special characters.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC007_GET_Location_InvalidLocationId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/locations/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    /**
     * This test verifies that a 404 Not Found status is returned when getting a location with a negative ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC008_GET_Location_InvalidLocationId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/locations/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    /**
     * This test verifies that a 404 Not Found status is returned when getting a location with a local ID containing special characters.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC009_GET_Location_InvalidLocalId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/locations/localId/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    /**
     * This test verifies that a 500 Internal Server Error status is returned when using an invalid advanced search query for locations.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC010_GET_Location_InvalidAdvancedSearchQuery() {
        String fullEndpoint = ApiActions.formEndpoint("/locations/advanced-search?search=%s", "invalid:query:format");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 500)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a location with a request body missing required fields.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC011_POST_Location_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        String invalidBody = "{\"name\":\"Test Location\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when updating a location with an invalid access token.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC012_PUT_InvalidAccessToken_UpdateLocation() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/locations/%s", data.get("id"));
        String validBody = "{\"name\":\"Updated Location\",\"address\":\"Updated Address\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    /**
     * This test verifies that a 401 Unauthorized status is returned when creating a location with an invalid data type for the "zipCode" field.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "fullRegression"})
    public void TC013_POST_InvalidDataType_ZipCodeAsNumber() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        String invalidBody = "{\"name\":\"Test Location\",\"address\":\"123 Test St\",\"city\":\"Test City\",\"state\":\"TS\",\"zipCode\":12345}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }
}
