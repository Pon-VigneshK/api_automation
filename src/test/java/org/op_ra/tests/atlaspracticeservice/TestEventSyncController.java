package org.op_ra.tests.atlaspracticeservice;

import org.assertj.core.api.Assertions;
import org.op_ra.annotations.FrameworkAnnotation;
import org.op_ra.enums.CategoryType;
import org.op_ra.requestbuilder.ApiActions;
import org.op_ra.requestbuilder.RequestBuilder;
import org.testng.annotations.Test;

/**
 * This class contains tests for the Event Sync Controller of the Atlas Practice Service.
 * It includes tests for syncing various types of data.
 *
 * @version 1.0
 * @since 2023-10-27
 */
public class TestEventSyncController {
    String requestBody = "{}";

    /**
     * This test verifies syncing all data.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC001_POST_Sync_All() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/all");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing lite data.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC002_POST_Sync_Lite() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/lite");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing organizations.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC003_POST_Sync_Organizations() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/organizations");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing practices.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC004_POST_Sync_Practices() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/practices");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing locations.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC005_POST_Sync_Locations() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/locations");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing departments.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC006_POST_Sync_Departments() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/departments");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing staffs.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC007_POST_Sync_Staffs() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/staffs");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing patients.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC008_POST_Sync_Patients() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/patients");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }

    /**
     * This test verifies syncing providers.
     */
    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.HIGH_LEVEL_TESTING, CategoryType.FULL_REGRESSION})
    @Test(groups = {"eventSyncController", "smoke", "highLevel", "fullRegression", "writeAPICall"})
    public void TC009_POST_Sync_Providers() {
        String fullEndpoint = ApiActions.formEndpoint("/sync/providers");
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestPostCall(fullEndpoint, requestBody), 200)).isEqualTo(true);
    }
}
