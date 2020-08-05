package com.bazl.alims.controller.delegate;

import com.bazl.alims.controller.BaseController;
import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.po.LimsConsignmentInfo;
import com.bazl.alims.model.po.OrgInfo;
import com.bazl.alims.service.LimsCaseInfoService;
import com.bazl.alims.service.LimsConsignmentInfoService;
import com.bazl.alims.service.OrgInfoService;
import com.bazl.alims.utils.DateUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sun on 2019/1/3.
 */
@Controller
@RequestMapping("/DocDownload")
public class DocDownloadController extends BaseController {

    @Autowired
    LimsCaseInfoService caseInfoService;
    @Autowired
    LimsConsignmentInfoService limsConsignatioInfoService;
    @Autowired
    OrgInfoService orgInfoService;

    @RequestMapping("/delegateDoc")
    public void delegateDoc(HttpServletRequest request, HttpServletResponse response,
                                    String consignmentId) {

        LimsConsignmentInfo consignmentInfo = limsConsignatioInfoService.selectByConsignmentId(consignmentId);
        LimsCaseInfo caseInfo = caseInfoService.selectByCaseId(consignmentInfo.getCaseId());

        Map<String, Object> params = new HashMap<String, Object>();
        String consignmentNo = consignmentInfo == null ? "" : consignmentInfo.getConsignmentNo();

        OrgInfo orgInfo  = orgInfoService.selectByPrimaryKey(consignmentInfo.getAcceptOrgId());

        if(StringUtils.isBlank(consignmentNo) || consignmentNo.length() < 4){
            params.put("yearNo", "");
            params.put("serialNo", "");
        }else {
            params.put("yearNo", consignmentNo.substring(0, 4));
            params.put("serialNo", consignmentNo.substring(4));
        }

        params.put("caseInfo", caseInfo);
        params.put("year", DateUtils.getCurrentYearStr());
        params.put("month", DateUtils.getCurrentMonthStr());
        params.put("day", DateUtils.getCurrentDayStr());
        params.put("orgInfo", orgInfo);

        try {
            Configuration cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            //获取模板
            Template tmp = cfg.getTemplate("鉴定委托书.ftl", "UTF-8");

            String filename = "鉴定委托书"+ DateUtils.dateToString(new Date(),"yyyyMMddHHmmss") +".doc";

            response.setCharacterEncoding("UTF-8");
            //判断委托编号是否为空
            if(StringUtils.isEmpty(consignmentNo)){
                response.setHeader("Content-disposition", "attachment;filename=" +new String(filename.getBytes("GBK"),"ISO-8859-1"));//文件头，导出的文件名
            }else {
                response.setHeader("Content-disposition", "attachment;filename="+ consignmentNo + new String(filename.getBytes("GBK"),"ISO-8859-1"));//文件头，导出的文件名
            }
            response.setContentType("application/x-msdownload");//类型

            tmp.process(params, response.getWriter());

        } catch (Exception ex) {
            logger.error("", ex);
        }
    }

    @RequestMapping("/employDoc")
    public void employDoc(HttpServletRequest request, HttpServletResponse response,
                                    String consignmentId) {

        LimsConsignmentInfo consignmentInfo = limsConsignatioInfoService.selectByConsignmentId(consignmentId);
        LimsCaseInfo caseInfo = caseInfoService.selectByCaseId(consignmentInfo.getCaseId());

        Map<String, Object> params = new HashMap<String, Object>();
        String consignmentNo = consignmentInfo == null ? "" : consignmentInfo.getConsignmentNo();

        OrgInfo orgInfo  = orgInfoService.selectByPrimaryKey(consignmentInfo.getAcceptOrgId());

        if(orgInfo != null){
            if(StringUtils.isNotBlank(orgInfo.getOrgName())){
                if("八支队".equals(orgInfo.getOrgName())){
                    params.put("orgNameFirst"," ");
                }else{
                    params.put("orgNameFirst", orgInfo.getOrgName().substring(0,1));
                }

            }
        }


        if(StringUtils.isBlank(consignmentNo) || consignmentNo.length() < 4){
            params.put("yearNo", "");
            params.put("serialNo", "");
        }else {
            params.put("yearNo", consignmentNo.substring(0, 4));
            params.put("serialNo", consignmentNo.substring(4));
        }

        params.put("caseInfo", caseInfo);
        params.put("year", DateUtils.getCurrentYearStr());
        params.put("month", DateUtils.getCurrentMonthStr());
        params.put("day", DateUtils.getCurrentDayStr());
        params.put("orgInfo", orgInfo);

        try {
            Configuration cfg = new Configuration();
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(this.getClass(), "/templates");
            //获取模板
            Template tmp = cfg.getTemplate("鉴定聘请书.ftl", "UTF-8");

            String filename = "鉴定聘请书"+ DateUtils.dateToString(new Date(),"yyyyMMddHHmmss") +".doc";

            response.setCharacterEncoding("UTF-8");
            //判断委托编号是否为空
            if(StringUtils.isEmpty(consignmentNo)){
                response.setHeader("Content-disposition", "attachment;filename=" +new String(filename.getBytes("GBK"),"ISO-8859-1"));//文件头，导出的文件名
            }else {
                response.setHeader("Content-disposition", "attachment;filename="+ consignmentNo + new String(filename.getBytes("GBK"),"ISO-8859-1"));//文件头，导出的文件名
            }
            response.setContentType("application/x-msdownload");//类型

            tmp.process(params, response.getWriter());
        } catch (Exception ex) {
            logger.error("", ex);
        }
    }
}
