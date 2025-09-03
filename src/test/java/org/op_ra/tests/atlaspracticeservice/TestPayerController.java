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

public class TestPayerController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"payerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_PayerById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM payer where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/payers/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id"), "fullName", data.get("full_name")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"payerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_PayerByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM payer where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/payers/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id"), "fullName", data.get("full_name")))).isEqualTo(true);

    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"payerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_PayersWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/payers?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"payerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_PayerSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM payer where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/payers/search?name=%s", data.get("name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id"), "fullName", data.get("full_name")))).isEqualTo(true);

    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"payerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_PayerRecord() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM payer where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/payers/record/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);

    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"payerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_PayerAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM payer where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/payers/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id"), "fullName", data.get("full_name")))).isEqualTo(true);

    }
}
