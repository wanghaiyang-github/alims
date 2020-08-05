package com.bazl.alims.controller.delegate;

import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by Sun on 2018/12/20.
 */
@Controller
@RequestMapping("/stateQuery")
public class StateQueryController {

    @Resource
    SolrClient client;

    /**
     * 鉴定状态查询
     * @return
     */
    @RequestMapping("/queryList")
    public ModelAndView queryList (){
        ModelAndView view = new ModelAndView();
        view.setViewName("stateQuery/queryList");
        return view;
    }

    /**
     * 鉴定状态详情查询
     * @return
     */
    @RequestMapping("/query")
    public ModelAndView query (String id, String text){
        ModelAndView view = new ModelAndView();
        view.setViewName("stateQuery/query");
        view.addObject("text", text);
        //创建查询语句
        SolrQuery query = new SolrQuery();
        //设置查询条件
        if(StringUtils.isNotBlank(id)){
            query.setQuery("id:"+id);    //设置查询关键字
        }
        query.setFields("id, case_name, case_no, case_brief, delegate_datetime, identify_requirement, person");//显示的字段 fl
        //执行查询(需传入code名)
        QueryResponse queryResponse = null;
        try {
            queryResponse = client.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文档列表
        SolrDocumentList documentList = queryResponse.getResults();
//        for (SolrDocument solrDocument : documentList) {
//            //取各个文档信息
//            System.out.println(solrDocument);
//        }
        view.addObject("document", documentList.get(0));
        return view;
    }

    /**
     * 鉴定状态查询
     * @return
     */
    @RequestMapping("/search")
    public ModelAndView search (String text, PageInfo pageInfo){

        ModelAndView view = new ModelAndView();
        view.addObject("text", text);
        view.setViewName("stateQuery/queryList");

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            view.addObject("date", "redirect:/login.html?timeoutFlag=1");
            return  view;
        }

        //创建查询语句
        SolrQuery query = new SolrQuery();
        //设置查询条件
        if(StringUtils.isNotBlank(text)){
            query.setQuery("case_name:"+"*"+text+"*"+" or "+"case_name:"+text
                    +" or "+ "case_no:"+"*"+text+"*"+" or "+"case_no:"+text
                    +" or "+ "identify_requirement:"+"*"+text+"*"+" or "+"identify_requirement:"+text
                    +" or "+"person:"+"*"+text+"*"+" or "+"person:"+text);    //设置查询关键字
            query.addFilterQuery("accept_org_id:"+operateUser.getOrgId());
        }else{
            query.setQuery("accept_org_id:"+operateUser.getOrgId());    //设置查询关键字
        }
        query.setFields("id, case_name, case_no, case_brief, delegate_datetime, identify_requirement, person");//显示的字段 fl

        query.setHighlight(true);
        query.addHighlightField("case_name");
        query.setHighlightSimplePre("<font style='color:red'>");
        query.setHighlightSimplePost("</font>");

        int pageNo = pageInfo.getPage();
        int pageSize = pageInfo.getEvePageRecordCnt();
        query.setStart((pageNo - 1) * pageSize); //offset
        query.setRows(5); //limit
        //执行查询(需传入code名)
        QueryResponse queryResponse = null;
        try {
            queryResponse = client.query(query);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取文档列表
       SolrDocumentList documentList = queryResponse.getResults();
        //获取总记录数
        long numFound = documentList.getNumFound();
//        System.out.println("总记录数:" + numFound);

        //k是id,内部的map的key是域名,其value是高亮的值集合
//        NamedList list = (NamedList) queryResponse.getResponse().get("highlighting");
//        System.out.println(list);//用于显示list中的值
//        for (SolrDocument solrDocument : documentList) {
//            //取各个文档信息
//            System.out.println(solrDocument);
//        }

        view.addObject("documentList", documentList);
        view.addObject("pageInfo", pageInfo.updatePageInfo(numFound));
        return view;
    }

}
