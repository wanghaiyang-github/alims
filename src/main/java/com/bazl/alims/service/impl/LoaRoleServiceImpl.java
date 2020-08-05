package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LoaRoleMapper;
import com.bazl.alims.model.LoaRole;
import com.bazl.alims.service.LoaRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sun on 2018/12/20.
 */
@Service
public class LoaRoleServiceImpl implements LoaRoleService{

    @Autowired
    LoaRoleMapper loaRoleMapper;

    @Override
    public List<LoaRole> listRolesByUserId(String userId) {
        return loaRoleMapper.listRolesByUserId(userId);
    }

    @Override
    public List<LoaRole> queryLoaRole() {
        return loaRoleMapper.queryLoaRole();
    }
}
