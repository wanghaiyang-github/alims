package com.bazl.alims.dao;

import com.bazl.alims.model.po.SerialNumber;
import org.apache.ibatis.annotations.Param;

public interface SerialNumberMapper {
    int insert(SerialNumber record);

    SerialNumber selectNextVal(@Param("typeCode")String typeCode, @Param("orgId")String orgId);

    int updateYear(@Param("year") String year, @Param("typeCode")String typeCode, @Param("orgId")String orgId);
    int update(SerialNumber record);
}