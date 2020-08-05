package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LimsSampleInfoDnaMapper;
import com.bazl.alims.model.po.LimsSampleInfoDna;
import com.bazl.alims.service.LimsSampleInfoDnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class LimsSampleInfoDnaServiceImpl implements LimsSampleInfoDnaService {

    @Autowired
    LimsSampleInfoDnaMapper limsSampleInfoDnaMapper;

    /**
     * 查询人员样本
     * @param sampleInfoDna
     * @return
     */
    @Override
    public LinkedList<LimsSampleInfoDna> selectByCaseIdAndRy(LimsSampleInfoDna sampleInfoDna) {
        LinkedList<LimsSampleInfoDna> limsPersonInfoList = limsSampleInfoDnaMapper.selectByCaseIdAndRy(sampleInfoDna);
        return limsPersonInfoList;
    }

    /**
     * 查询检材
     * @param sampleInfoDna
     * @return
     */
    @Override
    public List<LimsSampleInfoDna> selectByCaseIdAndWz(LimsSampleInfoDna sampleInfoDna) {
        List<LimsSampleInfoDna> limsPersonInfoList = limsSampleInfoDnaMapper.selectByCaseIdAndWz(sampleInfoDna);
        return limsPersonInfoList;
    }

    /**
     * 根据案件id查询物证信息
     * @param caseId
     * @return
     */
    @Override
    public List<LimsSampleInfoDna> selectByCaseId(String caseId) {
        List<LimsSampleInfoDna> limsPersonInfoList = limsSampleInfoDnaMapper.selectByCaseId(caseId);
        return limsPersonInfoList;
    }

    /**
     * 根据案件id查询样本信息
     * @param caseId
     * @return
     */
    @Override
    public List<LimsSampleInfoDna> selectYbByCaseId(String caseId) {
        List<LimsSampleInfoDna> limsSampleInfoDnaList = limsSampleInfoDnaMapper.selectYbByCaseId(caseId);
        return limsSampleInfoDnaList;
    }
    /**
     * 查询非现场检材
     * @param sampleInfoDna
     * @return
     */
    @Override
    public List<LimsSampleInfoDna> selectByCaseIdAndWzNon(LimsSampleInfoDna sampleInfoDna) {
        List<LimsSampleInfoDna> limsPersonInfoList = limsSampleInfoDnaMapper.selectByCaseIdAndWzNon(sampleInfoDna);
        return limsPersonInfoList;
    }

    @Override
    public LimsSampleInfoDna selectBySampleName(String caseId, String sampleNameXK) {
        LimsSampleInfoDna limsSampleInfoDna = limsSampleInfoDnaMapper.selectBySampleName(caseId,sampleNameXK);
        return limsSampleInfoDna;
    }

    @Override
    public void updateWno(LimsSampleInfoDna limsSampleInfoDna) {
        limsSampleInfoDnaMapper.updateSampleInfoDnaWno(limsSampleInfoDna);
    }
}
