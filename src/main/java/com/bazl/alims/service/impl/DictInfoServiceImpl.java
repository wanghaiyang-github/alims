package com.bazl.alims.service.impl;

import com.bazl.alims.dao.DictInfoMapper;
import com.bazl.alims.dao.LoaRoleMapper;
import com.bazl.alims.service.DictInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class DictInfoServiceImpl implements DictInfoService {

    @Autowired
    DictInfoMapper dictInfoMapper;


}
