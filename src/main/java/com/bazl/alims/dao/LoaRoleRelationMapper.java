package com.bazl.alims.dao;

import com.bazl.alims.model.LoaRoleRelation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaRoleRelationMapper {

    void addLoaRoleRelation(LoaRoleRelation loaRoleRelation);

    void deleteloaRoleRelationByUserId(String userId);

    void updateloaRoleRelationByUserId(@Param("roleId") String roleId, @Param("userId")String userId);
}