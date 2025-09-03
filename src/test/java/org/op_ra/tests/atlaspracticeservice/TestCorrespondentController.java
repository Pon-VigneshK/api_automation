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

/**
 * This class contains tests for the Correspondent Controller of the Atlas Practice Service.
 * It includes tests for reading correspondents.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestCorrespondentController {
    Map<String, String> data = new HashMap<>();

    /**
     * This test verifies reading correspondents with pagination.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"correspondentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_CorrespondentsWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/correspondents?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    /**
     * This test verifies reading a correspondent by its identifier.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"correspondentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_CorrespondentByIdentifier() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM correspondent where fk_organization_id=%s and id is not null and identifier is not null and login_id is not null and role is not null limit 1;", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/correspondents/identifier/%s", data.get("identifier"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "identifier", data.get("identifier"), "organization", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading a correspondent by its ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"correspondentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_CorrespondentById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM correspondent where fk_organization_id=%s and id is not null and identifier is not null and login_id is not null and role is not null limit 1;", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/correspondents/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "identifier", data.get("identifier"), "organization", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading a correspondent by its local ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"correspondentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_CorrespondentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM correspondent where fk_organization_id=%s and id is not null and identifier is not null and login_id is not null and role is not null limit 1;", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/correspondents/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "identifier", data.get("identifier"), "organization", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies the advanced search for correspondents.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"correspondentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_CorrespondentAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM correspondent where fk_organization_id=%s and id is not null and identifier is not null and login_id is not null and role is not null limit 1;", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/correspondents/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "identifier", data.get("identifier"), "organization", PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }
}
