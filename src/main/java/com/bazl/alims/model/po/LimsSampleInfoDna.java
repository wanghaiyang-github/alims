package com.bazl.alims.model.po;

import com.bazl.alims.utils.JsonDatetimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * lims_sample_info_dna
 * 物证检材信息表
 *  Created by hj on 2018/12/24
 */
public class LimsSampleInfoDna {

    /**
     * 检材信息主键id
     */
    private String sampleId;

    /**
     * 主键id
     */
    private String consignmentId;

    /**
     * 案件信息主键id
     */
    private String caseId;

    /**
     * 物证信息主键id
     */
    private String evidenceId;

    /**
     * 物证信息编号
     */
    private String evidenceNo;

    /**
     * 物证序号
     */
    private Integer evidenceSerialNo;


    /**
     * 检材编号
     */
    private String sampleNo;

    /**
     * 检材名称
     */
    private String sampleName;

    /**
     * 检材类型
     */
    private String sampleType;

    /**
     * 检材描述
     */
    private String sampleDesc;

    /**
     * 检材包装
     */
    private String samplePacking;

    /**
     * 检材载体（棉签、血卡）
     */
    private String sampleCarrier;

    /**
     * 提取时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date extractDatetime;

    /**
     * 提取人
     */
    private String extractPerson;

    /**
     * 提取方法（粘取、剪等）
     */
    private String extractMethod;

    /**
     * 提取部位
     */
    private String extractPart;

    /**
     * 检材类别（0：物证，1：人员）
     */
    private String sampleFlag;

    /**
     * 人员或物证主键id
     */
    private String linkId;

    /**
     * 送检目的
     */
    private String samplePurpose;

    /**
     * 样本状态
     */
    private String sampleStatus;

    /**
     * 入库标识（0：未入库，1：已入本地库）
     */
    private String instoredFlag;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date createDatetime;

    /**
     * 创建人
     */
    private String createPerson;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date updateDatetime;

    /**
     * 更新人
     */
    private String updatePerson;

    /**
     * 删除标记
     */
    private String deleteFlag;

    /**
     * 删除时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date deleteDatetime;

    /**
     * 删除人
     */
    private String deletePerson;

    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 备注信息
     */
    private String sampleRemark;

    /**
     * 检材类型名称
     */
    private String sampleTypeName;

    /**
     * 样本照片
     */
    private String sampleDnaPicture;
    /**
     * 样本照片路径
     */
    private String sampleDnaPicturePath;
    /**
     * 物证检材照片
     */
    private String sampleMaterialPicture;
    /**
     * 物证检材照片路径
     */
    private String sampleMaterialPicturePath;

    /**是否中心取*/
    private String coreTakenStats;

    /**是否事主取*/
    private String coreVictimStats;

    public String getSampleMaterialPicture() {
        return sampleMaterialPicture;
    }

    public void setSampleMaterialPicture(String sampleMaterialPicture) {
        this.sampleMaterialPicture = sampleMaterialPicture;
    }

    public String getSampleMaterialPicturePath() {
        return sampleMaterialPicturePath;
    }

    public void setSampleMaterialPicturePath(String sampleMaterialPicturePath) {
        this.sampleMaterialPicturePath = sampleMaterialPicturePath;
    }

    private String personName;

    private String extractMethodName;

    private String samplePackingName;

    private String sampleIdwz;

    public String getSampleIdwz() {
        return sampleIdwz;
    }

    public void setSampleIdwz(String sampleIdwz) {
        this.sampleIdwz = sampleIdwz;
    }

    public String getExtractMethodName() {
        return extractMethodName;
    }

    public void setExtractMethodName(String extractMethodName) {
        this.extractMethodName = extractMethodName;
    }

    public String getSamplePackingName() {
        return samplePackingName;
    }

