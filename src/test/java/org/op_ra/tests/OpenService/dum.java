package org.op_ra.tests.OpenService;

import static org.op_ra.enums.ConfigProperties.OPEN_ACTOR_BASE_URL;
import static org.op_ra.enums.ConfigProperties.OPEN_CHART_BASE_URL;
import static org.op_ra.enums.ConfigProperties.OPEN_CHC_BASE_URL;
import static org.op_ra.enums.ConfigProperties.OPEN_CODING_BASE_URL;
import static org.op_ra.enums.ConfigProperties.OPEN_DOCUMENT_BASE_URL;
import static org.op_ra.enums.ConfigProperties.OPEN_ERX_BASE_URL;
import static org.op_ra.enums.ConfigProperties.OPEN_LAB_BASE_URL;
import static org.op_ra.utils.PropertyUtils.getValue;


public class dum {

    public static void main(String[] args) {
        System.out.println(getValue(OPEN_ACTOR_BASE_URL));
        System.out.println(getValue(OPEN_CHART_BASE_URL));
        System.out.println(getValue(OPEN_CHC_BASE_URL));
        System.out.println(getValue(OPEN_CODING_BASE_URL));
        System.out.println(getValue(OPEN_DOCUMENT_BASE_URL));
        System.out.println(getValue(OPEN_ERX_BASE_URL));
        System.out.println(getValue(OPEN_LAB_BASE_URL));
    }

}
