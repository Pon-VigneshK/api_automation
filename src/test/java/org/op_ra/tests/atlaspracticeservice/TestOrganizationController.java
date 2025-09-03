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

public class TestOrganizationController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_OrganizationById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "opmedId", data.get("opmed_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_OrganizationByOpmedId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s and opmed_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/opmedId/%s", data.get("opmed_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "opmedId", data.get("opmed_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_OrganizationsWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/organizations?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_OrganizationSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/search?name=%s", data.get("name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "opmedId", data.get("opmed_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_OrganizationAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "opmedId", data.get("opmed_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"organizationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_HEAD_Check_OrganizationByOpMedId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM organization where id = %s and opmed_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/organizations/opmedId/%s", data.get("opmed_id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestHeadCall(fullEndpoint), 204)).isEqualTo(true);

    }

}