    public void setSamplePackingName(String samplePackingName) {
        this.samplePackingName = samplePackingName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getSampleDnaPicture() {
        return sampleDnaPicture;
    }

    public void setSampleDnaPicture(String sampleDnaPicture) {
        this.sampleDnaPicture = sampleDnaPicture;
    }

    public String getSampleDnaPicturePath() {
        return sampleDnaPicturePath;
    }

    public void setSampleDnaPicturePath(String sampleDnaPicturePath) {
        this.sampleDnaPicturePath = sampleDnaPicturePath;
    }

    public String getSampleTypeName() {
        return sampleTypeName;
    }

    public void setSampleTypeName(String sampleTypeName) {
        this.sampleTypeName = sampleTypeName;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getConsignmentId() {
        return consignmentId;
    }

    public void setConsignmentId(String consignmentId) {
        this.consignmentId = consignmentId;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
    }

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSampleDesc() {
        return sampleDesc;
    }

    public void setSampleDesc(String sampleDesc) {
        this.sampleDesc = sampleDesc;
    }

    public String getSamplePacking() {
        return samplePacking;
    }

    public void setSamplePacking(String samplePacking) {
        this.samplePacking = samplePacking;
    }

    public String getSampleCarrier() {
        return sampleCarrier;
    }

    public void setSampleCarrier(String sampleCarrier) {
        this.sampleCarrier = sampleCarrier;
    }

    public Date getExtractDatetime() {
        return extractDatetime;
    }

    public void setExtractDatetime(Date extractDatetime) {
        this.extractDatetime = extractDatetime;
    }

    public String getExtractPerson() {
        return extractPerson;
    }

    public void setExtractPerson(String extractPerson) {
        this.extractPerson = extractPerson;
    }

    public String getExtractMethod() {
        return extractMethod;
    }

    public void setExtractMethod(String extractMethod) {
        this.extractMethod = extractMethod;
    }

    public String getExtractPart() {
        return extractPart;
    }

    public void setExtractPart(String extractPart) {
        this.extractPart = extractPart;
    }

    public String getSampleFlag() {
        return sampleFlag;
    }

    public void setSampleFlag(String sampleFlag) {
        this.sampleFlag = sampleFlag;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getSamplePurpose() {
        return samplePurpose;
    }

    public void setSamplePurpose(String samplePurpose) {
        this.samplePurpose = samplePurpose;
    }

    public String getSampleStatus() {
        return sampleStatus;
    }

    public void setSampleStatus(String sampleStatus) {
        this.sampleStatus = sampleStatus;
    }

    public String getInstoredFlag() {
        return instoredFlag;
    }

    public void setInstoredFlag(String instoredFlag) {
        this.instoredFlag = instoredFlag;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Date getDeleteDatetime() {
        return deleteDatetime;
    }

    public void setDeleteDatetime(Date deleteDatetime) {
        this.deleteDatetime = deleteDatetime;
    }

    public String getDeletePerson() {
        return deletePerson;
    }

    public void setDeletePerson(String deletePerson) {
        this.deletePerson = deletePerson;
    }

    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getSampleRemark() {
        return sampleRemark;
    }

    public void setSampleRemark(String sampleRemark) {
        this.sampleRemark = sampleRemark;
    }

    public String getEvidenceNo() {
        return evidenceNo;
    }

    public void setEvidenceNo(String evidenceNo) {
        this.evidenceNo = evidenceNo;
    }

    public String getCoreTakenStats() {
        return coreTakenStats;
    }

    public void setCoreTakenStats(String coreTakenStats) {
        this.coreTakenStats = coreTakenStats;
    }

    public String getCoreVictimStats() {return coreVictimStats;}

    public void setCoreVictimStats(String coreVictimStats) {this.coreVictimStats = coreVictimStats;}

    public Integer getEvidenceSerialNo() {
        return evidenceSerialNo;
    }

    public void setEvidenceSerialNo(Integer evidenceSerialNo) {
        this.evidenceSerialNo = evidenceSerialNo;
    }
}



