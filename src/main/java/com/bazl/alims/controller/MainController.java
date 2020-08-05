package com.bazl.alims.controller;

import com.bazl.alims.common.Constants;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.service.LimsCaseInfoService;
import com.bazl.alims.utils.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sun on 2018/12/20.
 */
@Controller
@RequestMapping("/main")
public class MainController extends BaseController {

    @Autowired
    LimsCaseInfoService caseInfoService;

    @RequestMapping("/home")
    public ModelAndView home() {
        ModelAndView view = new ModelAndView();
        view.setViewName("home");

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if (operateUser == null) {
            view.addObject("date", "redirect:/login.html?timeoutFlag=1");
            return view;
        }

        //查询待送检状态案件数
        int cnt1 = caseInfoService.selectCountByCaseStatus(Constants.STATUS_01, operateUser.getOrgId());
        view.addObject("cnt1", cnt1);
        //查询待领取状态案件数
        int cnt3 = caseInfoService.selectCountByCaseStatus(Constants.CASE_STATUS_03, operateUser.getOrgId());
        view.addObject("cnt3", cnt3);

        //根据年份获取各个月份的案件数
        HashMap<String, String> monthMap = caseInfoService.selectMonthCountByYear(DateUtils.getCurrentYear(), operateUser.getOrgId());
        view.addObject("monthMap", monthMap);

        return view;
    }

    /**
     * 根据年份查询统计数据
     *
     * @param year
     * @return
     */
    @RequestMapping("/getCount")
    @ResponseBody
    public Map<String, Object> getCount(String year) {
        Map<String, Object> map = new HashMap<>();

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if (operateUser == null) {
            map.put("date", "redirect:/login.html?timeoutFlag=1");
            return map;
        }

        try {
            //根据年份获取各个月份的案件数
            HashMap<String, String> monthMap = caseInfoService.selectMonthCountByYear(year, operateUser.getOrgId());
            map.put("monthMap", monthMap);
            map.put("code", 0);
            map.put("message", "获取成功！");
        } catch (Exception ex) {
            logger.info("获取失败" + ex);
            map.put("code", 1);
            map.put("message", "获取失败！");
        }
        return map;
    }

    @RequestMapping("/receiveWord")
    public void circulationRecord(HttpServletRequest request, HttpServletResponse response,
                                  String caseNo, String caseName, String orgName, String delegatorName, String delegatorNo) throws ParseException {

        Map<String, Object> params = new HashMap<String, Object>();
        /*if (caseNo == null || caseNo == "") {
            params.put("year", "");
            params.put("no", "");
        } else {
            if (caseNo.contains("-")) {
                int length = caseNo.split("-").length;
                if (length > 2) {
                    params.put("year", (caseNo.split("-")[1]));
                    params.put("no", (caseNo.split("-")[2]));
                } else {
                    params.put("year", (caseNo.split("-")[0]));
                    params.put("no", (caseNo.split("-")[1]));
                }
            } else {
                params.put("year", caseNo.substring(0, 4));
                params.put("no", caseNo.substring(4, caseNo.length()));
            }
        }*/

        params.put("caseNo", StringUtils.isBlank(caseNo) ? "" : caseNo);
        params.put("caseName", StringUtils.isBlank(caseName) ? "" : caseName);
        params.put("orgName", StringUtils.isBlank(orgName) ? "" : orgName);
        params.put("delegatorName", StringUtils.isBlank(delegatorName) ? "" : delegatorName);
        params.put("delegatorNo", StringUtils.isBlank(delegatorNo) ? "" : delegatorNo);


        try {
            Configuration cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            //获取模板
            Template tmp = cfg.getTemplate("鉴定书领取单.ftl", "UTF-8");

            String filename = "-鉴定书领取单" + DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + ".doc";

            response.setCharacterEncoding("UTF-8");
            //文件头，导出的文件名
            response.setHeader("Content-disposition", "attachment;filename=" + caseNo + new String(filename.getBytes("GBK"), "ISO-8859-1"));
            //类型
            response.setContentType("application/x-msdownload");
            tmp.process(params, response.getWriter());
        } catch (Exception ex) {
            logger.error("下载错误", ex);
        } finally {
            try {
                response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
