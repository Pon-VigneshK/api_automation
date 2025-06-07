package org.op_ra.tests.OpenService;


import org.assertj.core.api.Assertions;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.ApiRequestBuilder;
import org.op_ra.requestbuilder.AssertionUtils;
import org.op_ra.utils.DatabaseQueryExecutor;
import org.op_ra.utils.RandomUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.op_ra.constants.FrameworkConstants.getNumberOne;

public class TestOpenERX {

    Map<String, String> data = new HashMap<>();

    @Test(groups = {"erxResource", "highLevel", "fullRegression", "searchAPICall"})
    public void TC001_GET_Erx_GetDrugDetails() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM RX_CHANGE_OPTION where NDCID is not null limit 1;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/drug/get_drug_detail?medicationType=national&medId=%s", data.get("NDCID"));
        Assertions.assertThat(AssertionUtils.validateValueInXml(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),"BrandType", "NDCID", data.get("DRUG_BRAND_TYPE"))).isEqualTo(true);
    }

    @Test(groups = {"erxResource", "highLevel", "fullRegression"})
    public void TC002_GET_Erx_GetProviderServiceLevels() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 1;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/servicelevel/prescriber/%s/location/%s", data.get("PRESCRIBER_ID"), data.get("LOCATION"));
        Assertions.assertThat(AssertionUtils.isXmlElementListEmpty(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),  data.get("SERVICE_LEVELS"))).isEqualTo(true);
    }

    @Test(groups = {"erxResource",  "fullRegression"})
    public void TC003_GET_Erx_Negative_GetProviderServiceLevelsWithoutLocation() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 1;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/servicelevel/prescriber/%s/location/", data.get("PRESCRIBER_ID"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }

    @Test(groups = {"erxResource", "fullRegression"})
    public void TC004_GET_Erx_Negative_GetProviderServiceLevelsWithInvalidLocation() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 1;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/servicelevel/prescriber/%s/location/%s", data.get("PRESCRIBER_ID"), RandomUtils.generateRandomNumericString(12));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }

    @Test(groups = {"erxResource",  "fullRegression"})
    public void TC005_GET_Erx_Negative_GetProviderServiceLevelsWithInvalidPrescriber() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 1;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/servicelevel/prescriber/%s/location/%s", RandomUtils.generateRandomNumericString(12), data.get("LOCATION"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }

    @Test(groups = {"erxResource","fullRegression"})
    public void TC006_GET_Erx_Negative_GetProviderServiceLevelsWithoutPrescriber() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 1;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/servicelevel/prescriber/location/%s", data.get("LOCATION"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }

    @Test(groups = {"erxResource", "highLevel", "fullRegression"})
    public void TC007_GET_Erx_GetDrugList() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 2;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/drug/search_drug_detail?searchString=%s&maxResults=%s&searchType=%s", data.get("DRUG_PARTIAL_NAME"), data.get("DRUG_MAX_RESULTS"), data.get("DRUG_SEARCH_TYPE"));
        Assertions.assertThat(AssertionUtils.validateValueInXml(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),"Drug","BrandName", data.get("DRUG_PARTIAL_NAME"))).isEqualTo(true);
    }

    @Test(groups = {"erxResource", "highLevel", "fullRegression"})
    public void TC008_GET_Erx_GetDrugListHavingSingleElement() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 2;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/drug/search_drug_detail?searchString=%s&maxResults=%s&searchType=%s", data.get("DRUG_PARTIAL_NAME"), getNumberOne(), data.get("DRUG_SEARCH_TYPE"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),200)).isEqualTo(true);
    }


    @Test(groups = {"erxResource",  "fullRegression"})
    public void TC009_GET_Erx_Negative_GetDrugListWithInvalidDrugName(){
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 2;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/drug/search_drug_detail?searchString=%s&maxResults=%s&searchType=%s", RandomUtils.generateRandomNumericString(12), data.get("DRUG_MAX_RESULTS"), data.get("DRUG_SEARCH_TYPE"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }

    @Test(groups = {"erxResource", "highLevel", "fullRegression"})
    public void TC010_GET_Erx_GetDrugDetail() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 3;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/drug/get_drug_detail?medicationType=%s&medId=%s", data.get("DRUG_MEDICATION_TYPE"), data.get("DRUG_MED_ID"));
        Assertions.assertThat(AssertionUtils.validateValueInXml(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),"DrugDetail", "NDCID", data.get("DRUG_MED_ID"))).isEqualTo(true);
    }

    @Test(groups = {"erxResource",  "fullRegression"})
    public void TC011_GET_Erx_Negative_GetDrugDetailWithInvalidMedId() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM openerx WHERE ID = 3;");
        String fullEndpoint = ApiActions.buildErxServiceEndpoint("/erx/drug/get_drug_detail?medicationType=%s&medId=%s", data.get("DRUG_MEDICATION_TYPE"), "1");
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getErxServiceResponse(fullEndpoint),404)).isEqualTo(true);

    }

}
