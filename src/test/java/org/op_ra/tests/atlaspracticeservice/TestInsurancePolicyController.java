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

public class TestInsurancePolicyController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"insurancePolicyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_InsurancePolicyById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM insurance_policy where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/insurance-policies/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "payerId", data.get("fk_payer_id"), "policyNumber", data.get("policy_number")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"insurancePolicyController", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_InsurancePoliciesWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/insurance-policies?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"insurancePolicyController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_InsurancePoliciesByPatient() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/insurance-policies/patient/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"insurancePolicyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_InsurancePolicyByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM insurance_policy where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/insurance-policies/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "payerId", data.get("fk_payer_id"), "policyNumber", data.get("policy_number")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"insurancePolicyController", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_InsurancePolicyAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM insurance_policy where fk_organization_id = %s and fk_patient_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/insurance-policies/advanced-search?search=%s", String.format("id:%s", data.get("id")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "payerId", data.get("fk_payer_id"), "policyNumber", data.get("policy_number")))).isEqualTo(true);
    }
}
