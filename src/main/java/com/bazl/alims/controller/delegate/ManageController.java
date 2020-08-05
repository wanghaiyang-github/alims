package com.bazl.alims.controller.delegate;

import com.bazl.alims.common.Constants;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.LoaRole;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.po.AmPersonalInfo;
import com.bazl.alims.model.vo.AmPersonalInfoVo;
import com.bazl.alims.service.AmPersonalInfoService;
import com.bazl.alims.service.DictItemService;
import com.bazl.alims.service.LoaRoleService;
import com.bazl.alims.service.LoaUserInfoService;
import com.bazl.alims.utils.DictUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Sun on 2018/12/20.
 */
@Controller
@RequestMapping("/manage")
public class ManageController {
    @Autowired
    private LoaRoleService loaRoleService;
    @Autowired
    AmPersonalInfoService amPersonalInfoService;
    @Autowired
    LoaUserInfoService loaUserInfoService;
    @Autowired
    private DictItemService dictItemService;

    /**
     * 委托人管理查询
     * @return
     */
    @RequestMapping("/delegatorManage")
    public ModelAndView query (HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("manage/delegatorManage");

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            view.addObject("date", "redirect:/login.html?timeoutFlag=1");
            return  view;
        }

        /**
         *查询角色信息表Loa_Role
         * 回显委托人添加权限设定
         */
        List<LoaRole>LoaRoleList = loaRoleService.queryLoaRole();
        view.addObject("LoaRoleList",LoaRoleList);
        /**
         *查询字典信息表DICT_ITEM
         * 回显委托人添加职务
         */
        DictItem dictItem = new DictItem();
        dictItem.setDictTypeCode(Constants.POSITION_LIST);
        List<DictItem> listDictItem = DictUtil.getDictList(dictItem);
        view.addObject("listDictItem",listDictItem);
        /**
         * 查询Am_Personal_Info ap , Loa_User_Info lu  ,LOA_ROLE_RELATION lrr , Loa_Role lr
         * 返回到委托管理展示页面
         */
        List<AmPersonalInfoVo> amPersonalInfoVoList=amPersonalInfoService.queryAmPersonalInfoVoList(operateUser.getOrgId());
        view.addObject("amPersonalInfoVoList",amPersonalInfoVoList);
        return view;
    }

    /**
     * 个人信息管理
     * @return
     */
    @RequestMapping("/personalInformation")
    public ModelAndView personalInformation (HttpSession session){
        LoaUserInfo user = (LoaUserInfo) session.getAttribute("user");
        ModelAndView view = new ModelAndView();
        //根据用户主键ID查询回显到委托人个人中心
        AmPersonalInfo amPersonalInfo =amPersonalInfoService.queryAmPersonalInfo(user.getPersonalId());
        /**
         *查询字典信息表DICT_ITEM
         * 回显个人中心职务
         */
        DictItem dictItem = new DictItem();
        dictItem.setDictTypeCode(Constants.POSITION_LIST);
        List<DictItem> listDictItem = DictUtil.getDictList(dictItem);
        view.addObject("listDictItem",listDictItem);
        view.addObject("amObject",amPersonalInfo);
        view.setViewName("manage/personalInformation");
        return view;
    }
    /**
     * 个人信息管理
     * @return
     */
    @RequestMapping("/updateAmPersonalInfoAndLoaUserInfo")
    public String updatepersonalInformation (AmPersonalInfo amPersonalInfo){
        //根据用户主键ID查询修改委托人个人中心信息
        amPersonalInfoService.updateAmPersonalInfoById(amPersonalInfo);
        loaUserInfoService.updateloaUserInfoByUserId(amPersonalInfo);
        return "redirect:/manage/personalInformation";
    }
}
