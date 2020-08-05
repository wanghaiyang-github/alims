package com.bazl.alims.service.impl;

import com.bazl.alims.dao.DelegateCenterConfigMapper;
import com.bazl.alims.model.po.DelegateCenterConfig;
import com.bazl.alims.service.DelegateCenterConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by LX on 2019/9/12.
 */
@Service
public class DelegateCenterConfigServiceImpl implements DelegateCenterConfigService {

    @Autowired
    DelegateCenterConfigMapper delegateCenterConfigMapper;

    @Override
    public int insert(DelegateCenterConfig record) {
        return delegateCenterConfigMapper.insert(record);
    }

    @Override
    public List<DelegateCenterConfig> selectAll() {
        return delegateCenterConfigMapper.selectAll();
    }

    @Override
    public List<DelegateCenterConfig> selectQualification(String delegateOrgCodePrefix) {
        return delegateCenterConfigMapper.selectQualification(delegateOrgCodePrefix);
    }
}
