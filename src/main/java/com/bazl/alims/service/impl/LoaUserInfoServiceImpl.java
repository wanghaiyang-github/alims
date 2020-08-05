package com.bazl.alims.service.impl;

import com.bazl.alims.dao.LoaUserInfoMapper;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.po.AmPersonalInfo;
import com.bazl.alims.service.LoaUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Sun on 2018/12/20.
 */
@Service
public class LoaUserInfoServiceImpl implements LoaUserInfoService {

    @Autowired
    LoaUserInfoMapper loaUserInfoMapper;

    @Override
    public LoaUserInfo selectByUserName(String loginName) {
        return loaUserInfoMapper.selectByUserName(loginName);
    }

    @Override
    public void addLoaUserInfo(LoaUserInfo loaUserInfo) {
        loaUserInfoMapper.addLoaUserInfo(loaUserInfo);
    }

    @Override
    public List<LoaUserInfo> queryloaUserInfoByPersonalId(String personalId) {
        return loaUserInfoMapper.queryloaUserInfoByPersonalId(personalId);
    }

    @Override
    public void deleteloaUserInfoByUserId(String userId) {
        loaUserInfoMapper.deleteloaUserInfoByUserId(userId);
    }

    @Override
    public void updateloaUserInfoByUserId(AmPersonalInfo amPersonalInfo) {
        loaUserInfoMapper.updateloaUserInfoByUserId(amPersonalInfo);
    }

    @Override
    public void forbiddenLoaUserInfo(String personalId, String user_active_flase) {
        loaUserInfoMapper.forbiddenLoaUserInfo(personalId,user_active_flase);
    }

    @Override
    public void startusingLoaUserInfo(String personalId, String user_active_true) {
        loaUserInfoMapper.startusingLoaUserInfo(personalId,user_active_true);
    }


}
