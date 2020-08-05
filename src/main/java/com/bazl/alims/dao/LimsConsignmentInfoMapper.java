package com.bazl.alims.dao;


import com.bazl.alims.model.po.LimsConsignmentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimsConsignmentInfoMapper {

    void insertConsignatioInfo(LimsConsignmentInfo consignatioInfo);

    void deleteConsignationInfo(LimsConsignmentInfo consignatioInfo);

    LimsConsignmentInfo selectByConsignmentId(String consignmentId);

    void updateConsignatioInfo(LimsConsignmentInfo limsConsignmentInfo);

    List<LimsConsignmentInfo> selectByCaseId(String caseId);

    public LimsConsignmentInfo selectByDelegateOrgCode(String delegateOrgCode);

    public LimsConsignmentInfo selectByConsignmentNo(@Param("consignmentNo")String consignmentNo, @Param("delegateOrgCode")String delegateOrgCode);
    //查询补送第几次数
    LimsConsignmentInfo selectMaxReplacementNum(String caseId);
    //更新补送次数
    void updateReplacementNum(LimsConsignmentInfo limsConsignmentInfo);

    List<LimsConsignmentInfo> selectByCaseXkNoTwo(String caseXkNo);
}