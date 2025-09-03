package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.FileReadUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.constants.FrameworkConstants.getJsonPayload;
import static org.op_ra.constants.FrameworkConstants.getJsonTempPayload;
import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.JsonUtils.updateLocationPayload;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestLocationController {
    Map<String, String> data = new HashMap<>();
    Object ID, LocalID;

    @BeforeClass(groups = {"locationController", "smoke", "highLevel", "fullRegression"})
    public void setUp() {
        updateLocationPayload(getJsonTempPayload("location.json"), getJsonPayload("location.json"));
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_POST_Create_Location() {
        String fullEndpoint = ApiActions.formEndpoint("/locations");
        Response response = RequestBuilder.requestPostCall(fullEndpoint, FileReadUtils.getPayLoadFileLocationAsString(getJsonPayload("location.json")));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        ID = response.jsonPath().get("id");
        LocalID = response.jsonPath().get("localId");
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_LocationByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location where fk_organization_id=%s and local_id = %s limit 1;", getValue(ORGANIZATIONIDHEADER), LocalID.toString()));
        String fullEndpoint = ApiActions.formEndpoint("/locations/localId/%s", LocalID.toString());
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", ID, "localId", LocalID, "address", data.get("address"), "organizationId", getValue(ORGANIZATIONIDHEADER), "state", data.get("state")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_LocationsWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/locations?page=1&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_LocationById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/locations/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_LocationSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/locations/search?name=%s", data.get("name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"locationController", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_LocationAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/locations/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }
}
