package com.bazl.alims.dao;

import com.bazl.alims.model.LoaPermission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaPermissionMapper {
    int deleteByPrimaryKey(String permissionId);

    int insert(LoaPermission record);

    LoaPermission selectByPrimaryKey(String permissionId);

    List<LoaPermission> selectAll();

    int updateByPrimaryKey(LoaPermission record);

    /**
     *根据角色id获取权限
     * @param roleId
     * @return
     */
    public List<LoaPermission> listByRoleId(String roleId);
}