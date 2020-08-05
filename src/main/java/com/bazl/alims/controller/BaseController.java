package com.bazl.alims.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/30.
 */
public abstract class BaseController {
    protected static final String MESSAGE = "message";
    protected static final String STATUS = "status";
    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final static String PARAMS_OPERATE_TYPE = "operateType";

}
