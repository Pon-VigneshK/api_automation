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

public class TestPharmacyController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"pharmacyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_PharmaciesWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/pharmacy?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"pharmacyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_PharmacyById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM pharmacy where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/pharmacy/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"pharmacyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_PharmacyByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM pharmacy where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/pharmacy/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"pharmacyController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC004_POST_Create_Pharmacy() {
        long timestamp = System.currentTimeMillis();
        String requestBody = String.format("{\"name\":\"Test Pharmacy %d\",\"localId\":\"TEST_PHARM_%d\",\"address\":\"123 Pharmacy St\",\"city\":\"Test City\",\"state\":\"TS\",\"zipCode\":\"12345\",\"phone\":\"555-0123\",\"isActive\":true,\"organizationId\":%s}",
                timestamp, timestamp, PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER));
        String fullEndpoint = ApiActions.formEndpoint("/pharmacy");
        Response response = RequestBuilder.requestPostCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Object pharmacyId = response.jsonPath().get("id");
        Object pharmacyLocalId = response.jsonPath().get("localId");
        System.out.println("Created Pharmacy - ID: " + pharmacyId + ", LocalID: " + pharmacyLocalId + ", Status: " + response.getStatusCode());
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"pharmacyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_PharmacySearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM pharmacy where fk_organization_id = %s and id is not null and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/pharmacy/search?id=%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"pharmacyController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_PharmacyAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM pharmacy where fk_organization_id = %s and id is not null and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/pharmacy/advanced-search?search=%s", String.format("id:%s", data.get("id")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }
}
