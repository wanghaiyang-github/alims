package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LoaPermissionMapper;
import com.bazl.alims.model.LoaPermission;
import com.bazl.alims.service.LoaPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sun on 2018/12/20.
 */
@Service
public class LoaPermissionServiceImpl implements LoaPermissionService {

    @Autowired
    LoaPermissionMapper loaPermissionMapper;

    @Override
    public int deleteByPrimaryKey(String permissionId) {
        return 0;
    }

    @Override
    public int insert(LoaPermission record) {
        return 0;
    }

    @Override
    public LoaPermission selectByPrimaryKey(String permissionId) {
        return null;
    }

    @Override
    public List<LoaPermission> selectAll() {
        return loaPermissionMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(LoaPermission record) {
        return 0;
    }

    @Override
    public List<LoaPermission> listByRoleId(String roleId) {
        return loaPermissionMapper.listByRoleId(roleId);
    }
}
