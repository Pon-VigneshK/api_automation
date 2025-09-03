package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestPatientBalanceController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientBalanceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_PatientBalanceById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient_balance where fk_organization_id = %s and id is not null and local_id is not null  limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patient-balances/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientBalanceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_PatientBalanceByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient_balance where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patient-balances/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientBalanceController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC003_PATCH_Update_PatientBalance() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient_balance where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String requestBody = "{\"balance\":100.50,\"notes\":\"Updated balance\"}";
        String fullEndpoint = ApiActions.formEndpoint("/patient-balances/%s", data.get("id"));
        Response response = RequestBuilder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 204)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientBalanceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_PatientBalanceRecord() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient_balance where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patient-balances/record/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientBalanceController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC005_POST_Create_PatientBalance() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and id is not null order by last_updated desc limit 1;", getValue(ORGANIZATIONIDHEADER)));

        if (data.isEmpty() || data.get("id") == null) {
            System.out.println("No patient data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping patient balance creation test.");
            return;
        }

        String requestBody = String.format("{\"patientId\":%s,\"balance\":250.75,\"balanceType\":\"OUTSTANDING\",\"notes\":\"Test balance creation\"}", data.get("id"));
        String fullEndpoint = ApiActions.formEndpoint("/patient-balances");
        Response response = RequestBuilder.requestPostCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 201)).isEqualTo(true);
    }
}
