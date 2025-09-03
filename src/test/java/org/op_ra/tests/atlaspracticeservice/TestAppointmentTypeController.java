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

public class TestAppointmentTypeController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentTypeController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_AppointmentTypeById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment_type where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointment-types/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "localId", data.get("local_id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentTypeController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_AppointmentTypeByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment_type where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointment-types/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "localId", data.get("local_id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);

    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentTypeController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_AppointmentTypesWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/appointment-types?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }


    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentTypeController", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_AppointmentTypeAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment_type where fk_organization_id = %s and name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointment-types/advanced-search?search=%s", String.format("name:%s", data.get("name")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "name", data.get("name"), "localId", data.get("local_id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);

    }

}
