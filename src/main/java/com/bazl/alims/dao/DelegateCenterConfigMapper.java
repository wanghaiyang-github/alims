package com.bazl.alims.dao;

import com.bazl.alims.model.po.DelegateCenterConfig;
import java.util.List;

public interface DelegateCenterConfigMapper {
    int insert(DelegateCenterConfig record);

    List<DelegateCenterConfig> selectAll();

    //查询鉴定机构
    List<DelegateCenterConfig> selectQualification(String delegateOrgCodePrefix);
}