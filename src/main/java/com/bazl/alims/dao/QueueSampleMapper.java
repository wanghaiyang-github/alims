package com.bazl.alims.dao;


import com.bazl.alims.model.po.QueueSample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * Created by Administrator on 2018/12/21.
 */
@Repository
public interface  QueueSampleMapper {

    void insertQueueSample(QueueSample queueSample);

}
