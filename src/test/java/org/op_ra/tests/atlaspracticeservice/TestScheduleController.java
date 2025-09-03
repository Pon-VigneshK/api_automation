package org.op_ra.tests.atlaspracticeservice;

import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestScheduleController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"scheduleController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_ProviderAppointmentSlots() {
        // Get provider data for testing appointment slots
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, s.id as staff_id FROM provider p JOIN staff s ON p.fk_staff_id = s.id WHERE p.fk_organization_id = %s AND p.id IS NOT NULL AND s.active = 1 LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        if (data.isEmpty() || data.get("id") == null) {
            System.out.println("No active provider data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping provider appointment slots test.");
            return;
        }

        // Get patient data (required parameter)
        Map<String, String> patientData = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient WHERE fk_organization_id = %s AND id IS NOT NULL AND is_deleted = 0 LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        if (patientData.isEmpty() || patientData.get("id") == null) {
            System.out.println("No patient data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping provider appointment slots test.");
            return;
        }

        // Get location data for the appointment slot search (optional but recommended)
        Map<String, String> locationData = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location WHERE fk_organization_id = %s AND id IS NOT NULL LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        // Use current date and next 7 days for search
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Build endpoint with required parameters: patientId, templateType, startDate, endDate
        // Optional parameters: providerId, locationIds
        String fullEndpoint;
        if (!locationData.isEmpty() && locationData.get("id") != null) {
            fullEndpoint = ApiActions.formEndpoint("/schedules/provider-appointment-slots?patientId=%s&templateType=WELL&startDate=%s&endDate=%s&providerId=%s&locationIds=%s",
                    patientData.get("id"),
                    startDateTime.format(formatter),
                    endDateTime.format(formatter),
                    data.get("id"),
                    locationData.get("id"));
        } else {
            fullEndpoint = ApiActions.formEndpoint("/schedules/provider-appointment-slots?patientId=%s&templateType=WELL&startDate=%s&endDate=%s&providerId=%s",
                    patientData.get("id"),
                    startDateTime.format(formatter),
                    endDateTime.format(formatter),
                    data.get("id"));
        }

        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        ApiActions.isStatusCodeEqualTo(response, 200);

        // Verify response structure for appointment slots
        Object success = response.jsonPath().get("success");
        Object results = response.jsonPath().get("results");

        Assertions.assertThat(success).isNotNull();
        System.out.println("Provider appointment slots search completed. Success: " + success + ", Results: " + (results != null ? "Available" : "None"));
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"scheduleController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_ProviderAppointmentSlots_MinimalParams() {
        // Get patient data (required parameter)
        Map<String, String> patientData = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient WHERE fk_organization_id = %s AND id IS NOT NULL AND is_deleted = 0 LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        if (patientData.isEmpty() || patientData.get("id") == null) {
            System.out.println("No patient data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping minimal params test.");
            return;
        }

        // Use current date and next day for search (minimal date range)
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Minimal required parameters: patientId, templateType, startDate, endDate
        String fullEndpoint = ApiActions.formEndpoint("/schedules/provider-appointment-slots?patientId=%s&templateType=WELL&startDate=%s&endDate=%s",
                patientData.get("id"),
                startDateTime.format(formatter),
                endDateTime.format(formatter));

        Response response = RequestBuilder.requestGetCall(fullEndpoint);

        // Enforce HTTP 200 OK - any 4XX/5XX will fail the test with detailed error analysis
        ApiActions.isStatusCodeEqualTo(response, 200);
        System.out.println("Minimal params appointment slots search completed successfully.");
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"scheduleController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC003_POST_Create_AppointmentForProviderSlot() {
        // Get provider and patient data for creating appointment
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, s.id as staff_id FROM provider p JOIN staff s ON p.fk_staff_id = s.id WHERE p.fk_organization_id = %s AND p.id IS NOT NULL AND s.active = 1 LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        if (data.isEmpty() || data.get("id") == null) {
            System.out.println("No active provider data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping appointment creation test.");
            return;
        }

        // Get patient data
        Map<String, String> patientData = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient WHERE fk_organization_id = %s AND id IS NOT NULL AND is_deleted = 0 LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        if (patientData.isEmpty() || patientData.get("id") == null) {
            System.out.println("No patient data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping appointment creation test.");
            return;
        }

        // Get location data
        Map<String, String> locationData = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM location WHERE fk_organization_id = %s AND id IS NOT NULL LIMIT 1;", getValue(ORGANIZATIONIDHEADER)));

        if (locationData.isEmpty() || locationData.get("id") == null) {
            System.out.println("No location data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping appointment creation test.");
            return;
        }

        // Create appointment request body using correct ProviderCreateAppointmentRequest format
        LocalDateTime appointmentDateTime = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        String requestBody = String.format("{"
                        + "\"patientId\":%s,"
                        + "\"providerId\":%s,"
                        + "\"locationId\":%s,"
                        + "\"templateType\":\"WELL\","
                        + "\"startDate\":\"%s\","
                        + "\"text\":\"Test appointment creation\""
                        + "}",
                patientData.get("id"),
                data.get("id"),
                locationData.get("id"),
                appointmentDateTime.format(formatter));
        System.out.println(requestBody);

        String fullEndpoint = ApiActions.formEndpoint("/schedules/provider-appointment-slots");
        Response response = RequestBuilder.requestPostCall(fullEndpoint, requestBody);
        ApiActions.isStatusCodeEqualTo(response,200);

        // Verify response structure
        Object success = response.jsonPath().get("success");
        Object appointmentId = response.jsonPath().get("appointmentId");
        Object status = response.jsonPath().get("status");

        System.out.println("Appointment creation completed. Success: " + success + ", AppointmentId: " + appointmentId + ", Status: " + status);

        if (success != null && success.toString().equals("true") && appointmentId != null) {
            System.out.println("Successfully created appointment with ID: " + appointmentId);
        }
    }
}
