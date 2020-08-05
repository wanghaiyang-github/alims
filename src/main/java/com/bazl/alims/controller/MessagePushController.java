package com.bazl.alims.controller;

import com.bazl.alims.common.Constants;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.service.LimsCaseInfoService;
import com.bazl.alims.utils.DateUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sun on 2018/12/20.
 */
@Controller
@RequestMapping("/message")
public class MessagePushController extends BaseController{

    @Autowired
    LimsCaseInfoService caseInfoService;

    /**
     * 根据年份查询统计数据
     * @param year
     * @return
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public Map<String,Object> getCount(String year){
        Map<String,Object> map = new HashMap<>();

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            map.put("date", "redirect:/login.html?timeoutFlag=1");
            return  map;
        }

        try {
            //根据年份获取各个月份的案件数

            HashMap<String, String> monthMap = caseInfoService.selectMonthCountByYear(year, operateUser.getOrgId());
            map.put("monthMap", monthMap);
            map.put("code", 0);
            map.put("message", "获取成功！");
        }catch(Exception ex){
            logger.info("获取失败"+ex);
            map.put("code", 1);
            map.put("message", "获取失败！");
        }
        return map;
    }

}
