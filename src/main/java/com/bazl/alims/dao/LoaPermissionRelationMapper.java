package com.bazl.alims.dao;

import com.bazl.alims.model.LoaPermissionRelation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaPermissionRelationMapper {
    int deleteByPrimaryKey(String id);

    int insert(LoaPermissionRelation record);

    LoaPermissionRelation selectByPrimaryKey(String id);

    List<LoaPermissionRelation> selectAll();

    int updateByPrimaryKey(LoaPermissionRelation record);
}