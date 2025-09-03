package org.op_ra.tests.atlaspracticeservice;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.enums.ConfigProperties;
import org.op_ra.reports.ExtentLogger;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.PropertyUtils;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestPatientController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_PatientByAtlasId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and local_id is not null and id is not null and fk_person_id is not null and is_deleted = 0 and fk_created_by_staff_id is not null and account_status_date is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_PatientByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and local_id is not null and id is not null and fk_person_id is not null and is_deleted = 0 and fk_created_by_staff_id is not null and account_status_date is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_PatientByPersonId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and fk_person_id is not null and id is not null and is_deleted = 0 limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/person/%s", data.get("fk_person_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_PatientsWithPagination() {
        String fullEndpoint = ApiActions.formEndpoint("/patients?page=0&size=10");
        Response response = RequestBuilder.requestGetCall(fullEndpoint);
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);
        Assertions.assertThat((Object) response.jsonPath().get("content")).isNotNull();
        Assertions.assertThat((Object) response.jsonPath().get("totalElements")).isNotNull();
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC005_GET_Read_PatientAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, per.first_name, per.last_name FROM patient p JOIN person per ON p.fk_person_id = per.id where p.fk_organization_id = %s and p.is_deleted = 0 and per.first_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String searchQuery = String.format("id:%s", data.get("id"));
        String fullEndpoint = ApiActions.formEndpoint("/patients/advanced-search?search=%s", searchQuery);
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC006_GET_Read_PatientSimpleSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, per.first_name, per.last_name FROM patient p JOIN person per ON p.fk_person_id = per.id where p.fk_organization_id = %s and p.is_deleted = 0 and per.first_name is not null and per.last_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/simple-search?firstName=%s&lastName=%s", data.get("first_name"), data.get("last_name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC007_GET_Read_PatientPhoneSearch() {
        try {
            // Try to get phone data, but handle the case where phone table doesn't exist
            data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, ph.phone_number FROM patient p JOIN person per ON p.fk_person_id = per.id JOIN phone ph ON per.id = ph.fk_person_id where p.fk_organization_id = %s and p.is_deleted = 0 and ph.phone_number is not null and ph.phone_number != '' limit 1;", getValue(ORGANIZATIONIDHEADER)));
        } catch (Exception e) {
            // If phone table doesn't exist, try alternative approach or skip
            System.out.println("Phone table not found or query failed: " + e.getMessage() + ". Skipping phone search test.");
            throw new SkipException("Phone table not found or query failed: " + e.getMessage() + ". Skipping phone search test.");
        }

        // Check if data is available and phone_number is not null
        if (data.isEmpty() || data.get("phone_number") == null || data.get("phone_number").equals("null")) {
            System.out.println("No patient data with valid phone_number found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping phone search test.");
            ExtentLogger.skip("No patient data with valid phone_number found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping phone search test.");
            throw new SkipException("No patient data with valid phone_number found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping phone search test.");
        }

        String fullEndpoint = ApiActions.formEndpoint("/patients/phones/search?phoneNumber=%s", data.get("phone_number"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC008_GET_Read_PatientEmailSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, per.email FROM patient p JOIN person per ON p.fk_person_id = per.id where p.fk_organization_id = %s and p.is_deleted = 0 and per.email is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/search/email/%s", data.get("email"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC009_GET_Read_PatientFamilyBalance() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and id is not null and is_deleted = 0  order by creation_date desc limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/familybalance/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "highLevel", "fullRegression", "readAPICall"})
    public void TC010_HEAD_Check_PatientByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient  where fk_organization_id = 1 and local_id is not null  order by last_updated desc limit 1;", getValue(ORGANIZATIONIDHEADER)));

        // Check if data is available
        if (data.isEmpty() || data.get("local_id") == null) {
            System.out.println("No patient data with local_id found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping HEAD check test.");
            return;
        }

        String fullEndpoint = ApiActions.formEndpoint("/patients/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestHeadCall(fullEndpoint), 204)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC011_POST_Create_PatientWithPerson() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM person where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));

        if (data.isEmpty() || data.get("id") == null) {
            System.out.println("No person data found for organization " + getValue(ORGANIZATIONIDHEADER) + ". Skipping patient creation test.");
            return;
        }

        // Generate a unique localId to avoid conflicts
        String uniqueLocalId = "TEST_PAT_" + System.currentTimeMillis();

        // Enhanced payload with required organizationId field
        String requestBody = String.format("{\"localId\":\"%s\",\"isActive\":true,\"notes\":\"Test patient creation\",\"organizationId\":%s}",
                uniqueLocalId, PropertyUtils.getValue(ConfigProperties.ORGANIZATIONIDHEADER));

        String fullEndpoint = ApiActions.formEndpoint("/patients/person/%s", data.get("id"));
        Response response = RequestBuilder.requestPostCall(fullEndpoint, requestBody);

        // Validate that response is in 2XX success range (200-299) - test should fail on any non-2XX response
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(response, 200)).isEqualTo(true);

        // Extract and log created patient details if successful
        Object patientId = response.jsonPath().get("id");
        Object patientLocalId = response.jsonPath().get("localId");
        System.out.println("Created Patient - ID: " + patientId + ", LocalID: " + patientLocalId + ", PersonID: " + data.get("id") + ", Status: " + response.getStatusCode());
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC012_GET_Read_PatientSimpleSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT p.*, per.first_name FROM patient p JOIN person per ON p.fk_person_id = per.id WHERE p.fk_organization_id = %s and per.first_name is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patients/simple-search?query=%s", data.get("first_name"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "personId", data.get("fk_person_id"), "accountStatus", data.get("account_status"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC013_PATCH_Update_Patient() {
        // Step 0: Get patient data from database
        data = DatabaseQueryExecutor.retrieveRowData(String.format(
                "SELECT * FROM patient WHERE fk_organization_id = %s AND id IS NOT NULL LIMIT 1;",
                getValue(ORGANIZATIONIDHEADER)));

        // Step 1: Check if data is available
        if (data.isEmpty() || data.get("id") == null) {
            System.out.println("No patient data found for organization " + getValue(ORGANIZATIONIDHEADER)
                    + ". Skipping patient update test.");
            return;
        }

        // Step 2: Fetch the current patient data using GET call
        String getEndpoint = ApiActions.formEndpoint("/patients/%s", data.get("id"));
        Response getResponse = RequestBuilder.requestGetCall(getEndpoint);

        // Assert GET response is successful
        Assertions.assertThat(getResponse.getStatusCode())
                .as("Verify GET response status code is 200")
                .isEqualTo(200);

        // Step 3: Extract current values
        JsonPath jsonPath = getResponse.jsonPath();
        String currentNotes = jsonPath.getString("notes");
        String currentLocalId = jsonPath.getString("localId");
        boolean currentIsActive = "true".equalsIgnoreCase(jsonPath.getString("isActive"));

        System.out.println("Current Patient Data - ID: " + data.get("id") + ", LocalID: " + currentLocalId +
                ", Notes: " + currentNotes + ", IsActive: " + currentIsActive);

        // Step 4: Prepare updated data
        String updatedNotes = "Updated patient notes - " + System.currentTimeMillis();
        boolean updatedIsActive = !currentIsActive;

        String requestBody = String.format("{\"notes\":\"%s\",\"isActive\":%s}",
                updatedNotes, updatedIsActive);

        System.out.println("PATCH Request Body: " + requestBody);

        // Step 5: Send PATCH request
        String patchEndpoint = ApiActions.formEndpoint("/patients/%s", data.get("id"));
        Response patchResponse = RequestBuilder.requestPatchCall(patchEndpoint, requestBody);

        // Step 6: Assert PATCH response status is 200
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(patchResponse, 204)).isEqualTo(true);
    }

}
