package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.DateUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestAppointmentController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_AppointmentById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and local_id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_AppointmentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_AppointmentsByPatients() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and local_id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/patients?patientIds=%s", data.get("fk_patient_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_AppointmentsByProvider() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_provider_id is not null and  local_id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/provider/%s", data.get("fk_provider_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_AppointmentsByLocation() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_location_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/location/%s", data.get("fk_location_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_AppointmentsWithDateRange() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and fk_location_id is not null and is_deleted = 0 and start_date is not null and end_date is not null  limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments?startDate=%s&endDate=%s", DateUtils.formatDate(data.get("start_date")),  DateUtils.formatDate(data.get("end_date")));
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC007_GET_Read_AppointmentSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/search?patientId=%s", data.get("fk_patient_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC008_GET_Read_AppointmentStatus() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/status", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC009_HEAD_Read_AppointmentCountByPatients() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/patients?patientIds=%s", data.get("fk_patient_id"));
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }

//    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
//    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "deleteAPICall"})
//    public void TC010_DELETE_RemoveAppointment() {
//        // First create test data or use existing appointment that can be safely deleted
//        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and id > 100 limit 1;", getValue(ORGANIZATIONIDHEADER)));
//
//        if (data.isEmpty() || data.get("id") == null) {
//            System.out.println("No appointment data found for deletion test. Skipping delete test.");
//            return;
//        }
//
//        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s", data.get("id"));
//        Response response = RequestBuilder.requestDeleteCall(fullEndpoint);
//
//        // Handle 405 Method Not Allowed - DELETE may not be supported
//        if (response.getStatusCode() == 405) {
//            System.out.println("DELETE method not allowed for appointments (405). This may be expected in the test environment.");
//            return;
//        }
//
//        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200) || ApiActions.isStatusCodeEqualTo(response, 204)).isEqualTo(true);
//    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC011_GET_Read_AppointmentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC012_GET_Read_AppointmentRecord() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/record/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC013_GET_Read_AppointmentAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/advanced-search?search=%s", String.format("patientId:%s", data.get("fk_patient_id")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC014_PATCH_ConfirmAppointment() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String requestBody = "{\"status\":\"CONFIRMED\"}";
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/confirm", data.get("id"));
        Response response = RequestBuilder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC015_PATCH_CancelAppointment() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String requestBody = "{\"status\":\"CANCELLED\",\"reason\":\"Patient request\"}";
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/cancel", data.get("id"));
        Response response = RequestBuilder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC016_PATCH_UpdateAppointmentStatus() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id =%s and fk_location_id is not null and is_deleted = 0 and start_date is not null and end_date is not null and billing_status ='Completed'  limit 1;", getValue(ORGANIZATIONIDHEADER)));

        if (data.isEmpty() || data.get("id") == null) {
            System.out.println("No appointment data found. Skipping status update test.");
            return;
        }

        String requestBody = "{\"status\":\"PENDING\"}";
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/status", data.get("id"));
        Response response = RequestBuilder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }


}
