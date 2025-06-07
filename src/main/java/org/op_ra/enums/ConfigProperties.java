package org.op_ra.enums;

public enum ConfigProperties {

    /**
     * Enums to restrict the values used on Property files. Without using enums there can be created null pointer exceptions
     * because of typos.
     * Whenever a new value is added to property file, corresponding enum should be created here.
     */
    // Framework Configuration Properties
    ENV,
    RUN_MODE,
    OVERRIDE_REPORTS,
    RETRY,
    LOG_RESPONSE,

    // Authentication Properties
    AUTH_TYPE,
    ACCESS_TOKEN_URL,
    CLIENT_ID,
    CLIENT_SECRET,
    SCOPE,
    AUDIENCE,
    AUTH_PREFIX,
    API_KEY,


    // Service Base URLs
    OPEN_ACTOR_BASE_URL,
    OPEN_CHART_BASE_URL,
    OPEN_CHC_BASE_URL,
    OPEN_CODING_BASE_URL,
    OPEN_DOCUMENT_BASE_URL,
    OPEN_ERX_BASE_URL,
    OPEN_LAB_BASE_URL,
    OPEN_JOBS_BASE_URL,


    // Service-specific Basic Authentication - Actor
    OPEN_ACTOR_USERNAME,
    OPEN_ACTOR_PASSWORD,

    // Service-specific Basic Authentication - Chart
    OPEN_CHART_USERNAME,
    OPEN_CHART_PASSWORD,

    // Service-specific Basic Authentication - CHC
    OPEN_CHC_USERNAME,
    OPEN_CHC_PASSWORD,

    // Service-specific Basic Authentication - Coding
    OPEN_CODING_USERNAME,
    OPEN_CODING_PASSWORD,

    // Service-specific Basic Authentication - Document
    OPEN_DOCUMENT_USERNAME,
    OPEN_DOCUMENT_PASSWORD,

    // Service-specific Basic Authentication - ERX
    OPEN_ERX_USERNAME,
    OPEN_ERX_PASSWORD,

    // Service-specific Basic Authentication - Lab
    OPEN_LAB_USERNAME,
    OPEN_LAB_PASSWORD,

    // Service-specific Basic Authentication - Job
    OPEN_JOB_USERNAME,
    OPEN_JOB_PASSWORD,



    // Service-specific Headers - Actor
    OPEN_ACTOR_ACCEPT_HEADER,
    OPEN_ACTOR_CONTENT_TYPE_HEADER,
    OPEN_ACTOR_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - Chart
    OPEN_CHART_ACCEPT_HEADER,
    OPEN_CHART_CONTENT_TYPE_HEADER,
    OPEN_CHART_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - CHC
    OPEN_CHC_ACCEPT_HEADER,
    OPEN_CHC_CONTENT_TYPE_HEADER,
    OPEN_CHC_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - Coding
    OPEN_CODING_ACCEPT_HEADER,
    OPEN_CODING_CONTENT_TYPE_HEADER,
    OPEN_CODING_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - Document
    OPEN_DOCUMENT_ACCEPT_HEADER,
    OPEN_DOCUMENT_CONTENT_TYPE_HEADER,
    OPEN_DOCUMENT_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - ERX
    OPEN_ERX_ACCEPT_HEADER,
    OPEN_ERX_CONTENT_TYPE_HEADER,
    OPEN_ERX_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - Lab
    OPEN_LAB_ACCEPT_HEADER,
    OPEN_LAB_CONTENT_TYPE_HEADER,
    OPEN_LAB_ORGANIZATION_ID_HEADER,

    // Service-specific Headers - Job
    OPEN_JOB_ACCEPT_HEADER,
    OPEN_JOB_CONTENT_TYPE_HEADER,
    OPEN_JOB_ORGANIZATION_ID_HEADER,

}
