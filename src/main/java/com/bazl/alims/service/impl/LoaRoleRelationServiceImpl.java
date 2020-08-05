package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LoaRoleRelationMapper;
import com.bazl.alims.model.LoaRoleRelation;
import com.bazl.alims.service.LoaRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */
@Service
public class LoaRoleRelationServiceImpl implements LoaRoleRelationService{
    @Autowired
    private LoaRoleRelationMapper loaRoleRelationMapper;

    @Override
    public void addLoaRoleRelation(LoaRoleRelation loaRoleRelation) {
        loaRoleRelationMapper.addLoaRoleRelation(loaRoleRelation);
    }

    @Override
    public void deleteloaRoleRelationByUserId(String userId) {
        loaRoleRelationMapper.deleteloaRoleRelationByUserId(userId);
    }

    @Override
    public void updateloaRoleRelationByUserId(String roleId, String userId) {
        loaRoleRelationMapper.updateloaRoleRelationByUserId(roleId,userId);
    }
}
