package com.bazl.alims.dao;

import com.bazl.alims.model.po.LogRecordInfo;

import java.util.List;

public interface LogRecordMapper {
    int deleteByPrimaryKey(String logId);

    int insert(LogRecordInfo record);

    LogRecordInfo selectByPrimaryKey(String logId);

    List<LogRecordInfo> selectAll();

    int updateByPrimaryKey(LogRecordInfo record);
}