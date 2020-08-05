package com.bazl.alims.model.po;

public class DelegateCenterConfig {
    private String delegateOrgCodePrefix;

    private String labOrgId;

    private String defaultCenterFlag;

    /* 业务字段 单位资质 */
    private String orgQualification;

    /* 业务字段  */
    private String orgContactPhone;

    /* 业务字段 电话地址 */
    private String orgLevel;

    /**
     * 主键ID
     */
    private String orgId;
    /**
     * 父单位主键ID
     */
    private String parentId;
    /**
     * 单位名称
     */
    private String orgName;
    /**
     * 单位编号
     */
    private String orgCode;

    /**
     * 单位地址
     */
    private String orgAddress;

    /**
     * 单位邮编
     */
    private String orgZipCode;
    /**
     * 单位负责人
     */
    private String orgPrincipalPhone;
    /**
     * 单位负责人电话
     */
    private String orgPhoneNumber;
    /**
     * 单位联系人
     */
    private String orgContact;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getOrgZipCode() {
        return orgZipCode;
    }

    public void setOrgZipCode(String orgZipCode) {
        this.orgZipCode = orgZipCode;
    }

    public String getOrgPrincipalPhone() {
        return orgPrincipalPhone;
    }

    public void setOrgPrincipalPhone(String orgPrincipalPhone) {
        this.orgPrincipalPhone = orgPrincipalPhone;
    }

    public String getOrgPhoneNumber() {
        return orgPhoneNumber;
    }

    public void setOrgPhoneNumber(String orgPhoneNumber) {
        this.orgPhoneNumber = orgPhoneNumber;
    }

    public String getOrgContact() {
        return orgContact;
    }

    public void setOrgContact(String orgContact) {
        this.orgContact = orgContact;
    }

    public String getOrgContactPhone() {
        return orgContactPhone;
    }

    public void setOrgContactPhone(String orgContactPhone) {
        this.orgContactPhone = orgContactPhone;
    }

    public String getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(String orgLevel) {
        this.orgLevel = orgLevel;
    }

    public String getOrgQualification() {
        return orgQualification;
    }

    public void setOrgQualification(String orgQualification) {
        this.orgQualification = orgQualification;
    }

    public String getDelegateOrgCodePrefix() {
        return delegateOrgCodePrefix;
    }

    public void setDelegateOrgCodePrefix(String delegateOrgCodePrefix) {
        this.delegateOrgCodePrefix = delegateOrgCodePrefix == null ? null : delegateOrgCodePrefix.trim();
    }

    public String getLabOrgId() {
        return labOrgId;
    }

    public void setLabOrgId(String labOrgId) {
        this.labOrgId = labOrgId == null ? null : labOrgId.trim();
    }

    public String getDefaultCenterFlag() {
        return defaultCenterFlag;
    }

    public void setDefaultCenterFlag(String defaultCenterFlag) {
        this.defaultCenterFlag = defaultCenterFlag == null ? null : defaultCenterFlag.trim();
    }
}