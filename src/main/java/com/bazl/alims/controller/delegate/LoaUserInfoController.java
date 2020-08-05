package com.bazl.alims.controller.delegate;

import com.bazl.alims.common.Constants;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.service.LoaUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/12/24.
 */
@Controller
@RequestMapping("/LoaUserInfoController")
public class LoaUserInfoController {
    @Autowired
    private LoaUserInfoService loaUserInfoService;
    /**
     * 禁用登录用户
     * @param loaUserInfo
     * @return
     */
    @RequestMapping("forbiddenLoaUserInfo")
    public String forbiddenLoaUserInfo(LoaUserInfo loaUserInfo){
        //根据PersonalId修改LOA_USER_INFO中的ActiveFlag状态为"1"
        loaUserInfoService.forbiddenLoaUserInfo(loaUserInfo.getPersonalId(), Constants.user_active_flase);
        return "redirect:/manage/delegatorManage";
    }
    /**
     * 启用登录用户
     * @param loaUserInfo
     * @return
     */
    @RequestMapping("startusingLoaUserInfo")
    public String startusingLoaUserInfo(LoaUserInfo loaUserInfo){
        //根据PersonalId修改LOA_USER_INFO中的ActiveFlag状态为"0"
        loaUserInfoService.startusingLoaUserInfo(loaUserInfo.getPersonalId(), Constants.user_active_true);
        return "redirect:/manage/delegatorManage";
    }
}
