package org.op_ra.enums;

public enum ConfigProperties {

    /**
     * Enums to restrict the values used on Property files. Without using enums there can be created null pointer exceptions
     * because of typos.
     * Whenever a new value is added to property file, corresponding enum should be created here.
     */
    BASEURL,
    OVERRIDEREPORTS,
    RETRY,
    ENV,
    AUTHTYPE,
    RUNMODE,
    ACCESSTOKENURL,
    CLIENTID,
    CLIENTSECRET,
    SCOPE,
    AUDIENCE,
    AUTHPREFIX,
    USERNAME,
    PASSWORD,
    LOGRESPONSE,
    AUDITLOGDB,
    APIKEY,
    NEEDHEADER,
    ACCEPTHEADER,
    CONTENTTYPEHEADER,
    ORGANIZATIONIDHEADER,
    PRACTICEIDHEADER;


}
