package com.bazl.alims.service;

import com.bazl.alims.model.po.LimsSampleInfoDna;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public interface LimsSampleInfoDnaService {

    LinkedList<LimsSampleInfoDna> selectByCaseIdAndRy(LimsSampleInfoDna sampleInfoDna);

    List<LimsSampleInfoDna> selectByCaseIdAndWz(LimsSampleInfoDna sampleInfoDna);

    List<LimsSampleInfoDna> selectByCaseId(String caseId);

    List<LimsSampleInfoDna> selectYbByCaseId(String caseId);

    /**
     * 查询非现场案件
     * @param sampleInfoDna
     * @return
     */
    List<LimsSampleInfoDna> selectByCaseIdAndWzNon(LimsSampleInfoDna sampleInfoDna);

    /**
     * 根据案件id,检材名称，查询物证检材编号是否存在
     * @param caseId
     * @param sampleNameXK
     * @return
     */
    LimsSampleInfoDna selectBySampleName(String caseId, String sampleNameXK);

    /**
     * 根据案件id,检材名称，更新W号
     * @param limsSampleInfoDna
     */
    void updateWno(LimsSampleInfoDna limsSampleInfoDna);

}