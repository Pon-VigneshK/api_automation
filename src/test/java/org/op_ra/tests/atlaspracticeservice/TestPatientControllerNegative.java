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

public class TestPatientControllerNegative {

    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetPatients() {
        String fullEndpoint = ApiActions.formEndpoint("/patients");
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreatePatient() {
        String fullEndpoint = ApiActions.formEndpoint("/patients");
        String validBody = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"dateOfBirth\":\"1990-01-01\",\"gender\":\"M\",\"phoneNumber\":\"555-1234\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetPatientById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/%s", data.get("id"));
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetPatients() {
        String fullEndpoint = ApiActions.formEndpoint("/patients");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreatePatient() {
        String fullEndpoint = ApiActions.formEndpoint("/patients");
        String validBody = "{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"dateOfBirth\":\"1985-05-15\",\"gender\":\"F\",\"phoneNumber\":\"555-5678\"}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC006_GET_Patient_InvalidPath_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/patients/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC007_GET_InvalidPatientId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/patients/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC008_GET_InvalidPatientId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/patients/%s", "-" + RandomUtils.getRandomNumber(5));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 404)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC009_POST_Patient_InvalidRequestBody_MissingRequiredFields() {
        String fullEndpoint = ApiActions.formEndpoint("/patients");
        String invalidBody = "{\"firstName\":\"John\"}"; // Missing required fields
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC010_PUT_InvalidAccessToken_UpdatePatient() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/%s", data.get("id"));
        String validBody = "{\"firstName\":\"Updated\",\"lastName\":\"Name\",\"dateOfBirth\":\"1990-01-01\",\"gender\":\"M\"}";
        Assertions.assertThat(RequestBuilder.requestPutCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }


    @FrameworkAnnotation(author = {"Keerthana"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "fullRegression"})
    public void TC011_POST_InvalidDataType_NumericFieldAsString() {
        String fullEndpoint = ApiActions.formEndpoint("/patients");
        String invalidBody = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"dateOfBirth\":\"1990-01-01\",\"gender\":\"M\",\"phoneNumber\":123456}"; // phoneNumber should be string
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, invalidBody)).isEqualTo(true);
    }


}
