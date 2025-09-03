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

public class TestProviderController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_ProviderById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "staffId", data.get("fk_staff_id"), "specialty", data.get("specialty"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_ProviderByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "staffId", data.get("fk_staff_id"), "specialty", data.get("specialty"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_ProviderByStaffId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM provider where fk_organization_id = %s and fk_staff_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/staff/%s", data.get("fk_staff_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "staffId", data.get("fk_staff_id"), "specialty", data.get("specialty"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_ProvidersWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/providers?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_ProviderSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, s.first_name, s.last_name FROM provider p JOIN staff s ON p.fk_staff_id = s.id where p.fk_organization_id = %s and s.first_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/search?firstName=%s", data.get("first_name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "staffId", data.get("fk_staff_id"), "specialty", data.get("specialty"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"providerController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_ProviderAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, s.first_name, s.last_name FROM provider p JOIN staff s ON p.fk_staff_id = s.id where p.fk_organization_id = %s and s.first_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/providers/advanced-search?search=%s", String.format("id:%s", data.get("id")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "staffId", data.get("fk_staff_id"), "specialty", data.get("specialty"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);


    }
}
