package com.bazl.alims.dao;


import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.vo.LimsCaseInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface LimsCaseInfoMapper {

    void insertCaseInfo(LimsCaseInfo caseInfo);

    void deleteCaseInfo(LimsCaseInfo caseInfo);

    LimsCaseInfo selectByCaseId(String caseId);

    List<LimsCaseInfo> selectByCaseXkNo(@Param("caseXkNo")String caseXkNo, @Param("acceptOrgId")String acceptOrgId);

    int selectCountByCaseStatus(@Param("status")String status, @Param("delegateOrgCode")String delegateOrgCode);

    HashMap selectMonthCountByYear(@Param("year")String year, @Param("delegateOrgCode")String delegateOrgCode);

    void updateCaseInfoDna(LimsCaseInfo limsCaseInfo);

    List<LimsCaseInfoVo> selectVOPaginationList(LimsCaseInfoVo caseInfoVo);

    int selectVOCount(LimsCaseInfoVo consignationInfoVo);

    void updateHasAppendFlagByCaseId(LimsCaseInfo limsCaseInfo);

    List<LimsCaseInfoVo> selectReplacementRecord(LimsCaseInfoVo limsCaseInfoVo);

    List<LimsCaseInfoVo> selectCaseQueryInfoList(LimsCaseInfoVo caseInfoVo);

    int selectCaseQueryVOCount(LimsCaseInfoVo consignationInfoVo);

    int queryXkNoCount(@Param("caseXkNo")String caseXkNo);

    /**
     * 查询所有新系统案件信息
     * @return
     */
    List<LimsCaseInfo> selectAllCase();

    /**
     * 根据K号，更新A号，WT号
     * @param caseXkNo
     * @param xkAno
     */
    void updateCaseXAno(@Param("caseXkNo")String caseXkNo, @Param("xkAno")String xkAno,@Param("consignationXkNo")String consignationXkNo);

    /**
     * 查询所有新系统案件信息 委托id
     * @return
     */
    List<LimsCaseInfoVo> selectAllConsignmentId(LimsCaseInfoVo caseInfoVo);
}