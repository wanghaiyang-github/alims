package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LimsPersonInfoMapper;
import com.bazl.alims.model.po.LimsPersonInfo;
import com.bazl.alims.service.LimsPersonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class LimsPersonInfoServiceImpl implements LimsPersonInfoService {

    @Autowired
    LimsPersonInfoMapper limsPersonInfoMapper;


    @Override
    public List<LimsPersonInfo> selectByCaseId(String caseId) {
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoMapper.selectByCaseId(caseId);
        return limsPersonInfoList;
    }

    @Override
    public List<LimsPersonInfo> selectByCaseIdAndConsignmentId(LimsPersonInfo limsPersonInfo) {
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoMapper.selectByCaseIdAndConsignmentId(limsPersonInfo);
        return limsPersonInfoList;
    }

    @Override
    public LimsPersonInfo selectPersonInfoById(String personId) {
        LimsPersonInfo personInfo = limsPersonInfoMapper.selectPersonInfoById(personId);
        return personInfo;
    }
}
