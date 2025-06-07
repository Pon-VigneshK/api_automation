package org.op_ra.tests.OpenService;


import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.ApiRequestBuilder;
import org.op_ra.requestbuilder.AssertionUtils;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.RandomUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class TestOpenOrphanDiagTest {
    Map<String, String> data = new HashMap<>();

    @Test(groups = {"openOrphanDiagTestResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC001_POST_OpenOrphanDiagTest_Add_Provider_DtnStatus_Correctly_Processed() {
        data= DatabaseQueryExecutor.retrieveRowData("SELECT * FROM ORPHAN_DIAG_TEST_ADD_PROVIDER WHERE STATUS = 'SUCCESS' limit 1;");
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/addProvider", data.get("DTN_STATUS_ID"));
        Assertions.assertThat(AssertionUtils.isResponseBodyEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),String.format("Success adding the provider to the address book from the Dtn Status record  %s", data.get("DTN_STATUS_ID")))).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource","fullRegression"})
    public void TC002_POST_OpenOrphanDiagTest_Add_Provider_DtnStatusIdNotFound() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/addProvider", String.valueOf(Integer.MIN_VALUE));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "fullRegression"})
    public void TC003_POST_OpenOrphanDiagTest_Add_Provider_DtnStatus_No_Repository_Document_Found() {
        data = DatabaseQueryExecutor
                .retrieveRowData("SELECT * FROM ORPHAN_DIAG_TEST_ADD_PROVIDER WHERE STATUS = 'NO_REPOSITORY_DOCUMENT_FOUND' limit 1;");
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/addProvider", data.get("DTN_STATUS_ID"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),500)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC004_POST_OpenOrphanDiagTest_Add_Provider_DtnStatus_No_Match_Provider_Id_In_Hl7() {
        data = DatabaseQueryExecutor
                .retrieveRowData("SELECT * FROM ORPHAN_DIAG_TEST_ADD_PROVIDER WHERE STATUS = 'NO_MATCH_PROVIDER_ID_IN_HL7' limit 1;");
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/addProvider", data.get("DTN_STATUS_ID"));
        Assertions.assertThat(AssertionUtils.isResponseBodyEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),String.format("Provider id on the DTN Status record %s did not match the Provider id in the Hl7 {}%s", data.get("PROVIDER_ID_OP"), data.get("PROVIDER_ID_HL7")))).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource",  "fullRegression"})
    public void TC005_POST_OpenOrphanDiagTest_Add_Provider_Not_Authorized_Error() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/addProvider", String.valueOf(Integer.MIN_VALUE));
        Assertions.assertThat(ApiRequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC006_POST_OpenOrphanDiagTest_Add_Provider_Bad_Request() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/addProvider", RandomUtils.generateRandomString(4));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC007_POST_OpenOrphanDiagTest_Update_Dtn_Status_Op_Provider_Id_Correctly_Processed() {
        Map<String, String> data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM ORPHAN_DIAG_TEST_UPDATE_DTN_STATUS WHERE STATUS = 'SUCCESS' AND OP_PROVIDER_ID not null limit 1;");
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update?opProviderId=%s", data.get("DTN_STATUS_ID"), data.get("OP_PROVIDER_ID"));
        Assertions.assertThat(AssertionUtils.isResponseBodyEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),String.format("Success updating Dtn Status record with id %s", data.get("DTN_STATUS_ID")))).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC008_POST_OpenOrphanDiagTest_Update_Dtn_Status_Patient_Id_Correctly_Processed() {
        Map<String, String> data = DatabaseQueryExecutor
                .retrieveRowData("SELECT * FROM ORPHAN_DIAG_TEST_UPDATE_DTN_STATUS WHERE STATUS = 'SUCCESS' AND PATIENT_ID not null limit 1;");
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update?patientId=%s", data.get("DTN_STATUS_ID"), data.get("PATIENT_ID"));
        Assertions.assertThat(AssertionUtils.isResponseBodyEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),String.format("Success updating Dtn Status record with id %s", data.get("DTN_STATUS_ID")))).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource","fullRegression"})
    public void TC009_POST_OpenOrphanDiagTest_Update_DtnStatus_NotFound() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update?opProviderId=%s", data.get("DTN_STATUS_ID"), String.valueOf(Integer.MIN_VALUE), data.get("OP_PROVIDER_ID"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource","fullRegression"})
    public void TC010_POST_OpenOrphanDiagTest_Update_Dtn_Status_Bad_Request() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update",RandomUtils.generateRandomString(4));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource",  "fullRegression"})
    public void TC011_POST_OpenOrphanDiagTest_Update_Dtn_Status_Not_Authorized_Error() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update", String.valueOf(Integer.MIN_VALUE));
        Assertions.assertThat(ApiRequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC012_POST_OpenOrphanDiagTest_Update_Dtn_Status_No_Query_Parameters() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM ORPHAN_DIAG_TEST_UPDATE_DTN_STATUS WHERE STATUS = 'NO_QUERY_PARAMETERS' limit 1;");
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update", data.get("DTN_STATUS_ID"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource","fullRegression"})
    public void TC013_POST_OpenOrphanDiagTest_Update_Dtn_Status_Op_Provider_Id_Bad_Request() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update?opProviderId", String.valueOf(Integer.MIN_VALUE), RandomUtils.generateRandomNumericString(12));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }

    @Test(groups = {"openOrphanDiagTestResource", "fullRegression"})
    public void TC014_POST_OpenOrphanDiagTest_Update_Dtn_Status_Patient_Id_Bad_Request() {
        String fullEndpoint = ApiActions.buildLabServiceEndpoint("/rest/orphanDiagnosticTests/%s/update?patientId", String.valueOf(Integer.MIN_VALUE), RandomUtils.generateRandomNumericString(12));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.postChcService(fullEndpoint),400)).isEqualTo(true);
    }
}
