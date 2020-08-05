package com.bazl.alims.dao;


import com.bazl.alims.model.po.LimsSampleInfoDna;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public interface LimsSampleInfoDnaMapper {

    void insertSampleInfoDna(LimsSampleInfoDna sampleInfoDna);

    LimsSampleInfoDna selectSampleInfoDnaById(String sampleId);

    void updateSampleInfoDna(LimsSampleInfoDna sampleInfoDna);

    LinkedList<LimsSampleInfoDna> selectByCaseIdAndRy(LimsSampleInfoDna sampleInfoDna);

    List<LimsSampleInfoDna> selectByCaseIdAndWz(LimsSampleInfoDna sampleInfoDna);

    void deleteByCaseIdAndConsignmentId(LimsSampleInfoDna sampleInfoDna);

    void deleteBySampleId(String sampleId);

    LimsSampleInfoDna selectById(String sampleId);

    List<LimsSampleInfoDna> selectByCaseId(String caseId);

    List<LimsSampleInfoDna> selectYbByCaseId(String caseId);
    //查询非现场案件检材
    List<LimsSampleInfoDna> selectByCaseIdAndWzNon(LimsSampleInfoDna sampleInfoDna);

    LimsSampleInfoDna selectBySampleName(@Param("caseId")String caseId, @Param("sampleName")String sampleNameXK);

    void updateSampleInfoDnaWno(LimsSampleInfoDna limsSampleInfoDna);



}