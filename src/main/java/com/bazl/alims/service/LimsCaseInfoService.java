package com.bazl.alims.service;

import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.PageInfo;
import com.bazl.alims.model.bo.DelegateDataModel;
import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.vo.LimsCaseInfoVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface LimsCaseInfoService {

    LimsCaseInfo selectByCaseId(String caseId);

    List<LimsCaseInfo> selectByCaseXkNo(String caseXkNo, String acceptOrgId);

    /**
     * 查询案件数量
     */
    public int selectCountByCaseStatus(String status, String delegateOrgCode);

    /**
     * 根据年份获取各个月份的案件数
     */
    public HashMap selectMonthCountByYear(String year, String delegateOrgCode);

    //查询与补送查询List
    public List<LimsCaseInfoVo> selectCaseInfoList(LimsCaseInfoVo query, PageInfo pageInfo);

    //查询与补送查询count
    public int selectVOCount(LimsCaseInfoVo query);

    //添加委托补送信息
    public Map<String, String> submitReplacement(DelegateDataModel delegateDataModel, LoaUserInfo operateUser,String evaluationCenterId);

    //查询补送记录
    public List<LimsCaseInfoVo> selectReplacementRecord(LimsCaseInfoVo limsCaseInfoVo);

    //添加非案件委托补送信息
    public Map<String, String> submitNonCaseReplacement(DelegateDataModel delegateDataModel, LoaUserInfo operateUser,String evaluationCenterId);

    //通用查询分页list
    public List<LimsCaseInfoVo> selectCaseQueryInfoList(LimsCaseInfoVo query, PageInfo pageInfo);

    //通用查询count
    public int selectCaseQueryVOCount(LimsCaseInfoVo query);

    int queryXkNoCount(String caseXkNo);

    /**
     * 定时任务更新A号,WT号
     * @param caseXkNo
     * @param xkAno
     */
    void updateCaseXAno(String caseXkNo, String xkAno,String consignationXkNo);

    List<LimsCaseInfoVo> selectAllConsignmentIdw(Integer offset, Integer rows);
}