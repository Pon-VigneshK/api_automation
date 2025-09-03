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

public class TestVisitTemplateController {
    Map<String, String> data = new HashMap<>();


    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"visitTemplateController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC001_GET_Read_VisitTemplateById() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM visit_template where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/visit-templates/%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "templateType", data.get("template_type"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"visitTemplateController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC002_GET_Read_VisitTemplateByLocalId() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM visit_template where fk_organization_id=%s and  local_id is not null and id is not null and template_type is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/visit-templates/localId/%s", data.get("local_id"));
        Assertions.assertThat(ApiActions.isStatusCodeEqualTo(RequestBuilder.requestGetCall(fullEndpoint), 200)).isEqualTo(true);
    }


    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"visitTemplateController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC003_GET_Read_VisitTemplateSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM visit_template where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/visit-templates/search?id=%s", data.get("id"));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "templateType", data.get("template_type"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }

    @FrameworkAnnotation(author = {"Vicky"}, category = {CategoryType.SMOKE, CategoryType.FULL_REGRESSION})
    @Test(groups = {"visitTemplateController", "smoke", "highLevel", "fullRegression", "readAPICall"})
    public void TC004_GET_Read_VisitTemplateAdvancedSearch() {
        data = DatabaseQueryExecutor.retrieveRowData(String.format("SELECT * FROM visit_template where fk_organization_id = %s and id is not null limit 1;", getValue(ORGANIZATIONIDHEADER)));
        String fullEndpoint = ApiActions.formEndpoint("/visit-templates/advanced-search?search=%s", String.format("id:%s", data.get("id")));
        Assertions.assertThat(ApiActions.verifyJsonKeyValues(RequestBuilder.requestGetCall(fullEndpoint), Map.of("id", data.get("id"), "localId", data.get("local_id"), "templateType", data.get("template_type"), "organizationId", getValue(ORGANIZATIONIDHEADER)))).isEqualTo(true);
    }
}
