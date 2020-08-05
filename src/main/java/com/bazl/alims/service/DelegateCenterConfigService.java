package com.bazl.alims.service;

import com.bazl.alims.model.po.DelegateCenterConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by LX on 2019/9/12.
 */
@Repository
public interface DelegateCenterConfigService {

    int insert(DelegateCenterConfig record);

    List<DelegateCenterConfig> selectAll();

    //查询鉴定机构
    List<DelegateCenterConfig> selectQualification(String delegateOrgCodePrefix);
}
