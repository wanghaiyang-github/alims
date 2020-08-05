package com.bazl.alims.service;

import com.bazl.alims.model.po.OrgInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sun on 2018/12/20.
 */
@Repository
public interface OrgInfoService {
    int insert(OrgInfo record);

    List<OrgInfo> selectAll();

    OrgInfo selectByPrimaryKey(String orgId);

    List<OrgInfo> selectDelegateByParentsId(String parentsId);
}
