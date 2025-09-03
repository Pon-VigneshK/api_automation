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

public class TestPracticeController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_PracticeById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "state", data.get("state"), "city", data.get("city"), "address", data.get("address"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "zip", data.get("zip")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_PracticeByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "state", data.get("state"), "city", data.get("city"), "address", data.get("address"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "zip", data.get("zip")))).isEqualTo(true);

    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_PracticesWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/practices?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_PracticeSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/search?id=%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "state", data.get("state"), "city", data.get("city"), "address", data.get("address"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "zip", data.get("zip")))).isEqualTo(true);

    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_PracticeAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM practice where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_PracticeByOpMedIdAndLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, o.opmed_id FROM practice p JOIN organization o ON p.fk_organization_id = o.id WHERE p.fk_organization_id = %s and p.local_id is not null and o.opmed_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/opmedId/%s/localId/%s", data.get("opmed_id"), data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "state", data.get("state"), "city", data.get("city"), "address", data.get("address"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER), "zip", data.get("zip")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"practiceController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC009_GET_Read_PracticeByProvider() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/practices/provider/%s", data.get("id"));
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
    }
}
