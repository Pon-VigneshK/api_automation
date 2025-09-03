package org.op_ra.tests.atlaspracticeservice;

import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.DateUtils;
import org.op_ra.utils.RandomUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.enums.ConfigProperties.ORGANIZATIONIDHEADER;
import static org.op_ra.utils.PropertyUtils.getValue;

public class TestAppointmentControllerNegative {

    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC001_GET_InvalidAccessToken_GetAppointments() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and fk_location_id is not null and is_deleted = 0 and start_date is not null and end_date is not null  limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments?startDate=%s&endDate=%s", DateUtils.formatDate(data.get("start_date")), DateUtils.formatDate(data.get("end_date")));
        Assertions.assertThat(RequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC002_POST_InvalidAccessToken_CreateAppointment() {
        String fullEndpoint = ApiActions.formEndpoint("/appointments");
        String validBody = "{\"patientId\":123,\"providerId\":456,\"locationId\":789,\"appointmentDateTime\":\"2024-12-01T10:00:00\",\"reason\":\"Test Appointment\"}";
        Assertions.assertThat(RequestBuilder.requestPostCallUsingInvalidToken(fullEndpoint, validBody)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC003_GET_NoAuthentication_GetAppointmentsByLocation() {
        String fullEndpoint = ApiActions.formEndpoint("/appointments/location/123");
        Assertions.assertThat(RequestBuilder.requestGetCallWithoutAuth(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC004_GET_InvalidOrganizationHeader_GetAppointments() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM appointment where fk_organization_id = %s and local_id is not null and fk_location_id is not null and is_deleted = 0 and start_date is not null and end_date is not null  limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/appointments?startDate=%s&endDate=%s", DateUtils.formatDate(data.get("start_date")), DateUtils.formatDate(data.get("end_date")));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC005_POST_MissingOrganizationHeader_CreateAppointment() {
        String fullEndpoint = ApiActions.formEndpoint("/appointments");
        String validBody = "{\"patientId\":123,\"providerId\":456,\"locationId\":789,\"appointmentDateTime\":\"2024-12-01T10:00:00\",\"reason\":\"Test Appointment\"}";
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCallUsingInvalidHeader(fullEndpoint, validBody), 500)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC006_GET_InvalidAppoitnment_Path_NonExistentEndpoint() {
        String fullEndpoint = ApiActions.formEndpoint("/appointments/invalid/endpoint");
        Assertions.assertThat(RequestBuilder.requestGetCallInvalidPath(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC007_GET_AppointmentInvalidLocationId_SpecialCharacters() {
        String fullEndpoint = ApiActions.formEndpoint("/appointments/location/%s", RandomUtils.getRandomAlphabets(8));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCallByString(fullEndpoint)).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.FULL_REGRESSION})
    @Test(groups = {"appointmentController", "fullRegression"})
    public void TC008_GET_InvalidLocationId_NegativeValue() {
        String fullEndpoint = ApiActions.formEndpoint("/appointments/location/%s", RandomUtils.getRandomNumber(10));
        Assertions.assertThat(RequestBuilder.requestInvalidReadGetCall(fullEndpoint)).isEqualTo(true);
    }
}
