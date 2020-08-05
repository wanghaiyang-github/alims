package com.bazl.alims.service;

import com.bazl.alims.model.LoaRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaRoleService {

    /**
     * 根据用户id查询所有角色
     * @param userId
     * @return
     */
    public List<LoaRole> listRolesByUserId(String userId);
    /**
     *查询角色信息表Loa_Role
     * 回显委托人添加权限设定
     */
    List<LoaRole> queryLoaRole();
}