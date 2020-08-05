package com.bazl.alims.service;

import com.bazl.alims.model.po.LimsPersonInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimsPersonInfoService {

    List<LimsPersonInfo> selectByCaseId(String caseId);

    List<LimsPersonInfo> selectByCaseIdAndConsignmentId(LimsPersonInfo limsPersonInfo);

    LimsPersonInfo selectPersonInfoById(String personId);
}