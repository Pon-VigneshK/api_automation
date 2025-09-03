package org.op_ra.tests.atlaspracticeservice;

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

public class TestPatientCareTeamController {
    Map<String, String> data = new HashMap<>();

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientCareTeamController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_PatientCareTeamById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient_care_team where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patient-care-team/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientCareTeamController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_PatientCareTeamByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient_care_team where fk_organization_id = %s and local_id is not null and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patient-care-team/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "organizationId", getValue(ORGANIZATIONIDHEADER), "localId", data.get("local_id")))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"patientCareTeamController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_PatientCareTeamByPatient() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM patient where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/patient-care-team/patient/%s", data.get("id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }
}
