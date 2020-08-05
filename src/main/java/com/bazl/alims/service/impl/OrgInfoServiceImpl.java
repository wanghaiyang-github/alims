package com.bazl.alims.service.impl;

import com.bazl.alims.dao.OrgInfoMapper;
import com.bazl.alims.model.po.OrgInfo;
import com.bazl.alims.service.OrgInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sun on 2018/12/20.
 */
@Service
public class OrgInfoServiceImpl implements OrgInfoService {

    @Autowired
    OrgInfoMapper orgInfoMapper;

    @Override
    public int insert(OrgInfo record) {
        return orgInfoMapper.insert(record);
    }

    @Override
    public List<OrgInfo> selectAll() {
        return orgInfoMapper.selectAll();
    }

    @Override
    public OrgInfo selectByPrimaryKey(String orgId) {
        return orgInfoMapper.selectByPrimaryKey(orgId);
    }

    @Override
    public List<OrgInfo> selectDelegateByParentsId(String parentsId) {
        return orgInfoMapper.selectDelegateByParentsId(parentsId);
    }

}
