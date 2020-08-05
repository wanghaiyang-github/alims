package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LimsPerosnRelationMapper;
import com.bazl.alims.service.LimsPerosnRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class LimsPerosnRelationServiceImpl implements LimsPerosnRelationService {

    @Autowired
    LimsPerosnRelationMapper limsPerosnRelationMapper;


}
