package com.bazl.alims.service.impl;

import com.bazl.alims.dao.QueueSampleMapper;
import com.bazl.alims.model.po.QueueSample;
import com.bazl.alims.service.QueueSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class QueueSampleServiceImpl implements QueueSampleService {

    @Autowired
    QueueSampleMapper queueSampleMapper;

    @Transactional
    public void QueueSampleService(QueueSample queueSample) {
        queueSampleMapper.insertQueueSample(queueSample);

    }











}
