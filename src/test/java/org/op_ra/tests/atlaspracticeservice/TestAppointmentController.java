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

/**
 * This class contains tests for the Appointment Controller of the Atlas Practice Service.
 * It includes tests for reading, creating, updating, and deleting appointments.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestAppointmentController {
    Map<String, String> data = new HashMap<>();

    /**
     * This test verifies reading an appointment by its ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_AppointmentById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and local_id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading an appointment by its local ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_AppointmentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading appointments by patient IDs.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_AppointmentsByPatients() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and local_id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/patients?patientIds=%s", data.get("fk_patient_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading appointments by provider ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_AppointmentsByProvider() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_provider_id is not null and  local_id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/provider/%s", data.get("fk_provider_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading appointments by location ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_AppointmentsByLocation() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_location_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/location/%s", data.get("fk_location_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading appointments within a date range.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_AppointmentsWithDateRange() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and fk_location_id is not null and is_deleted = 0 and start_date is not null and end_date is not null  limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments?startDate=%s&endDate=%s", DateUtils.formatDate(data.get("start_date")),  DateUtils.formatDate(data.get("end_date")));
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
    }

    /**
     * This test verifies searching for appointments by patient ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC007_GET_Read_AppointmentSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/search?patientId=%s", data.get("fk_patient_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies reading the status of an appointment.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC008_GET_Read_AppointmentStatus() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/status", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    /**
     * This test verifies getting the count of appointments by patient ID using a HEAD request.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC009_HEAD_Read_AppointmentCountByPatients() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/patients?patientIds=%s", data.get("fk_patient_id"));
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }

    /**
     * This test verifies reading an appointment by its local ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC011_GET_Read_AppointmentByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id")))).isEqualTo(true);
    }

    /**
     * This test verifies reading an appointment record by its ID.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC012_GET_Read_AppointmentRecord() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/record/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    /**
     * This test verifies the advanced search for appointments.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "highLevel", "fullRegression", "readAPICall"})
    public void TC013_GET_Read_AppointmentAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and fk_patient_id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments/advanced-search?search=%s", String.format("patientId:%s", data.get("fk_patient_id")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("localId", data.get("local_id"), "id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    /**
     * This test verifies confirming an appointment.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC014_PATCH_ConfirmAppointment() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String requestBody = "{\"status\":\"CONFIRMED\"}";
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/confirm", data.get("id"));
        Response response = RequestBuilder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }

    /**
     * This test verifies canceling an appointment.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC015_PATCH_CancelAppointment() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and id is not null and is_deleted = 0 and local_id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String requestBody = "{\"status\":\"CANCELLED\",\"reason\":\"Patient request\"}";
        String fullEndpoint = ApiActions.formEndpoint("/appointments/%s/cancel", data.get("id"));
        Response response = RequestBuilder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }

    /**
     * This test verifies updating the status of an appointment.
     */
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
        Response response = RequestBuiI will now add Javadoc to the `TestAdminController.java` class. I will add a class-level Javadoc explaining the purpose of the test class, and a method-level Javadoc for the test method explaining what the test does. I will apply this same pattern to all test classes.lder.requestPatchCall(fullEndpoint, requestBody);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
    }
}
