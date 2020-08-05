package com.bazl.alims.service;

import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.po.AmPersonalInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaUserInfoService {
    /**
     * 根据用户名查询用户信息
     * @param loginName
     * @return
     */
    public LoaUserInfo selectByUserName(String loginName);

    void addLoaUserInfo(LoaUserInfo loaUserInfo);

    List<LoaUserInfo> queryloaUserInfoByPersonalId(String personalId);

    void deleteloaUserInfoByUserId(String userId);

    void updateloaUserInfoByUserId(AmPersonalInfo amPersonalInfo);

    void forbiddenLoaUserInfo(String personalId, String user_active_flase);

    void startusingLoaUserInfo(String personalId, String user_active_true);
}