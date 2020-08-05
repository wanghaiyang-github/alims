package com.bazl.alims.dao;

import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.po.AmPersonalInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaUserInfoMapper {
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

    void forbiddenLoaUserInfo(@Param("personalId") String personalId, @Param("useractiveflase") String user_active_flase);

    void startusingLoaUserInfo(@Param("personalId")String personalId,  @Param("useractiveflase") String user_active_true);
}