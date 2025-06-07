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

public class TestOpenAccessOrders {

    Map<String, String> data = new HashMap<>();

    @Test(groups = {"accessOrders", "highLevel", "fullRegression", "searchAPICall"})
    public void TC001_GET_AccessOrders_ByLocationAndRequisitionId() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM ACCESS_ORDERS WHERE requisition_id IS NOT NULL LIMIT 1;");
        String fullEndpoint = ApiActions.buildChcServiceEndpoint( "/chc/url/order/accessOrders?locationId=%s&requisitionId=%s", data.get("location_id"), data.get("requisition_id"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getChcServiceResponse(fullEndpoint),200)).isEqualTo(true);
    }

    @Test(groups = {"accessOrders", "fullRegression"})
    public void TC002_GET_AccessOrders_ExpiredToken() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT * FROM ACCESS_ORDERS WHERE requisition_id IS NOT NULL LIMIT 1;");
        String fullEndpoint = ApiActions.buildChcServiceEndpoint( "/chc/url/order/accessOrders?locationId=%s&requisitionId=%s", data.get("location_id"), data.get("requisition_id"));
        Assertions.assertThat(ApiRequestBuilder.requestGetCallUsingInvalidToken(fullEndpoint)).isEqualTo(true);
    }

    @Test(groups = {"accessOrders",  "fullRegression"})
    public void TC003_GET_AccessOrders_invalidHeader() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT location_id, requisition_id FROM ACCESS_ORDERS WHERE requisition_id IS NOT NULL LIMIT 1;");
        String fullEndpoint = ApiActions.buildChcServiceEndpoint("/chc/url/order/accessOrders?locationId=%s&requisitionId=%s", data.get("location_id"), data.get("requisition_id"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.requestGetCallUsingInvalidHeader(fullEndpoint,"CHC"), 404)).isEqualTo(true);

    }
    @Test(groups = {"accessOrders",  "fullRegression"})
    public void TC004_GET_AccessOrders_invalidAlphaNumeric() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT requisition_id FROM ACCESS_ORDERS WHERE requisition_id IS NOT NULL LIMIT 1;");
        String fullEndpoint = ApiActions.buildChcServiceEndpoint("/chc/url/order/accessOrders?locationId=%s&requisitionId=%s", RandomUtils.generateRandomNumericString(15), data.get("requisition_id"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getChcServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }
    @Test(groups = {"accessOrders",  "fullRegression"})
    public void TC005_GET_AccessOrders_invalidPath() {
        data = DatabaseQueryExecutor.retrieveRowData("SELECT location_id, requisition_id FROM ACCESS_ORDERS WHERE requisition_id IS NOT NULL LIMIT 1;");
        String fullEndpoint = ApiActions.buildChcServiceEndpoint("/chc/url/order/accessOrderz?locationId=%s&requisitionId=%s", data.get("location_id"), data.get("requisition_id"));
        Assertions.assertThat(AssertionUtils.isStatusCodeEqualTo(ApiRequestBuilder.getChcServiceResponse(fullEndpoint),404)).isEqualTo(true);
    }




}