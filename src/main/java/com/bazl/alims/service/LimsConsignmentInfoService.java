package com.bazl.alims.service;

import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.bo.DelegateDataModel;
import com.bazl.alims.model.po.LimsConsignmentInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LimsConsignmentInfoService {

    public Map<String, String> submitDelegate(DelegateDataModel delegateDataModel, LoaUserInfo operateUser, String personIds, String sampleIdWzs, String sampleIds, String evaluationCenterId);

    public void delCaseAndBring(String consignmentId, String caseId, LoaUserInfo operateUser);

    public LimsConsignmentInfo selectByConsignmentId(String consignmentId);

    public List<LimsConsignmentInfo> selectByCaseId(String caseId);

    public Map<String, String> submitNonDelegate(DelegateDataModel delegateDataModel, LoaUserInfo operateUser, String personIds, String sampleIds, String evaluationCenterId,String sampleIdWzs);

    /**
     * 保存在逃委托信息
     * @param delegateDataModel
     * @param operateUser
     * @param personIds
     * @param sampleIds
     * @param evaluationCenterId
     * @param sampleIdWzs
     * @return
     */
    public Map<String, String> submitFugitivesDelegate(DelegateDataModel delegateDataModel, LoaUserInfo operateUser, String personIds, String sampleIds, String evaluationCenterId, String sampleIdWzs);

    public LimsConsignmentInfo selectByDelegateOrgCode(String delegateOrgCode);

    public LimsConsignmentInfo selectByConsignmentNo(String consignmentNo, String delegateOrgCode);

    List<LimsConsignmentInfo> selectByCaseXkNoTwo(String caseXkNo);
}