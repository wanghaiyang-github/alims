package com.bazl.alims.model.vo;

import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.utils.JsonDatetimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/4.
 */
public class LimsCaseInfoVo extends AbstractBaseVo<LimsCaseInfo> implements Serializable {

    public LimsCaseInfoVo() {
        super();
        this.entity = new LimsCaseInfo();
    }

    public LimsCaseInfoVo(LimsCaseInfo entity) {
        super();
        this.entity = entity;
    }

    private String consignmentId;

    private String consignmentNo;

    private String delegator1Id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date delegateStartDatetime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date delegateEndDatetime;

    private String delegator1Name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date delegateDatetime;

    private String caseTypeName;

    private String casePropertyName;

    private String caseStatusName;

    private String createPerson;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date createDatetime;

    private String delegator2Name;

    private String delegator2Id;

    private String delegateOrgCode;

    private String delegateOrgName;

    private String status;

    private String appendFlag;

    private String acceptOrgId;

    private String acceptorId;
    //鉴定中心
    private String orgQualification;
    //受理时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date acceptDatetime;
    //补送第几次数
    private Integer replacementNum;

    private String xkStatus;

    /**
     * 所属辖区
     * @return
     */
    private String areaOrgCode;

    public String getAreaOrgCode() {
        return areaOrgCode;
    }

    public void setAreaOrgCode(String areaOrgCode) {
        this.areaOrgCode = areaOrgCode;
    }

    public String getXkStatus() {
        return xkStatus;
    }

    public void setXkStatus(String xkStatus) {
        this.xkStatus = xkStatus;
    }

    public Date getAcceptDatetime() {
        return acceptDatetime;
    }

    public void setAcceptDatetime(Date acceptDatetime) {
        this.acceptDatetime = acceptDatetime;
    }

    public Integer getReplacementNum() {
        return replacementNum;
    }

    public void setReplacementNum(Integer replacementNum) {
        this.replacementNum = replacementNum;
    }

    public String getOrgQualification() {
        return orgQualification;
    }

    public void setOrgQualification(String orgQualification) {
        this.orgQualification = orgQualification;
    }

    public String getAcceptorId() {
        return acceptorId;
    }

    public void setAcceptorId(String acceptorId) {
        this.acceptorId = acceptorId;
    }

    public String getAppendFlag() {
        return appendFlag;
    }

    public void setAppendFlag(String appendFlag) {
        this.appendFlag = appendFlag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelegateOrgName() {
        return delegateOrgName;
    }

    public void setDelegateOrgName(String delegateOrgName) {
        this.delegateOrgName = delegateOrgName;
    }

    public String getDelegateOrgCode() {
        return delegateOrgCode;
    }

    public void setDelegateOrgCode(String delegateOrgCode) {
        this.delegateOrgCode = delegateOrgCode;
    }

    public String getDelegator2Id() {
        return delegator2Id;
    }

    public void setDelegator2Id(String delegator2Id) {
        this.delegator2Id = delegator2Id;
    }

    public String getDelegator2Name() {
        return delegator2Name;
    }

    public void setDelegator2Name(String delegator2Name) {
        this.delegator2Name = delegator2Name;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getDelegateDatetime() {
        return delegateDatetime;
    }

    public void setDelegateDatetime(Date delegateDatetime) {
        this.delegateDatetime = delegateDatetime;
    }

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getConsignmentNo() {
        return consignmentNo;
    }

    public void setConsignmentNo(String consignmentNo) {
        this.consignmentNo = consignmentNo;
    }

    public String getDelegator1Id() {
        return delegator1Id;
    }

    public void setDelegator1Id(String delegator1Id) {
        this.delegator1Id = delegator1Id;
    }

    public Date getDelegateStartDatetime() {
        return delegateStartDatetime;
    }

    public void setDelegateStartDatetime(Date delegateStartDatetime) {
        this.delegateStartDatetime = delegateStartDatetime;
    }

    public Date getDelegateEndDatetime() {
        return delegateEndDatetime;
    }

    public void setDelegateEndDatetime(Date delegateEndDatetime) {
        this.delegateEndDatetime = delegateEndDatetime;
    }

    public String getDelegator1Name() {
        return delegator1Name;
    }

    public void setDelegator1Name(String delegator1Name) {
        this.delegator1Name = delegator1Name;
    }

    public String getCaseTypeName() {
        return caseTypeName;
    }

    public void setCaseTypeName(String caseTypeName) {
        this.caseTypeName = caseTypeName;
    }

    public String getCasePropertyName() {
        return casePropertyName;
    }

    public void setCasePropertyName(String casePropertyName) {
        this.casePropertyName = casePropertyName;
    }

    public String getCaseStatusName() {
        return caseStatusName;
    }

    public void setCaseStatusName(String caseStatusName) {
        this.caseStatusName = caseStatusName;
    }

    public String getAcceptOrgId() {
        return acceptOrgId;
    }

    public void setAcceptOrgId(String acceptOrgId) {
        this.acceptOrgId = acceptOrgId;
    }
}
