package com.bazl.alims.controller.delegate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bazl.alims.common.Constants;
import com.bazl.alims.model.LoaRoleRelation;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.po.AmPersonalInfo;
import com.bazl.alims.service.AmPersonalInfoService;
import com.bazl.alims.service.LoaRoleRelationService;
import com.bazl.alims.service.LoaUserInfoService;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by Administrator on 2018/12/21.
 */
@Controller
@RequestMapping("/AmPersonalInfoController")
public class AmPersonalInfoController {
    @Autowired
    private AmPersonalInfoService amPersonalInfoService;
    @Autowired
    private LoaUserInfoService loaUserInfoService;
    @Autowired
    private LoaRoleRelationService loaRoleRelationService;
    /**
     * 委托人管理，添加或修改委托人信息
     * @param amPersonalInfo
     * @return
     */
    @RequestMapping("addAndUpdateAmPersonalInfo")
    public String addAndUpdateAmPersonalInfo(AmPersonalInfo amPersonalInfo){

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();

        if (Strings.isEmpty(amPersonalInfo.getPersonalId())){
            //添加人员信息表AM_PERSONAL_INFO
            String id = UUID.randomUUID().toString();
            amPersonalInfo.setPersonalId(id);
            amPersonalInfoService.addAmPersonalInfo(amPersonalInfo);
            //添加关联 用户信息表 LOA_USER_INFO
            String userid = UUID.randomUUID().toString();
            LoaUserInfo loaUserInfo = new LoaUserInfo();
            loaUserInfo.setActiveFlag(Constants.user_active_true);
            loaUserInfo.setUserId(userid);
            loaUserInfo.setPersonalId(id);
//            loaUserInfo.setLoginName(amPersonalInfo.getLoginName());
//            loaUserInfo.setLoginPassword(amPersonalInfo.getLoginPassword());
            loaUserInfo.setOrgId(operateUser.getOrgId());
            loaUserInfo.setUserType(Constants.USER_TYPE_5);//用户类型：委托人
            loaUserInfoService.addLoaUserInfo(loaUserInfo);
            //添加关联 用户权限关系表 LOA_ROLE_RELATION
            LoaRoleRelation loaRoleRelation = new LoaRoleRelation();
            String ids = UUID.randomUUID().toString();
            loaRoleRelation.setId(ids);
            loaRoleRelation.setRoleId(amPersonalInfo.getRoleId());
            loaRoleRelation.setUserId(userid);
            loaRoleRelationService.addLoaRoleRelation(loaRoleRelation);
        }else {
            //根据PersonalId查询LOA_USER_INFO用户信息表获取到UserID对用户权限关系表LOA_ROLE_RELATION表中的信息进行修改
            List<LoaUserInfo> list =loaUserInfoService.queryloaUserInfoByPersonalId(amPersonalInfo.getPersonalId());
            loaRoleRelationService.updateloaRoleRelationByUserId(amPersonalInfo.getRoleId(),list.get(0).getUserId());
            //修改LOA_USER_INFO用户信息表信息
            loaUserInfoService.updateloaUserInfoByUserId(amPersonalInfo);
            //修改LOA_ROLE_RELATION人员信息表信息
            amPersonalInfoService.updateAmPersonalInfo(amPersonalInfo);
        }
        return "redirect:/manage/delegatorManage";
    };

    /**
     * 委托人管理，委托人删除
     * @param amPersonalInfoF
     * @return
     */
    @RequestMapping("deleteAmPersonalInfo")
    public String deleteAmPersonalInfo(AmPersonalInfo amPersonalInfo){
        if (!Objects.isNull(amPersonalInfo)){
            //根据PersonalId查询LOA_USER_INFO用户信息表获取到UserID对用户权限关系表LOA_ROLE_RELATION表中的信息进行删除
            List<LoaUserInfo> list =loaUserInfoService.queryloaUserInfoByPersonalId(amPersonalInfo.getPersonalId());
            loaRoleRelationService.deleteloaRoleRelationByUserId(list.get(0).getUserId());
            //删除LOA_USER_INFO用户信息表信息
            loaUserInfoService.deleteloaUserInfoByUserId(list.get(0).getUserId());
            //删除LOA_ROLE_RELATION人员信息表信息
            amPersonalInfoService.deleteAmPersonalInfo(amPersonalInfo);
        }
        return "redirect:/manage/delegatorManage";
    };

    /**
     * 委托人管理，添加或修改委托人信息
     * @param params
     * @return
     */
    @RequestMapping(value="/updateRole", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> updateRole(@RequestParam(value = "params")String params){
        Map<String,Object> map=new HashMap();

        JSONObject str = JSON.parseObject(params);
        List<AmPersonalInfo> amPersonalInfoList = new ArrayList<>();
        if(str.containsKey("amPersonalInfoList")) {
            amPersonalInfoList = (List<AmPersonalInfo>) JSON.parseArray(str.get("amPersonalInfoList").toString(), AmPersonalInfo.class);
        }
        for(AmPersonalInfo amPersonalInfo : amPersonalInfoList){
            //根据PersonalId查询LOA_USER_INFO用户信息表获取到UserID对用户权限关系表LOA_ROLE_RELATION表中的信息进行修改
            List<LoaUserInfo> list =loaUserInfoService.queryloaUserInfoByPersonalId(amPersonalInfo.getPersonalId());
            loaRoleRelationService.deleteloaRoleRelationByUserId(list.get(0).getUserId());
            //添加关联 用户权限关系表 LOA_ROLE_RELATION
            LoaRoleRelation loaRoleRelation = new LoaRoleRelation();
            String ids = UUID.randomUUID().toString();
            loaRoleRelation.setId(ids);
            loaRoleRelation.setRoleId(amPersonalInfo.getRoleId());
            loaRoleRelation.setUserId(list.get(0).getUserId());
            loaRoleRelationService.addLoaRoleRelation(loaRoleRelation);
        }
        map.put("success",true);
        return map;
    };
}
