package com.bazl.alims.controller;

import com.bazl.alims.common.Constants;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.LoaPermission;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.po.OrgInfo;
import com.bazl.alims.service.DictItemService;
import com.bazl.alims.service.LoaPermissionService;
import com.bazl.alims.service.OrgInfoService;
import com.bazl.alims.utils.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sun on 2018/12/19.
 */
@Controller
public class LoginController extends BaseController{

    @Autowired
    OrgInfoService orgInfoService;
    @Autowired
    LoaPermissionService permissionService;

    @Autowired
    DictItemService dictItemService;

    @RequestMapping("/login")
    public ModelAndView login(){
        List<OrgInfo> orgInfos = orgInfoService.selectAll();
        ModelAndView view = new ModelAndView();
        view.addObject("orgInfos", orgInfos);
        view.setViewName("login");
        return view;
    }

    @RequestMapping("/loginUser")
    public ModelAndView loginUser(String username, String password, HttpSession session, HttpServletRequest request) {
        List<OrgInfo> orgInfos = orgInfoService.selectAll();
        ModelAndView view = new ModelAndView();
        
        view.addObject("orgInfos", orgInfos);
        //授权认证
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(username,password);
        Subject subject = SecurityUtils.getSubject();
        try {
            String ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (username.equals("jgj")){
                if (ip.equals("14.80.189.39")){
//                if (ip.equals("14.60.76.50")){
                    subject.login(usernamePasswordToken);
                    //获得用户对象
                    LoaUserInfo user=(LoaUserInfo) subject.getPrincipal();
                    //存入session
                    session.setAttribute("user", user);
                    //字典项所有数据存入session
                    List<DictItem> listDictItem = dictItemService.selectAllCode();
                    session.setAttribute("listDictItem", listDictItem);
                    view.setViewName("redirect:/indexJsp");
                }else{
                    view.addObject("meg", "本机无权限登录！");
                    view.setViewName("login");
                    return view;
                }
            }else{
                subject.login(usernamePasswordToken);
                //获得用户对象
                LoaUserInfo user=(LoaUserInfo) subject.getPrincipal();
                //存入session
                session.setAttribute("user", user);
                //字典项所有数据存入session
                List<DictItem> listDictItem = dictItemService.selectAllCode();
                session.setAttribute("listDictItem", listDictItem);
                view.setViewName("redirect:/indexJsp");
            }
            return view;
        } catch (Exception e) {
            logger.error(e.getMessage());
            view.addObject("meg", "用户名或密码错误！");
            view.setViewName("login");
            return view;
        }
//        try {
            //完成登录
//        } catch(Exception e) {
//            logger.error(e.getMessage());
//            view.addObject("meg", "用户名或密码错误！");
//            view.setViewName("login");
//            return view;
//        }

    }

    @RequestMapping("/indexJsp")
    public ModelAndView indexJsp(){
        ModelAndView view=new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        //获得用户对象
        LoaUserInfo user=(LoaUserInfo) subject.getPrincipal();

        view.addObject("user", user);
        view.addObject("permissionList", getPermissionList());

        view.setViewName("index");
        return  view;
    }



    /**
     * 获取二级菜单列表
     * @return
     */
    private List<LoaPermission> getPermissionList(){
        List<LoaPermission> permissionList = permissionService.selectAll();
        List<LoaPermission> result = new ArrayList<>();
        for(LoaPermission permission : permissionList){
            if(permission.getRootFlag().equals(Constants.permission_root_flase)){
                List<LoaPermission> nodes = new ArrayList<>();
                for(LoaPermission permissionSec : permissionList){
                    if(StringUtils.isNotBlank(permissionSec.getParentId()) && permissionSec.getParentId().equals(permission.getPermissionId())){
                        nodes.add(permissionSec);
                    }
                }
                if(ListUtils.isNotNullAndEmptyList(nodes)){
                    permission.setPermissionList(nodes);
                }
                result.add(permission);
            }
        }
        return result;
    }

    @RequestMapping("/loginOut")
    public ModelAndView loginOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        List<OrgInfo> orgInfos = orgInfoService.selectAll();
        ModelAndView view = new ModelAndView();
        view.addObject("orgInfos", orgInfos);
        view.setViewName("login");
        return view;
    }

}
