package com.bazl.alims.dao;

import com.bazl.alims.model.LoaRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaRoleMapper {
    /**
     * 根据用户id查询所有角色
     * @param userId
     * @return
     */
    public List<LoaRole> listRolesByUserId(String userId);

    List<LoaRole> queryLoaRole();
}