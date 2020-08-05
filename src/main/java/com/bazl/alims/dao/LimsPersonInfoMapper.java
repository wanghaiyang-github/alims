package com.bazl.alims.dao;


import com.bazl.alims.model.po.LimsPersonInfo;
import com.bazl.alims.model.po.PersonDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LimsPersonInfoMapper {

    void insertPersonDetail(PersonDetail personDetail);

    void insertPersonInfo(LimsPersonInfo limsPersonInfo);

    PersonDetail selectPersonDetailById(String personDetailId);

    LimsPersonInfo selectPersonInfoById(String personId);

    void updatePersonInfo(LimsPersonInfo limsPersonInfo);

    List<LimsPersonInfo> selectByCaseId(String caseId);

    void deleteByCaseIdAndConsignmentId(LimsPersonInfo limsPersonInfo);

    List<LimsPersonInfo> selectByCaseIdAndConsignmentId(LimsPersonInfo limsPersonInfo);

    void deleteByPersonId(String personId);
}