package com.bazl.alims.service;

import com.bazl.alims.model.LoaRoleRelation;

import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */
public interface LoaRoleRelationService {
    void addLoaRoleRelation(LoaRoleRelation loaRoleRelation);

    void deleteloaRoleRelationByUserId(String userId);

    void updateloaRoleRelationByUserId(String roleId, String userId);
}
