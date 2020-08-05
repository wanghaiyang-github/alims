package com.bazl.alims.controller.delegate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bazl.alims.HttpClient.GetXkCaseService;
import com.bazl.alims.common.Constants;
import com.bazl.alims.controller.BaseController;
import com.bazl.alims.dao.LimsConsignmentInfoMapper;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.PageInfo;
import com.bazl.alims.model.bo.DelegateDataModel;
import com.bazl.alims.model.po.*;
import com.bazl.alims.model.vo.LimsCaseInfoVo;
import com.bazl.alims.service.*;
import com.bazl.alims.utils.DictUtil;
import com.bazl.alims.utils.ListUtils;
import com.bazl.alims.webservices.XckyWebService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Sun on 2018/12/20.
 */
@Controller
@RequestMapping("/caseQuery")
public class CaseQueryController extends BaseController{

    @Autowired
    LimsConsignmentInfoService limsConsignmentInfoService;

    @Autowired
    LimsCaseInfoService limsCaseInfoService;

    @Autowired
    PersonDetailService personDetailService;

    @Autowired
    LimsPersonInfoService limsPersonInfoService;

    @Autowired
    LimsSampleInfoDnaService limsSampleInfoDnaService;

    @Autowired
    LimsPerosnRelationService limsPerosnRelationService;

    @Autowired
    AmPersonalInfoService amPersonalInfoService;

    @Autowired
    OrgInfoService orgInfoService;

    @Autowired
    GetXkCaseService getXkCaseService;

    @Autowired
    XckyAddressInfoService xckyAddressInfoService;
    @Autowired
    XckyWebService xckyWebService;

    @Autowired
    LimsConsignmentInfoMapper limsConsignatioInfoMapper;
    @Autowired
    DelegateCenterConfigService delegateCenterConfigService;
    @Autowired
    FugitivesInfoService fugitivesInfoService;

//    @Autowired
//    MobileNewsMapper mobileNewsMapper;

//    @Autowired
//    LogRecordMapper logRecordMapper;

//    @Value("${Is_App_Inform}")
//    private int isAppInform;
//    @Value("${Is_App_Url}")
//    private String AppUrl;

    /**
     * 查询与补送
     * @return
     */
    @RequestMapping("/caseAndBring")
    public ModelAndView caseAndBring (HttpServletRequest request, LimsCaseInfoVo query, PageInfo pageInfo){
        ModelAndView view = new ModelAndView();

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            view.addObject("date", "redirect:/login.html?timeoutFlag=1");
            return  view;
        }

        try {

            String orgId = StringUtils.isBlank(operateUser.getSubOrgId()) ? operateUser.getOrgId() : operateUser.getSubOrgId();

            //如果不为空并且是北京市公安司法鉴定中心 就把所有分局只要是送检单位选择的是北京市公安司法鉴定中心的都查询出来
            //如果不为空并且选择的是当前登录人的单位的送检中心  就查询当前登录人的送检单位的案件
            String acceptOrgId = null;
            if(StringUtils.isNotBlank(query.getAcceptOrgId()) && ("110230000000").equals(query.getAcceptOrgId())){
                acceptOrgId = "110230000000";
                orgId = null;
            }else if(StringUtils.isNotBlank(query.getAcceptOrgId()) && (StringUtils.isBlank(operateUser.getSubOrgId()) ? operateUser.getOrgId() : operateUser.getSubOrgId()).equals(query.getAcceptOrgId())){
                acceptOrgId = StringUtils.isBlank(operateUser.getSubOrgId()) ? operateUser.getOrgId() : operateUser.getSubOrgId();
            }else if(StringUtils.isBlank(query.getAcceptOrgId())){
                acceptOrgId = null;
            }

            query = resetCaseInfoQuery(query);
            if(StringUtils.isBlank(query.getStatus())){
                query.setStatus(Constants.CASE_STATUS_02);
            }else{
                query.setStatus(query.getStatus());
            }
            query.setDelegateOrgCode(orgId);
            if (orgId!= null){
                if(orgId.contains("110024")){
                    query.setAcceptOrgId(query.getAcceptOrgId());
                }else {
                    query.setAcceptOrgId(acceptOrgId);
                }
            }

            //查询查询与补送数据
            List<LimsCaseInfoVo> caseInfoList =limsCaseInfoService.selectCaseInfoList(query, pageInfo);
            if(null != caseInfoList && caseInfoList.size() > 0){
                for(LimsCaseInfoVo limsCaseInfoVo:caseInfoList){
                    if(StringUtils.isNotBlank(limsCaseInfoVo.getAcceptorId())){
                        //查询受理单位
                        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(limsCaseInfoVo.getAcceptOrgId());
                        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
                            limsCaseInfoVo.setOrgQualification(orgInfo.getOrgQualification());
                        }
                    }
                }
            }

            int caseInfoCount = limsCaseInfoService.selectVOCount(query);
            view = initModelAndView();

            //查询补送次数
            if(null != caseInfoList && caseInfoList.size() > 0){
                for(LimsCaseInfoVo limsCaseInfoVo:caseInfoList){
                    if(StringUtils.isNotBlank(limsCaseInfoVo.getEntity().getCaseId())){
                        LimsConsignmentInfo limsConsignmentInfo = limsConsignatioInfoMapper.selectMaxReplacementNum(limsCaseInfoVo.getEntity().getCaseId());
                        if(limsConsignmentInfo != null){
                            limsCaseInfoVo.getEntity().setReplacementNum(limsConsignmentInfo.getReplacementNum());//replacementNum
                        }
                    }
                }
            }


            //查询分局单位
            OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
            //查询法医中心
            OrgInfo forensicCenterorg = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
            view.addObject("forensicCenterorg",forensicCenterorg);
            //创建orgInfoList点击鉴定中心的选择，可以多选
//            List<OrgInfo> orgInfos = new ArrayList<>();
//            if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
//                orgInfos.add(orgInfo);
//                orgInfos.add(forensicCenterorg);
//            }else if(orgInfo.getOrgId().indexOf("110024") != -1){
//                orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
//                orgInfos.add(orgInfo);
//
//                orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//                orgInfos.add(orgInfo);
//            } else{
//                orgInfos.add(forensicCenterorg);
//            }
            String orgId1 = operateUser.getOrgId();
            String substring = orgId1.substring(0, 6);
            List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);

            view.addObject("orgInfos",delegateCenterConfigs);


            view.addObject("query", query);
            view.addObject("caseInfoList", caseInfoList);
            view.addObject("pageInfo", pageInfo.updatePageInfo(caseInfoCount));
        }catch (Exception ex){
            logger.info("查询失败:"+ex);
        }
        view.setViewName("caseQuery/caseAndBring");
        return view;
    }

    /**
     * 通用查询
     * @return
     */
    @RequestMapping("/caseQuery")
    public ModelAndView caseQuery (HttpServletRequest request, LimsCaseInfoVo query, PageInfo pageInfo, String survey){
        ModelAndView view = new ModelAndView();

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            view.addObject("date", "redirect:/login.html?timeoutFlag=1");
            return  view;
        }

        try {
            String orgId = StringUtils.isBlank(operateUser.getSubOrgId()) ? operateUser.getOrgId() : operateUser.getSubOrgId();

            query = resetCaseInfoQuery(query);
            if("110105000000".equals(orgId)){
                query.setAreaOrgCode("110105");
            }else if("110023000002".equals(orgId)){
                query.setDelegateOrgCode(null);
            }else{
                query.setDelegateOrgCode(orgId);
            }

//            query.setDelegateOrgCode(orgId);

//            if (StringUtils.isNotBlank(survey) && survey.equals("1")) {
//                query.getEntity().setEntrustStatus("0");
//            }
            //查询查询与补送数据
            if(query.getEntity().getEntrustStatus()== ""){
                query.getEntity().setEntrustStatus(null);
            }
            List<LimsCaseInfoVo> caseInfoList =limsCaseInfoService.selectCaseQueryInfoList(query, pageInfo);
            if(null != caseInfoList && caseInfoList.size() > 0){
                for(LimsCaseInfoVo limsCaseInfoVo:caseInfoList){
                    if(StringUtils.isNotBlank(limsCaseInfoVo.getAcceptorId())){
                        //查询受理人姓名
                        AmPersonalInfo amPersonalInfo = amPersonalInfoService.queryAmPersonalInfo(limsCaseInfoVo.getAcceptorId());
                        if(null != amPersonalInfo){
                            limsCaseInfoVo.setAcceptorId(amPersonalInfo.getFullName());
                        }

                        //查询受理单位
                        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(limsCaseInfoVo.getAcceptOrgId());
                        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
                            limsCaseInfoVo.setOrgQualification(orgInfo.getOrgQualification());
                        }
                    }
                }
            }
            int caseInfoCount = limsCaseInfoService.selectCaseQueryVOCount(query);
            view = initModelAndView();
            view.addObject("query", query);
            view.addObject("survey", survey);
            view.addObject("caseInfoList", caseInfoList);
            view.addObject("pageInfo", pageInfo.updatePageInfo(caseInfoCount));
        }catch (Exception ex){
            logger.info("查询失败:"+ex);
        }
        view.setViewName("caseQuery/caseQuery");
        return view;
    }

    @RequestMapping("/updateCaseWord")
    public ModelAndView updateCaseWord (HttpServletRequest request, String caseId, String consignmentId){
        ModelAndView view = new ModelAndView();
        view.addObject("caseId", caseId);
        view.addObject("consignmentId", consignmentId);
        view.setViewName("delegationReg/print");
        return view;
    }

    /**
     * 专家指导查询
     * @return
     */
    @RequestMapping("/consultationQuery")
    public ModelAndView consultationQuery (){
        ModelAndView view = new ModelAndView();
        view.setViewName("caseQuery/consultationQuery");
        return view;
    }

    /**
     * 删除查询与补送
     * @param request
     * @param consignmentId
     * @return
     */
    @RequestMapping("/delCaseAndBring")
    @ResponseBody
    public Map<String,Object> delCaseAndBring(HttpServletRequest request, String consignmentId, String caseId) {
        Map<String,Object> map = new HashMap<>();
        try {
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            if(operateUser == null){
                map.put("date","redirect:/login.html?timeoutFlag=1");
                return  map;
            }
            limsConsignmentInfoService.delCaseAndBring(consignmentId, caseId, operateUser);
            map.put("code", 0);
            map.put("message", "删除成功！");
        }catch(Exception ex){
            logger.info("删除失败"+ex);
            map.put("code", 1);
            map.put("message", "删除失败！");
        }
        return map;
    }

    /**
     * 查询委托登记详情信息
     * @param request
     * @param consignmentId
     * @param caseId
     * @return
     */
    @RequestMapping("/editCaseAndBring")
    public ModelAndView editCaseAndBring(HttpServletRequest request, String consignmentId, String caseId,HttpSession session, String survey) {
        ModelAndView modelAndView = initModelAndView();
        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        operateUser.setStatusId("2");

        //根据委托id查询委托信息
        LimsConsignmentInfo consignatioInfo = limsConsignmentInfoService.selectByConsignmentId(consignmentId);
        //根据案件id查询案件信息
        LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);

        //根据案件id查询人员信息
        LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
        limsPersonInfo.setConsignmentId(consignmentId);
        limsPersonInfo.setCaseId(caseId);
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
        //根据案件id查询样本信息
        LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
        sampleInfoDna.setCaseId(caseId);
        sampleInfoDna.setConsignmentId(consignmentId);
        List<LimsSampleInfoDna> sampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndRy(sampleInfoDna);
        if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0) {
            for (LimsPersonInfo limsPersonInfo1 : limsPersonInfoList) {
                for (LimsSampleInfoDna sampleInfoDna2 : sampleInfoDnaList) {
                    //样本linkId 存在
                    if (limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())) {
                        if (sampleInfoDna2 != null) {
                            LimsPersonInfo person = limsPersonInfoService.selectPersonInfoById(sampleInfoDna2.getLinkId());
                            if (person != null && StringUtils.isNotBlank(person.getPersonType())) {
                                if (person.getPersonType().equals("03")) {
                                    sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_TRUE);
                                }
//                                } else {
//                                    sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_FLASE);
//                                }
                            }
                        }
                        sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                        //样本Linkid 不存在的时候
                    }
//                    }else{
//                        if (sampleInfoDna2!=null && StringUtils.isNotBlank(sampleInfoDna2.getSampleDesc())){
//                            if (sampleInfoDna2.getSampleDesc().substring(0,3).equals("受害人") && !(sampleInfoDna2.getSampleDesc().substring(0,5).equals("受害人亲属"))){
//                                sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_TRUE);
//                            }else{
//                                sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_FLASE);
//                            }
//                        }
//                    }
                }
            }
            //如果人员样本集合为空
        }
//        }else if (limsPersonInfoList == null && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
//               LimsSampleInfoDna sampleInfoDna2 = new LimsSampleInfoDna();
//              for(LimsSampleInfoDna sampleInfo:sampleInfoDnaList){
//                   if (sampleInfo !=null && StringUtils.isNotBlank(sampleInfo.getSampleDesc())){
//                      if ((sampleInfo.getSampleDesc().contains("受害人")||sampleInfo.getSampleDesc().contains("事主"))&&(!sampleInfo.getSampleDesc().contains("受害人亲属"))){
//                          sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_TRUE);
//                       }else{
//                          sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_FLASE);
//                      }
//                  }
//            }
//        }

        //根据案件id查询物证信息
        List<LimsSampleInfoDna> sampleInfoDnaList1 = limsSampleInfoDnaService.selectByCaseIdAndWz(sampleInfoDna);

        //查询分局单位
        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        if(null != orgInfo){
            modelAndView.addObject("orgId", orgInfo.getOrgId());
        }

        OrgInfo subOrgInfo = new OrgInfo();
        subOrgInfo.setOrgCode(consignatioInfo.getDelegateOrgCode());
        subOrgInfo.setOrgName(consignatioInfo.getDelegateOrgName());

        //创建orgInfoList点击鉴定中心的选择，可以多选
        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            //查询鉴定中心
            orgInfos.add(orgInfo);

            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
            orgInfos.add(orgInfo);
        } else if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
            orgInfos.add(orgInfo);

            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
            orgInfos.add(orgInfo);
        }
        else{
            //查询法医鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
            orgInfos.add(orgInfo);
        }
//        int xkNoCount  =  limsCaseInfoService.queryXkNoCount(limsCaseInfo.getCaseXkNo());
//        modelAndView.addObject("xkNoCount",xkNoCount);
        modelAndView.addObject("orgInfos",orgInfos);

        modelAndView.addObject("checkedOrgId", consignatioInfo.getAcceptOrgId());
        modelAndView.addObject("subOrgInfo", subOrgInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("consignatioInfo", consignatioInfo);
        modelAndView.addObject("limsCaseInfo", limsCaseInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
        modelAndView.addObject("sampleInfoDnaList", sampleInfoDnaList);
        modelAndView.addObject("limsSampleInfoList", sampleInfoDnaList1);
        session.setAttribute("user", operateUser);
        session.setAttribute("survey", survey);
        modelAndView.setViewName("delegationReg/dnaReg");
        return modelAndView;
    }

    @RequestMapping("/refresh")
    @ResponseBody
    public Map<String, Object> refresh(HttpServletRequest request,
                                       HttpServletResponse response, String xkNo,String consignmentId,String caseId){
        Map<String, Object> result = new HashMap<String, Object>();
        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            result.put("date","redirect:/login.html?timeoutFlag=1");
            return  result;
        }

        //根据现勘编号查询现勘数据
        XckyAddressInfo xckyAddressInfo = xckyAddressInfoService.selectByOrgId(operateUser.getOrgId());
        if(xckyAddressInfo == null){
            xckyAddressInfo = xckyAddressInfoService.selectDefault();
        }

        Map<String, Object> caseSampleInfoList = null;
        try {
            caseSampleInfoList = xckyWebService.getXckyInfoByKno(xckyAddressInfo, xkNo);
            //caseSampleInfoList = getXkCaseService.getCaseByXkNo(xkNo);
        }catch(Exception ex){
            logger.error("根据现勘号调取案件信息时出现错误！", ex);
        }


        List<LimsSampleInfoDna> limsSampleInfoListXk = (List<LimsSampleInfoDna>)caseSampleInfoList.get("limsSampleInfoList");

        //根据案件id查询物证检材信息  并和现堪返回的物证信息作对比
        List<LimsSampleInfoDna> limsSampleInfoDnaListWz = new ArrayList<>();
        LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
        limsSampleInfoDna.setCaseId(caseId);
        limsSampleInfoDna.setConsignmentId(consignmentId);
        List<LimsSampleInfoDna> limsSampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndWz(limsSampleInfoDna);

        List<String> l1=new ArrayList<>();
        Map<String,Integer> map=new HashMap<>();
        for(int i=0;i<limsSampleInfoListXk.size();i++){
            l1.add(limsSampleInfoListXk.get(i).getEvidenceNo());
            map.put(limsSampleInfoListXk.get(i).getEvidenceNo(),i);
        }
        List<String> l2=new ArrayList<>();
        for(int j=0;j<limsSampleInfoDnaList.size();j++){
            l2.add(limsSampleInfoDnaList.get(j).getEvidenceNo());
        }
        l1.removeAll(l2);
        for(int m=0;m<l1.size();m++){
            limsSampleInfoDnaListWz.add(limsSampleInfoListXk.get(map.get(l1.get(m))));
        }

        //对物证检材进行排序
        if(null != limsSampleInfoDnaListWz && limsSampleInfoDnaListWz.size() > 0){
            Collections.sort(limsSampleInfoDnaListWz , new Comparator<LimsSampleInfoDna>() {
                @Override
                public int compare(LimsSampleInfoDna l1, LimsSampleInfoDna l2) {
                    String evidenceNo1 = l1.getEvidenceNo().substring(8);
                    String evidenceNo2 = l2.getEvidenceNo().substring(8);
                    BigDecimal no1 = new BigDecimal(evidenceNo1);
                    BigDecimal no2 = new BigDecimal(evidenceNo2);
                    //相减
                    BigDecimal diff = no1.subtract(no2);
                    if (diff.intValue() > 0) {
                        return 1;
                    }else if (diff.intValue() < 0) {
                        return -1;
                    }
                    return 0; //相等为0
                }
            });
        }
        limsSampleInfoDnaListWz.addAll(limsSampleInfoDnaList);

        //根据案件id查询被鉴定人样本信息  并和现堪返回的被鉴定人样本信息作对比
        List<LimsSampleInfoDna> LimsSampleInfoDnaybXk = (List<LimsSampleInfoDna>)caseSampleInfoList.get("limsPersonSampleInfoList");
        //根据案件id和委托id查询被鉴定人信息
        LimsPersonInfo limsPersonInfos = new LimsPersonInfo();
        limsPersonInfos.setCaseId(caseId);
        limsPersonInfos.setConsignmentId(consignmentId);
        List<LimsPersonInfo> limsPersonInfoList2 = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfos);
        LimsSampleInfoDna limsSampleInfoDnayb = new LimsSampleInfoDna();
        limsSampleInfoDnayb.setCaseId(caseId);
        limsSampleInfoDnayb.setConsignmentId(consignmentId);
        //根据案件id和委托id查询样本信息
        List<LimsSampleInfoDna> limsSampleInfoDnaybList1 = limsSampleInfoDnaService.selectByCaseIdAndRy(limsSampleInfoDnayb);
        //获取样本对应的被鉴定人的样本关联人名称
        if(null != limsPersonInfoList2 && limsPersonInfoList2.size() > 0 && null != limsSampleInfoDnaybList1 && limsSampleInfoDnaybList1.size() > 0){
            for(LimsPersonInfo limsPersonInfo1:limsPersonInfoList2){
                for(LimsSampleInfoDna sampleInfoDna2:limsSampleInfoDnaybList1){
                    if(limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())){
                        sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                    }
                }
            }
        }
        //判断样本list不为空
        if(null != limsSampleInfoDnaybList1 && limsSampleInfoDnaybList1.size() > 0){
            for(int g=0; g<limsSampleInfoDnaybList1.size();g++){
                String sampleName = limsSampleInfoDnaybList1.get(g).getSampleName();
                String sampleType = limsSampleInfoDnaybList1.get(g).getSampleType();
                for(int h=0; h<LimsSampleInfoDnaybXk.size();h++){
                    String sampleName1 = LimsSampleInfoDnaybXk.get(h).getSampleName();
                    String sampleType1 = LimsSampleInfoDnaybXk.get(h).getSampleType();
                    //根据样本类型和样本名称进行筛选,
                    if(StringUtils.isNotBlank(sampleName) && StringUtils.isNotBlank(sampleName1)){
                        if(StringUtils.isNotBlank(sampleType) && StringUtils.isNotBlank(sampleType1)){
                            if((sampleName).equals(sampleName1) && (sampleType).equals(sampleType1)){
                                LimsSampleInfoDnaybXk.remove(h);
                                break;
                            }
                        }
                    }
                }
            }
            if(null != LimsSampleInfoDnaybXk && LimsSampleInfoDnaybXk.size()>0){
                limsSampleInfoDnaybList1.addAll(LimsSampleInfoDnaybXk);
            }
        }else{
            limsSampleInfoDnaybList1 = LimsSampleInfoDnaybXk;
        }


        //根据案件id查询被鉴定人信息  并和现堪返回的被鉴定人信息作对比
        //根据现堪编号获取被鉴定人信息list
        List<LimsPersonInfo> limsPersonInfoListXk = (List<LimsPersonInfo>)caseSampleInfoList.get("limsPersonInfoList");
        LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
        limsPersonInfo.setCaseId(caseId);
        limsPersonInfo.setConsignmentId(consignmentId);
        //根据案件id和委托id查询被鉴定人信息list
        List<LimsPersonInfo> limsPersonInfoList1 = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
        //根据用户身份证号，用户名，用户类型筛选
        if(null != limsPersonInfoList1 && limsPersonInfoList1.size() > 0 ){
            for(int i=0;i<limsPersonInfoList1.size();i++){
                String xkPersonName = limsPersonInfoList1.get(i).getPersonName();
                String xkPersonType = limsPersonInfoList1.get(i).getPersonType();
                String xkPersonIdCard = limsPersonInfoList1.get(i).getPersonIdCard();
                for(int j=0;j<limsPersonInfoListXk.size();j++){
                    String xkPersonName1 = limsPersonInfoListXk.get(j).getPersonName();
                    String xkPersonType1 = limsPersonInfoListXk.get(j).getPersonType();
                    String xkPersonIdCard1 = limsPersonInfoListXk.get(j).getPersonIdCard();
                    String personId1 = limsPersonInfoListXk.get(j).getPersonId();

                    //先根据身份证号筛选,
                    if(StringUtils.isNotBlank(xkPersonIdCard) && StringUtils.isNotBlank(xkPersonIdCard1)){
                        if(xkPersonIdCard.equals(xkPersonIdCard1)){
                            for(LimsSampleInfoDna limsSampleInfoDna1:limsSampleInfoDnaybList1){
                                if(null != limsSampleInfoDna1){
                                    if(StringUtils.isNotBlank(limsSampleInfoDna1.getLinkId())){
                                        if(StringUtils.isNotBlank(personId1)){
                                            if((personId1).equals(limsSampleInfoDna1.getLinkId())){
                                                limsSampleInfoDna1.setLinkId(limsPersonInfoList1.get(i).getPersonId());
                                            }
                                        }
                                    }
                                }
                            }
                            limsPersonInfoListXk.remove(j);
                            break;
                        }
                    }else{
                        if(StringUtils.isNotBlank(xkPersonName) && StringUtils.isNotBlank(xkPersonName1)){
                            if(StringUtils.isNotBlank(xkPersonType) && StringUtils.isNotBlank(xkPersonType1)){
                                if(xkPersonName.equals(xkPersonName1) && xkPersonType.equals(xkPersonType1)){
                                    for(LimsSampleInfoDna limsSampleInfoDna1:limsSampleInfoDnaybList1){
                                        if(null != limsSampleInfoDna1){
                                            if(StringUtils.isNotBlank(limsSampleInfoDna1.getLinkId())){
                                                if((limsPersonInfoListXk.get(j).getPersonId()).equals(limsSampleInfoDna1.getLinkId())){
                                                    limsSampleInfoDna1.setLinkId(limsPersonInfoList1.get(i).getPersonId());
                                                }
                                            }
                                        }
                                    }
                                    limsPersonInfoListXk.remove(j);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if(null != limsPersonInfoListXk && limsPersonInfoListXk.size() > 0 ){
                limsPersonInfoList1.addAll(limsPersonInfoListXk);
            }
        }else{
            limsPersonInfoList1=limsPersonInfoListXk;
        }


        result.put("limsSampleInfoDnaybList", limsSampleInfoDnaybList1);
        result.put("limscaseInfo", caseSampleInfoList.get("limsCaseInfo"));
        result.put("limsPersonInfoList", limsPersonInfoList1);
        result.put("limsSampleInfoDnaListWz", limsSampleInfoDnaListWz);
        result.put("date", limsSampleInfoDnaListWz.size());
        return result;
    }


    /**
     * 查询添加补送的案件记录并调到添加补送页面
     * @param request
     * @param response
     * @param caseId
     * @return
     */
    @RequestMapping("/replacementByCaseId")
    public ModelAndView replacementByCaseId(HttpServletRequest request, HttpServletResponse response, String caseId,HttpSession session) {
        ModelAndView modelAndView = initModelAndView();
        try {
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            operateUser.setStatusId("1");
            if(operateUser == null){
                modelAndView.setViewName("redirect:/login.html?timeoutFlag=1");
                return  modelAndView;
            }

            //根据案件id查询案件信息
            LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
            List<LimsConsignmentInfo> consignmentInfoList = limsConsignmentInfoService.selectByCaseId(caseId);
            //物证
            List<LimsSampleInfoDna> limsSampleInfoDnaList = limsSampleInfoDnaService.selectByCaseId(limsCaseInfo.getCaseId());
            //人员样本
            List<LimsSampleInfoDna> limsSampleInfoDnaList2 = limsSampleInfoDnaService.selectYbByCaseId(limsCaseInfo.getCaseId());
            //人员
            List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseId(limsCaseInfo.getCaseId());

            LimsConsignmentInfo consignmentInfo = null;
            for(LimsConsignmentInfo limsConsignmentInfo : consignmentInfoList){
                if(limsConsignmentInfo.getAppendFlag().equals("0")){
                    consignmentInfo = limsConsignmentInfo;
                }
            }
            modelAndView.addObject("limsCaseInfo", limsCaseInfo);
            modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
            modelAndView.addObject("areaOrgCode", consignmentInfo == null ? "" : consignmentInfo.getAreaOrgCode());

            //创建orgInfoList鉴定中心的选择，可以多选
            List<OrgInfo> orgInfos = new ArrayList<>();
            if(consignmentInfo != null && StringUtils.isNotBlank(consignmentInfo.getAcceptOrgId())){
                OrgInfo orgInfo1 = orgInfoService.selectByPrimaryKey(consignmentInfo.getAcceptOrgId());
                orgInfos.add(orgInfo1);
            }
            modelAndView.addObject("orgInfos",orgInfos);

            LimsConsignmentInfo limsConsignmentInfo = new LimsConsignmentInfo();
            limsConsignmentInfo.setConsignmentNo("委托书编号自动生成");

            //补送的时候委托人默认和主案件一样
            limsConsignmentInfo.setDelegator1Id(consignmentInfo.getDelegator1Id());
            limsConsignmentInfo.setDelegator2Id(consignmentInfo.getDelegator2Id());
            limsConsignmentInfo.setDelegator1Position(consignmentInfo.getDelegator1Position());
            limsConsignmentInfo.setDelegator2Position(consignmentInfo.getDelegator2Position());
            limsConsignmentInfo.setDelegator1PaperworkNo(consignmentInfo.getDelegator1PaperworkNo());
            limsConsignmentInfo.setDelegator2PaperworkNo(consignmentInfo.getDelegator2PaperworkNo());
            limsConsignmentInfo.setDelegator1PhoneNumber(consignmentInfo.getDelegator1PhoneNumber());
            limsConsignmentInfo.setDelegator2PhoneNumber(consignmentInfo.getDelegator2PhoneNumber());

            modelAndView.addObject("consignatioInfo", limsConsignmentInfo);

            //根据现勘编号查询现勘数据
        /*
            1、判断该现勘号是否已经获取；
            2、根据当前登录人所在单位获取对应的现勘接口信息；
            3、根据[2]中获取的现勘信息以及待调取信息的K号，获取数据；
            4、如果指定K号在现勘系统中不存在，则跳转到错误提示页面：提示现勘号不存在，让用户重新输入现勘号。
         */

            OrgInfo xcOrg = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
            //如果登录人时派出所级别，则获取对应分局
//            if(orgInfo.getOrgLevel().equals(Constants.ORG_LEVEL_PAICHUSUO)){
//                xcOrg = orgInfoService.selectByPrimaryKey(orgInfo.getParentId());
//            }

            XckyAddressInfo xckyAddressInfo = xckyAddressInfoService.selectByOrgId(xcOrg.getOrgId());
            if(xckyAddressInfo == null){
                xckyAddressInfo = xckyAddressInfoService.selectDefault();
            }

            Map<String, Object> caseSampleInfoList = null;
            try {
                caseSampleInfoList = xckyWebService.getXckyInfoByKno(xckyAddressInfo, limsCaseInfo.getCaseXkNo());
            }catch(Exception ex){
                logger.error("根据现勘号调取案件信息时出现错误！", ex);
            }

            if (caseSampleInfoList == null) {
                /*// 跳转到错误提示页面：提示现勘号不存在，让用户重新输入现勘号。
                modelAndView.addObject("errMsg", "现勘号" + limsCaseInfo.getCaseXkNo() + "不存在，请核实后重新获取！");
                modelAndView.setViewName("delegationReg/dnaXkErr");
                return modelAndView;*/

//                modelAndView.addObject("limsSampleInfoList", limsSampleInfoDnaList);
//                modelAndView.addObject("sampleInfoDnaList", limsSampleInfoDnaList2);
//                modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
            }else {

                LimsCaseInfo limsCaseInfoXk = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                List<LimsSampleInfoDna> limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                List<LimsSampleInfoDna> limsPersonSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsPersonSampleInfoList");
                List<LimsPersonInfo> limsPersonInfoListXk = (List<LimsPersonInfo>) caseSampleInfoList.get("limsPersonInfoList");

                modelAndView.addObject("limsCaseInfoXk", limsCaseInfoXk);
                modelAndView.addObject("limsSampleInfoListXk", limsSampleInfoListXk);


                //根据案件id查询物证检材信息  并和现堪返回的物证信息作对比
                List<LimsSampleInfoDna> limsSampleInfoDnaListWz = new ArrayList<>();

                List<String> l1 = new ArrayList<>();
                Map<String, Integer> map = new HashMap<>();
                if (null != limsSampleInfoListXk && limsSampleInfoListXk.size() > 0) {
                    for (int i = 0; i < limsSampleInfoListXk.size(); i++) {
                        l1.add(limsSampleInfoListXk.get(i).getEvidenceNo());
                        map.put(limsSampleInfoListXk.get(i).getEvidenceNo(), i);
                    }
                    List<String> l2 = new ArrayList<>();
                    for (int j = 0; j < limsSampleInfoDnaList.size(); j++) {
                        l2.add(limsSampleInfoDnaList.get(j).getEvidenceNo());
                    }
                    l1.removeAll(l2);
                    for (int m = 0; m < l1.size(); m++) {
                        limsSampleInfoDnaListWz.add(limsSampleInfoListXk.get(map.get(l1.get(m))));
                    }
                }
                modelAndView.addObject("limsSampleInfoList", limsSampleInfoDnaListWz);

                //根据案件id查询人员样本信息  并和现堪返回的物证信息作对比
                List<LimsSampleInfoDna> limsSampleInfoDnaListYb = new ArrayList<>();

                List<String> l3 = new ArrayList<>();
                Map<String, Integer> map1 = new HashMap<>();
                if (null != limsPersonSampleInfoListXk && limsPersonSampleInfoListXk.size() > 0) {
                    for (int i = 0; i < limsPersonSampleInfoListXk.size(); i++) {
                        l3.add(limsPersonSampleInfoListXk.get(i).getSampleName());
                        map1.put(limsPersonSampleInfoListXk.get(i).getSampleName(), i);
                    }

                    List<String> l4 = new ArrayList<>();
                    if (null != limsSampleInfoDnaList2 && limsSampleInfoDnaList2.size() > 0) {
                        for (int j = 0; j < limsSampleInfoDnaList2.size(); j++) {
                            l4.add(limsSampleInfoDnaList2.get(j).getSampleName());
                        }
                    }
                    l3.removeAll(l4);
                    for (int m = 0; m < l3.size(); m++) {
                        limsSampleInfoDnaListYb.add(limsPersonSampleInfoListXk.get(map1.get(l3.get(m))));
                    }
                    //如果人员类型为03 则默认为事主样本
                    for (LimsPersonInfo  limsPersonInfo :limsPersonInfoListXk){
                        if (limsPersonInfo!=null && StringUtils.isNotBlank(limsPersonInfo.getPersonType())){
                            for (LimsSampleInfoDna  sampleInfoDna2: limsSampleInfoDnaListYb){
                                if (sampleInfoDna2!=null && sampleInfoDna2.getPersonName().equals(limsPersonInfo.getPersonName())){
                                    if (limsPersonInfo.getPersonType().equals("03")){
                                        sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_TRUE);
                                    }else{
                                        sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_FLASE);
                                    }
                                }
                            }
                        }
                    }
                }
                modelAndView.addObject("sampleInfoDnaList", limsSampleInfoDnaListYb);


                //根据案件id查询人员信息  并和现堪返回的人员信息作对比
                List<LimsPersonInfo> limsPersonInfoListK = new ArrayList<>();

                List<String> l5 = new ArrayList<>();
                Map<String, Integer> map2 = new HashMap<>();
                if (null != limsPersonInfoListXk && limsPersonInfoListXk.size() > 0) {
                    for (int i = 0; i < limsPersonInfoListXk.size(); i++) {
                        l5.add(limsPersonInfoListXk.get(i).getPersonName());
                        map2.put(limsPersonInfoListXk.get(i).getPersonName(), i);
                    }

                    List<String> l6 = new ArrayList<>();
                    for (int j = 0; j < limsPersonInfoList.size(); j++) {
                        l6.add(limsPersonInfoList.get(j).getPersonName());
                    }
                    l5.removeAll(l6);
                    for (int m = 0; m < l5.size(); m++) {
                        limsPersonInfoListK.add(limsPersonInfoListXk.get(map2.get(l5.get(m))));
                    }
                }

                modelAndView.addObject("limsPersonInfoList", limsPersonInfoListK);
            }

            session.setAttribute("user",operateUser);
            modelAndView.setViewName("caseQuery/replacement");
        } catch (Exception ex) {
            logger.error("添加补送页面案件信息查询失败："+ex);
        }
        return modelAndView;
    }

    /**
     * 添加委托补送信息
     * @param paramsData
     * @return
     */
    @RequestMapping(value="/submitReplacement", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> submitReplacement(@RequestParam(value = "paramsData")String paramsData,HttpServletRequest request) {
        Map<String,Object> map=new HashMap();
        String url="";
        try {
            JSONObject str = JSON.parseObject(paramsData);
            //委托信息
            LimsConsignmentInfo consignatioInfo = new LimsConsignmentInfo();
            //案件信息
            LimsCaseInfo caseInfoDna = new LimsCaseInfo();
            //被鉴定人信息已经对应的样本信息
            List<LimsPersonInfo> limsPersonInfoList = new ArrayList<>();
            //物证检材信息
            List<LimsSampleInfoDna> sampleInfoDnaList = new LinkedList<>();
            //鉴定中心主键id
            String evaluationCenterId = "";
            String orgname = "";

            //解析前台传来的数据
            if(str.containsKey("evaluationCenter")){
                evaluationCenterId = str.get("evaluationCenter").toString();
                logger.info("evaluationCenterId = "+evaluationCenterId);
            }
            if(str.containsKey("consignmentInfo")) {
                consignatioInfo = JSON.parseObject(str.get("consignmentInfo").toString(), LimsConsignmentInfo.class);
                logger.info("consignatioInfo = "+consignatioInfo);
            }

            if(str.containsKey("caseInfoDna")) {
                caseInfoDna = JSON.parseObject(str.get("caseInfoDna").toString(), LimsCaseInfo.class);
                logger.info("caseInfoDna = "+caseInfoDna);
            }

            if(str.containsKey("limsPersonInfoList")) {
                limsPersonInfoList = (List<LimsPersonInfo>) JSON.parseArray(str.get("limsPersonInfoList").toString(), LimsPersonInfo.class);
            }

            if(str.containsKey("sampleInfoDnaList")) {
                sampleInfoDnaList = (List<LimsSampleInfoDna>) JSON.parseArray(str.get("sampleInfoDnaList").toString(), LimsSampleInfoDna.class);
                logger.info("sampleInfoDnaList = "+sampleInfoDnaList);

            }
//            if(str.containsKey("orgname")) {
//                orgname = str.get("orgname").toString();
//            }

            DelegateDataModel delegateDataModel = new DelegateDataModel();
            delegateDataModel.setCaseInfoDna(caseInfoDna);
            delegateDataModel.setLimsPersonInfoList(limsPersonInfoList);
            delegateDataModel.setSampleInfoDnaList(sampleInfoDnaList);
            delegateDataModel.setConsignatioInfo(consignatioInfo);

            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            if(operateUser == null){
                map.put("success",false);
                map.put(url,"/login.html?timeoutFlag=1");
                return  map;
            }

            //添加委托信息
            Map<String, String> result = limsCaseInfoService.submitReplacement(delegateDataModel, operateUser,evaluationCenterId);
            map.put("caseId",result == null ? "" : (result.get("caseId")==null?"":result.get("caseId")));
            map.put("consignmentId",result == null ? "" : (result.get("consignmentId")==null?"":result.get("consignmentId")));
            map.put("success",true);

//            if (0 == isAppInform){ //1开启 0默认关闭
//                String date = DateUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
//                CallThirdpartyInfo bean = new CallThirdpartyInfo();
//                //发起人信息
//                bean.setUserid(operateUser.getUserId());
//                bean.setUserName(operateUser.getLoginName());
//                bean.setOrgid(operateUser.getOrgId());
//                bean.setIp(IpAddressUtils.getIpAddr(request));
//                //接收人
//                List<String> phones = new ArrayList<>();
//                if (null != consignatioInfo.getDelegator1PhoneNumber())
//                    phones.add(consignatioInfo.getDelegator1PhoneNumber());
//                if (null != consignatioInfo.getDelegator2PhoneNumber())
//                    phones.add(consignatioInfo.getDelegator2PhoneNumber());
//                bean.setPhones(phones);
//                String countName = consignatioInfo.getDelegator1Name()+","+consignatioInfo.getDelegator2Name();
//                ArrayList<MobileNews> mobileNewsList = new ArrayList<>();
//                MobileNews count = new MobileNews();
//                count.setId(UuidUtil.generateUUID());
//                count.setTitle(caseInfoDna.getCaseName());
//                count.setContent(CallThirdpartyTool.Content(5,countName,consignatioInfo.getDelegateOrgName(),orgname));
//                count.setState(0);
//                count.setCreateDatetime(date);
//                count.setUpdateDatetime(date);
//                count.setType(5);
//                count.setUserid(consignatioInfo.getDelegator1Id());
//                count.setUsername(consignatioInfo.getDelegator1Name());
//                count.setMessageType(1);
//                count.setUserOrg(consignatioInfo.getDelegateOrgCode());
//                count.setCaseId(consignatioInfo.getCaseId());
//                MobileNews count2 = new MobileNews();
//                count2.setId(UuidUtil.generateUUID());
//                count2.setTitle(caseInfoDna.getCaseName());
//                count2.setContent(CallThirdpartyTool.Content(5,countName,consignatioInfo.getDelegateOrgName(),orgname));
//                count2.setState(0);
//                count2.setCreateDatetime(date);
//                count2.setUpdateDatetime(date);
//                count2.setType(5);
//                count2.setUserid(consignatioInfo.getDelegator2Id());
//                count2.setUsername(consignatioInfo.getDelegator2Name());
//                count2.setUserOrg(consignatioInfo.getDelegateOrgCode());
//                count2.setMessageType(1);
//                count2.setCaseId(consignatioInfo.getCaseId());
//                MobileNews count3 = new MobileNews();
//                count3.setId(UuidUtil.generateUUID());
//                count3.setTitle(caseInfoDna.getCaseName());
//                count3.setContent(CallThirdpartyTool.Content(5,countName,consignatioInfo.getDelegateOrgName(),orgname));
//                count3.setState(0);
//                count3.setCreateDatetime(date);
//                count3.setUpdateDatetime(date);
//                count3.setType(5);
//                count3.setUserid("pc");
//                count3.setUserOrg(consignatioInfo.getDelegateOrgCode());
//                count3.setCaseId(consignatioInfo.getCaseId());
//                count3.setMessageType(2);
//                mobileNewsList.add(count);
//                mobileNewsList.add(count2);
//                mobileNewsList.add(count3);
//                //信息存储到消息表
//                for (MobileNews MobileNews: mobileNewsList) {
//                    int insert = mobileNewsMapper.insert(MobileNews);
//                    logger.info("===成功存储消息:"+insert+"条.");
//                }
//                bean.setContent(mobileNewsList);
//                //第三方唤醒app
//                LogRecordInfo logRecordInfo = CallThirdpartyTool.CallThirdpartyTool(AppUrl, bean);
//                logger.info("===唤醒app结束开始存储日志");
//                logRecordMapper.insert(logRecordInfo);
//
//            }


        }catch(Exception ex){
            map.put("error",false);
            logger.error("委托登记保存失败"+ex.getMessage());
        }
        return map;
    }


    /**
     * 查询补送记录
     * @param request
     * @param caseId
     * @return
     */
    @RequestMapping("/queryReplacementRecord")
    @ResponseBody
    public Map<String,Object> queryReplacementRecord(HttpServletRequest request, String caseId) {
        Map<String,Object> map = new HashMap<>();
        try {
            LimsCaseInfoVo limsCaseInfoVo = new LimsCaseInfoVo();
            limsCaseInfoVo.getEntity().setCaseId(caseId);
            List<LimsCaseInfoVo> limsCaseInfoVoList = limsCaseInfoService.selectReplacementRecord(limsCaseInfoVo);
            map.put("limsCaseInfoVoList", limsCaseInfoVoList);
            map.put("code", 0);
            map.put("message", "补送记录查询成功！");
        }catch(Exception ex){
            logger.info("补送记录查询失败"+ex);
            map.put("code", 1);
            map.put("message", "补送记录查询失败！");
        }
        return map;
    }

    /**
     * 查询补送记录详情
     * @param request
     * @param consignmentId
     * @param caseId
     * @return
     */
    @RequestMapping("/queryReplacementRecordDetail")
    public ModelAndView queryReplacementRecordDetail(HttpServletRequest request, String consignmentId, String caseId) {
        ModelAndView modelAndView = initModelAndView();

        //根据委托id查询委托信息
        LimsConsignmentInfo consignatioInfo = limsConsignmentInfoService.selectByConsignmentId(consignmentId);
        //根据案件id查询案件信息
        LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
        //根据案件id查询人员信息
        LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
        limsPersonInfo.setConsignmentId(consignmentId);
        limsPersonInfo.setCaseId(caseId);
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
        //根据案件id查询样本信息
        LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
        sampleInfoDna.setCaseId(caseId);
        sampleInfoDna.setConsignmentId(consignmentId);
        List<LimsSampleInfoDna> sampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndRy(sampleInfoDna);
        if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
            for(LimsPersonInfo limsPersonInfo1:limsPersonInfoList){
                for(LimsSampleInfoDna sampleInfoDna2:sampleInfoDnaList){
                    if(limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())){
                        sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                    }
                }
            }
        }

        //根据案件id查询物证信息
        List<LimsSampleInfoDna> sampleInfoDnaList1 = limsSampleInfoDnaService.selectByCaseIdAndWz(sampleInfoDna);

        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgName(consignatioInfo.getDelegateOrgName());
        modelAndView.addObject("orgInfo", orgInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("consignatioInfo", consignatioInfo);
        modelAndView.addObject("limsCaseInfo", limsCaseInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
        modelAndView.addObject("sampleInfoDnaList", sampleInfoDnaList);
        modelAndView.addObject("limsSampleInfoList", sampleInfoDnaList1);
        modelAndView.setViewName("delegationReg/replacementRecordDetail");
        return modelAndView;
    }


    /**
     * 查询非案件委托登记详情信息
     * @param request
     * @param consignmentId
     * @param caseId
     * @return
     */
    @RequestMapping("/editNonCaseAndBring")
    public ModelAndView editNonCaseAndBring(HttpServletRequest request, String consignmentId, String caseId,HttpSession session) {
        ModelAndView modelAndView = initModelAndView();

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        operateUser.setStatusId("2");

        DictItem dictItem = new DictItem();

        //案件性质
        List<DictItem> casePropertyList1 = new LinkedList<>();
        dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
        List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);
        for(DictItem dictItem1:casePropertyList){
            if(("06").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            }else if(("09").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            } else if(("10").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            } else if(("11").equals(dictItem1.getDictCode())) {
                casePropertyList1.add(dictItem1);
            }else  if(("19").equals(dictItem1.getDictCode())) {
                casePropertyList1.add(dictItem1);
            }else if(("20").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            }
        }
        modelAndView.addObject("casePropertyList",casePropertyList1);

        //根据委托id查询委托信息
        LimsConsignmentInfo consignatioInfo = limsConsignmentInfoService.selectByConsignmentId(consignmentId);
        //根据案件id查询案件信息
        LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
        //根据案件id查询人员信息
        LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
        limsPersonInfo.setConsignmentId(consignmentId);
        limsPersonInfo.setCaseId(caseId);
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
        //根据案件id查询样本信息
        LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
        sampleInfoDna.setCaseId(caseId);
        sampleInfoDna.setConsignmentId(consignmentId);
        List<LimsSampleInfoDna> sampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndRy(sampleInfoDna);
        if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
            for(LimsPersonInfo limsPersonInfo1:limsPersonInfoList){
                for(LimsSampleInfoDna sampleInfoDna2:sampleInfoDnaList){
                    if(limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())){
                        sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                    }
                }
            }
        }

        //根据案件id查询物证信息
        List<LimsSampleInfoDna> sampleInfoDnaList1 = limsSampleInfoDnaService.selectByCaseIdAndWzNon(sampleInfoDna);

        OrgInfo orgInfoYn = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());

        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //创建orgInfoList点击鉴定中心的选择，可以多选
//        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            //查询鉴定中心
//            orgInfos.add(orgInfo);

            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }
        else if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
//            orgInfos.add(orgInfo);

//            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }
        else{
            //查询法医鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }
        String orgId = operateUser.getOrgId();
        String substring = orgId.substring(0, 6);
        List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);

        modelAndView.addObject("orgInfos",delegateCenterConfigs);

        modelAndView.addObject("checkedOrgId", consignatioInfo.getAcceptOrgId());
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("consignatioInfo", consignatioInfo);
        modelAndView.addObject("limsCaseInfo", limsCaseInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
        modelAndView.addObject("sampleInfoDnaList", sampleInfoDnaList);
        modelAndView.addObject("limsSampleInfoList", sampleInfoDnaList1);
        session.setAttribute("user",operateUser);
        modelAndView.setViewName("delegationReg/nonDnaReg");
        return modelAndView;
    }

    /**
     * 查询在逃人员案件委托登记详情信息
     * @param request
     * @param consignmentId
     * @param caseId
     * @return
     */
    @RequestMapping("/editFugitivesReg")
    public ModelAndView editFugitivesReg(HttpServletRequest request, String consignmentId, String caseId,HttpSession session) {
        ModelAndView modelAndView = initModelAndView();

        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        operateUser.setStatusId("2");

        DictItem dictItem = new DictItem();

        //案件性质
        dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
        List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);

        modelAndView.addObject("casePropertyList",casePropertyList);

        //根据委托id查询委托信息
        LimsConsignmentInfo consignatioInfo = limsConsignmentInfoService.selectByConsignmentId(consignmentId);
        //根据案件id查询案件信息
        LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
        //根据案件id查询人员信息
        LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
        limsPersonInfo.setConsignmentId(consignmentId);
        limsPersonInfo.setCaseId(caseId);
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
        //区分出在逃人员和在逃人员亲属
        if (ListUtils.isNotNullAndEmptyList(limsPersonInfoList)) {
            List<LimsPersonInfo> fugitivesInfoList = new ArrayList<>();
            List<LimsPersonInfo> fugitivesInfoRelationList = new ArrayList<>();
            for (LimsPersonInfo lpi : limsPersonInfoList) {
                if (Constants.PERSON_TYPE_07.equals(lpi.getPersonType())) {
                    fugitivesInfoList.add(lpi);
                }else if (Constants.PERSON_TYPE_08.equals(lpi.getPersonType())) {
                    //判断目标样本id是否存在
                    if (StringUtils.isNotBlank(lpi.getTargetPersonId())) {
                        FugitivesInfo fugitivesInfo = fugitivesInfoService.selectByPrimaryKey(lpi.getTargetPersonId());
                        //判断在逃人员信息是否存在
                        if (fugitivesInfo != null) {
                            lpi.setFugitivesName(fugitivesInfo.getPersonName());
                            lpi.setFugitivesCard(fugitivesInfo.getPersonCard());
                            lpi.setFugitiveNo(fugitivesInfo.getFugitiveNo());
                        }
                    }
                    fugitivesInfoRelationList.add(lpi);
                }
            }
            modelAndView.addObject("fugitivesInfoList",fugitivesInfoList);
            modelAndView.addObject("fugitivesInfoRelationList",fugitivesInfoRelationList);
        }
        //根据案件id查询样本信息
        LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
        sampleInfoDna.setCaseId(caseId);
        sampleInfoDna.setConsignmentId(consignmentId);
        List<LimsSampleInfoDna> sampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndRy(sampleInfoDna);
        if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
            for(LimsPersonInfo limsPersonInfo1:limsPersonInfoList){
                for(LimsSampleInfoDna sampleInfoDna2:sampleInfoDnaList){
                    if(limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())){
                        sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                    }
                }
            }
        }

        //根据案件id查询物证信息
        List<LimsSampleInfoDna> sampleInfoDnaList1 = limsSampleInfoDnaService.selectByCaseIdAndWzNon(sampleInfoDna);

        OrgInfo orgInfoYn = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());

        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //创建orgInfoList点击鉴定中心的选择，可以多选
//        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            //查询鉴定中心
//            orgInfos.add(orgInfo);

            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }
        else if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
//            orgInfos.add(orgInfo);

//            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }
        else{
            //查询法医鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }
        String orgId = operateUser.getOrgId();
        String substring = orgId.substring(0, 6);
        List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);

        modelAndView.addObject("orgInfos",delegateCenterConfigs);

        modelAndView.addObject("checkedOrgId", consignatioInfo.getAcceptOrgId());
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("consignatioInfo", consignatioInfo);
        modelAndView.addObject("limsCaseInfo", limsCaseInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
        modelAndView.addObject("sampleInfoDnaList", sampleInfoDnaList);
        modelAndView.addObject("limsSampleInfoList", sampleInfoDnaList1);
        session.setAttribute("user",operateUser);
        modelAndView.setViewName("delegationReg/fugitivesReg");
        return modelAndView;
    }

    /**
     * 查询添加补送的非案件案件委托记录并调到添加补送页面
     * @param request
     * @param response
     * @param caseId
     * @return
     */
    @RequestMapping("/replacementNonCaseByCaseId")
    public ModelAndView replacementNonCaseByCaseId(HttpServletRequest request, HttpServletResponse response, String caseId,HttpSession session) {
        ModelAndView modelAndView = initModelAndView();
        try {
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            operateUser.setStatusId("1");

            DictItem dictItem = new DictItem();
            //案件性质
            List<DictItem> casePropertyList1 = new LinkedList<>();
            dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
            List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);
            for(DictItem dictItem1:casePropertyList){
                if(("06").equals(dictItem1.getDictCode())){
                    casePropertyList1.add(dictItem1);
                }else if(("09").equals(dictItem1.getDictCode())){
                    casePropertyList1.add(dictItem1);
                } else if(("10").equals(dictItem1.getDictCode())){
                    casePropertyList1.add(dictItem1);
                } else if(("11").equals(dictItem1.getDictCode())) {
                    casePropertyList1.add(dictItem1);
                }else  if(("19").equals(dictItem1.getDictCode())) {
                    casePropertyList1.add(dictItem1);
                }else if(("20").equals(dictItem1.getDictCode())){
                    casePropertyList1.add(dictItem1);
                }
            }
            modelAndView.addObject("casePropertyList",casePropertyList1);

            //根据案件id查询案件信息
            LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
            List<LimsConsignmentInfo> consignmentInfoList = limsConsignmentInfoService.selectByCaseId(caseId);
            modelAndView.addObject("limsCaseInfo", limsCaseInfo);
            modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());

            LimsConsignmentInfo consignmentInfo = null;
            for(LimsConsignmentInfo limsConsignmentInfo : consignmentInfoList){
                if(limsConsignmentInfo.getAppendFlag().equals("0")){
                    consignmentInfo = limsConsignmentInfo;
                }
            }
            modelAndView.addObject("areaOrgCode", consignmentInfo == null ? "" : consignmentInfo.getAreaOrgCode());

            OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(consignmentInfo.getAcceptOrgId());
            //创建orgInfoList点击鉴定中心的选择，可以多选
            List<OrgInfo> orgInfos = new ArrayList<>();
            orgInfos.add(orgInfo);
            modelAndView.addObject("orgInfos",orgInfos);

            LimsConsignmentInfo limsConsignmentInfo = new LimsConsignmentInfo();
            limsConsignmentInfo.setConsignmentNo("委托书编号自动生成");

            //补送的时候委托人默认和主案件一样
            limsConsignmentInfo.setDelegator1Id(consignmentInfo.getDelegator1Id());
            limsConsignmentInfo.setDelegator2Id(consignmentInfo.getDelegator2Id());
            limsConsignmentInfo.setDelegator1Position(consignmentInfo.getDelegator1Position());
            limsConsignmentInfo.setDelegator2Position(consignmentInfo.getDelegator2Position());
            limsConsignmentInfo.setDelegator1PaperworkNo(consignmentInfo.getDelegator1PaperworkNo());
            limsConsignmentInfo.setDelegator2PaperworkNo(consignmentInfo.getDelegator2PaperworkNo());
            limsConsignmentInfo.setDelegator1PhoneNumber(consignmentInfo.getDelegator1PhoneNumber());
            limsConsignmentInfo.setDelegator2PhoneNumber(consignmentInfo.getDelegator2PhoneNumber());

            modelAndView.addObject("consignatioInfo",limsConsignmentInfo);
            session.setAttribute("user",operateUser);
            modelAndView.setViewName("caseQuery/replacementNonCase");
        } catch (Exception ex) {
            logger.error("添加补送页面案件信息查询失败："+ex);
        }
        return modelAndView;
    }


    /**
     * 查询添加补送的在逃人员案件案件委托记录并调到添加补送页面
     * @param request
     * @param response
     * @param caseId
     * @return
     */
    @RequestMapping("/replacementFugitivesCaseByCaseId")
    public ModelAndView replacementFugitivesCaseByCaseId(HttpServletRequest request, HttpServletResponse response, String caseId,HttpSession session) {
        ModelAndView modelAndView = initModelAndView();
        try {
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            operateUser.setStatusId("1");

            DictItem dictItem = new DictItem();
            //案件性质
            dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
            List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);
            modelAndView.addObject("casePropertyList",casePropertyList);

            //根据案件id查询案件信息
            LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
            List<LimsConsignmentInfo> consignmentInfoList = limsConsignmentInfoService.selectByCaseId(caseId);
            modelAndView.addObject("limsCaseInfo", limsCaseInfo);
            modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());

            LimsConsignmentInfo consignmentInfo = null;
            for(LimsConsignmentInfo limsConsignmentInfo : consignmentInfoList){
                if(limsConsignmentInfo.getAppendFlag().equals("0")){
                    consignmentInfo = limsConsignmentInfo;
                }
            }
            modelAndView.addObject("areaOrgCode", consignmentInfo == null ? "" : consignmentInfo.getAreaOrgCode());

            OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(consignmentInfo.getAcceptOrgId());
            //创建orgInfoList点击鉴定中心的选择，可以多选
            List<OrgInfo> orgInfos = new ArrayList<>();
            orgInfos.add(orgInfo);
            modelAndView.addObject("orgInfos",orgInfos);

            LimsConsignmentInfo limsConsignmentInfo = new LimsConsignmentInfo();
            limsConsignmentInfo.setConsignmentNo("委托书编号自动生成");

            //补送的时候委托人默认和主案件一样
            limsConsignmentInfo.setDelegator1Id(consignmentInfo.getDelegator1Id());
            limsConsignmentInfo.setDelegator2Id(consignmentInfo.getDelegator2Id());
            limsConsignmentInfo.setDelegator1Position(consignmentInfo.getDelegator1Position());
            limsConsignmentInfo.setDelegator2Position(consignmentInfo.getDelegator2Position());
            limsConsignmentInfo.setDelegator1PaperworkNo(consignmentInfo.getDelegator1PaperworkNo());
            limsConsignmentInfo.setDelegator2PaperworkNo(consignmentInfo.getDelegator2PaperworkNo());
            limsConsignmentInfo.setDelegator1PhoneNumber(consignmentInfo.getDelegator1PhoneNumber());
            limsConsignmentInfo.setDelegator2PhoneNumber(consignmentInfo.getDelegator2PhoneNumber());
            limsConsignmentInfo.setPreIdentifyDesc(consignmentInfo.getPreIdentifyDesc());
            limsConsignmentInfo.setReidentifyReason(consignmentInfo.getReidentifyReason());

            modelAndView.addObject("consignatioInfo",limsConsignmentInfo);
            session.setAttribute("user",operateUser);
            modelAndView.setViewName("caseQuery/replacementFugitivesCase");
        } catch (Exception ex) {
            logger.error("添加补送页面案件信息查询失败："+ex);
        }
        return modelAndView;
    }


    /**
     * 添加非案件委托补送信息
     * @param paramsData
     * @return
     */
    @RequestMapping(value="/submitNonCaseReplacement", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> submitNonCaseReplacement(@RequestParam(value = "paramsData")String paramsData) {
        Map<String,Object> map=new HashMap();
        String url="";
        try {
            JSONObject str = JSON.parseObject(paramsData);
            //委托信息
            LimsConsignmentInfo consignatioInfo = new LimsConsignmentInfo();
            //案件信息
            LimsCaseInfo caseInfoDna = new LimsCaseInfo();
            //被鉴定人信息已经对应的样本信息
            List<LimsPersonInfo> limsPersonInfoList = new ArrayList<>();
            //鉴定中心主键id
            String evaluationCenterId = "";
            //物证检材信息
            List<LimsSampleInfoDna> sampleInfoDnaList = new LinkedList<>();

            //解析前台传来的数据
            if(str.containsKey("evaluationCenter")){
                evaluationCenterId = str.get("evaluationCenter").toString();
            }
            if(str.containsKey("consignmentInfo")) {
                consignatioInfo = JSON.parseObject(str.get("consignmentInfo").toString(), LimsConsignmentInfo.class);
            }

            if(str.containsKey("caseInfoDna")) {
                caseInfoDna = JSON.parseObject(str.get("caseInfoDna").toString(), LimsCaseInfo.class);
            }

            if(str.containsKey("limsPersonInfoList")) {
                limsPersonInfoList = (List<LimsPersonInfo>) JSON.parseArray(str.get("limsPersonInfoList").toString(), LimsPersonInfo.class);
            }

            if(str.containsKey("sampleInfoDnaList")) {
                sampleInfoDnaList = (List<LimsSampleInfoDna>) JSON.parseArray(str.get("sampleInfoDnaList").toString(), LimsSampleInfoDna.class);
            }

            DelegateDataModel delegateDataModel = new DelegateDataModel();
            delegateDataModel.setCaseInfoDna(caseInfoDna);
            delegateDataModel.setLimsPersonInfoList(limsPersonInfoList);
            delegateDataModel.setConsignatioInfo(consignatioInfo);
            delegateDataModel.setSampleInfoDnaList(sampleInfoDnaList);

            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            if(operateUser == null){
                map.put("success",false);
                map.put(url,"/login.html?timeoutFlag=1");
                return  map;
            }

            //添加委托信息
            Map<String, String> result = limsCaseInfoService.submitNonCaseReplacement(delegateDataModel, operateUser,evaluationCenterId);
            map.put("caseId",result == null ? "" : (result.get("caseId")==null?"":result.get("caseId")));
            map.put("consignmentId",result == null ? "" : (result.get("consignmentId")==null?"":result.get("consignmentId")));
            map.put("success",true);
        }catch(Exception ex){
            map.put("error",false);
            logger.error("非案件委托补送信息保存失败"+ex.getMessage());
        }
        return map;
    }


    /**
     * 查询补送记录详情
     * @param request
     * @param consignmentId
     * @param caseId
     * @return
     */
    @RequestMapping("/queryNonCaseReplacementRecordDetail")
    public ModelAndView queryNonCaseReplacementRecordDetail(HttpServletRequest request, String consignmentId, String caseId) {
        ModelAndView modelAndView = initModelAndView();

        //根据委托id查询委托信息
        LimsConsignmentInfo consignatioInfo = limsConsignmentInfoService.selectByConsignmentId(consignmentId);
        //根据案件id查询案件信息
        LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
        //根据案件id查询人员信息
        LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
        limsPersonInfo.setConsignmentId(consignmentId);
        limsPersonInfo.setCaseId(caseId);
        List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
        //根据案件id查询样本信息
        LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
        sampleInfoDna.setCaseId(caseId);
        sampleInfoDna.setConsignmentId(consignmentId);
        List<LimsSampleInfoDna> sampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndRy(sampleInfoDna);
        if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
            for(LimsPersonInfo limsPersonInfo1:limsPersonInfoList){
                for(LimsSampleInfoDna sampleInfoDna2:sampleInfoDnaList){
                    if(limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())){
                        sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                    }
                }
            }
        }

        //根据案件id查询物证信息
        List<LimsSampleInfoDna> sampleInfoDnaList1 = limsSampleInfoDnaService.selectByCaseIdAndWz(sampleInfoDna);

        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgName(consignatioInfo.getDelegateOrgName());
        modelAndView.addObject("orgInfo", orgInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("consignatioInfo", consignatioInfo);
        modelAndView.addObject("limsCaseInfo", limsCaseInfo);
        modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
        modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
        modelAndView.addObject("sampleInfoDnaList", sampleInfoDnaList);
        modelAndView.addObject("limsSampleInfoList", sampleInfoDnaList1);
        modelAndView.setViewName("delegationReg/nonCaseReplacementRecordDetail");
        return modelAndView;
    }

    /**
     * 查询添加补送的案件记录并调到添加补送页面
     * @param request
     * @param response
     * @param caseId
     * @return
     */
    @RequestMapping("/replacementByCaseIdAndConsignmentId")
    public ModelAndView replacementByCaseIdAndConsignmentId(HttpServletRequest request, HttpServletResponse response, String caseId, String consignmentId) {
        ModelAndView modelAndView = initModelAndView();
        try {

            //根据案件id查询案件信息
            LimsCaseInfo limsCaseInfo = limsCaseInfoService.selectByCaseId(caseId);
            List<LimsConsignmentInfo> consignmentInfoList = limsConsignmentInfoService.selectByCaseId(caseId);
            modelAndView.addObject("limsCaseInfo", limsCaseInfo);
            modelAndView.addObject("xkNo", limsCaseInfo.getCaseXkNo());
            modelAndView.addObject("areaOrgCode", ListUtils.isNullOrEmptyList(consignmentInfoList) ? "" : consignmentInfoList.get(0).getAreaOrgCode());

            //根据委托id查询委托信息
            LimsConsignmentInfo consignatioInfo = limsConsignmentInfoService.selectByConsignmentId(consignmentId);
            //根据案件id查询人员信息
            LimsPersonInfo limsPersonInfo = new LimsPersonInfo();
            limsPersonInfo.setConsignmentId(consignmentId);
            limsPersonInfo.setCaseId(caseId);
            List<LimsPersonInfo> limsPersonInfoList = limsPersonInfoService.selectByCaseIdAndConsignmentId(limsPersonInfo);
            //根据案件id查询样本信息
            LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
            sampleInfoDna.setCaseId(caseId);
            sampleInfoDna.setConsignmentId(consignmentId);
            List<LimsSampleInfoDna> sampleInfoDnaList = limsSampleInfoDnaService.selectByCaseIdAndRy(sampleInfoDna);
            if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
                for(LimsPersonInfo limsPersonInfo1:limsPersonInfoList){
                    for(LimsSampleInfoDna sampleInfoDna2:sampleInfoDnaList){
                        if(limsPersonInfo1.getPersonId().equals(sampleInfoDna2.getLinkId())){
                            sampleInfoDna2.setPersonName(limsPersonInfo1.getPersonName());
                        }
                    }
                }
            }
            modelAndView.addObject("consignatioInfo", consignatioInfo);
            modelAndView.addObject("limsPersonInfoList", limsPersonInfoList);
            modelAndView.addObject("sampleInfoDnaList", sampleInfoDnaList);

            //根据现堪编号查询现堪数据
//            Map<String, Object> caseSampleInfoList = getXkCaseService.getCaseByXkNo(limsCaseInfo.getCaseXkNo());
            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            //根据现勘编号查询现勘数据
            XckyAddressInfo xckyAddressInfo = xckyAddressInfoService.selectByOrgId(operateUser.getOrgId());
            if(xckyAddressInfo == null){
                xckyAddressInfo = xckyAddressInfoService.selectDefault();
            }

            Map<String, Object> caseSampleInfoList = null;
            try {
                caseSampleInfoList = xckyWebService.getXckyInfoByKno(xckyAddressInfo, limsCaseInfo.getCaseXkNo());
            }catch(Exception ex){
                logger.error("根据现勘号调取案件信息时出现错误！", ex);
            }
            LimsCaseInfo limsCaseInfoXk = (LimsCaseInfo)caseSampleInfoList.get("limsCaseInfo");
            List<LimsSampleInfoDna> limsSampleInfoListXk = (List<LimsSampleInfoDna>)caseSampleInfoList.get("limsSampleInfoList");
            modelAndView.addObject("limsCaseInfoXk", limsCaseInfoXk);
            modelAndView.addObject("limsSampleInfoListXk", limsSampleInfoListXk);

            //根据案件id查询物证检材信息  并和现堪返回的物证信息作对比
            List<LimsSampleInfoDna> limsSampleInfoDnaListWz = new ArrayList<>();
            List<LimsSampleInfoDna> limsSampleInfoDnaList = limsSampleInfoDnaService.selectByCaseId(limsCaseInfo.getCaseId());

            List<String> l1=new ArrayList<>();
            Map<String,Integer> map=new HashMap<>();
            for(int i=0;i<limsSampleInfoListXk.size();i++){
                l1.add(limsSampleInfoListXk.get(i).getEvidenceNo());
                map.put(limsSampleInfoListXk.get(i).getEvidenceNo(),i);
            }
            List<String> l2=new ArrayList<>();
            for(int j=0;j<limsSampleInfoDnaList.size();j++){
                l2.add(limsSampleInfoDnaList.get(j).getEvidenceNo());
            }
            l1.removeAll(l2);
            for(int m=0;m<l1.size();m++){
                limsSampleInfoDnaListWz.add(limsSampleInfoListXk.get(map.get(l1.get(m))));
            }


            modelAndView.addObject("limsSampleInfoList", limsSampleInfoDnaListWz);
            modelAndView.setViewName("caseQuery/replacement");
        } catch (Exception ex) {
            logger.error("添加补送页面案件信息查询失败："+ex);
        }
        return modelAndView;
    }

    private LimsCaseInfoVo resetCaseInfoQuery (LimsCaseInfoVo query) {

        if (StringUtils.isBlank(query.getStatus())) {
            query.setStatus(null);
        }else {
            query.setStatus(query.getStatus());
        }

        if (StringUtils.isBlank(query.getEntity().getCaseName())) {
            query.getEntity().setCaseName(null);
        }else {
            query.getEntity().setCaseName(query.getEntity().getCaseName().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getCaseNo())) {
            query.getEntity().setCaseNo(null);
        }else {
            query.getEntity().setCaseNo(query.getEntity().getCaseNo().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getCaseXkNo())) {
            query.getEntity().setCaseXkNo(null);
        }else {
            query.getEntity().setCaseXkNo(query.getEntity().getCaseXkNo().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getCaseProperty())) {
            query.getEntity().setCaseProperty(null);
        }else {
            query.getEntity().setCaseProperty(query.getEntity().getCaseProperty().trim());
        }

        if (StringUtils.isBlank(query.getEntity().getCaseType())) {
            query.getEntity().setCaseType(null);
        }else {
            query.getEntity().setCaseType(query.getEntity().getCaseType().trim());
        }

        if (StringUtils.isBlank(query.getDelegator1Name())) {
            query.setDelegator1Name(null);
        }else {
            query.setDelegator1Name(query.getDelegator1Name().trim());
        }

        if (null == query.getDelegateEndDatetime()) {
            query.setDelegateEndDatetime(null);
        }else {
            query.setDelegateEndDatetime(query.getDelegateEndDatetime());
        }

        if (null == query.getDelegateStartDatetime()) {
            query.setDelegateStartDatetime(null);
        }else {
            query.setDelegateStartDatetime(query.getDelegateStartDatetime());
        }

//        if (StringUtils.isBlank(query.getEntity().getEntrustStatus())) {
//            query.getEntity().setEntrustStatus(null);
//        }else {
//            query.getEntity().setEntrustStatus(query.getEntity().getEntrustStatus());
//        }
//
//        if (StringUtils.isBlank(query.getXkStatus())) {
//            query.setXkStatus(null);
//        }else {
//            query.setXkStatus(query.getXkStatus());
//        }

        return query;
    }

    /**
     * 查询字典项
     * @return
     */
    private ModelAndView  initModelAndView () {
        ModelAndView modelAndView = new ModelAndView();
        /*  字典 */

        DictItem dictItem = new DictItem();
        //案件类型
        dictItem.setDictTypeCode(Constants.CASE_TYPE);
        List<DictItem> caseTypeList = DictUtil.getDictList(dictItem);

        //案件性质
        //List<DictItem> casePropertyList1 = new LinkedList<>();
        dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
        List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);
        /*for(DictItem dictItem1:casePropertyList){
            if(("06").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            }else if(("09").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            } else if(("10").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            } else if(("11").equals(dictItem1.getDictCode())) {
                casePropertyList1.add(dictItem1);
            }else  if(("19").equals(dictItem1.getDictCode())) {
                casePropertyList1.add(dictItem1);
            }else if(("20").equals(dictItem1.getDictCode())){
                casePropertyList1.add(dictItem1);
            }
        }*/

        //案件级别
        dictItem.setDictTypeCode(Constants.CASE_LEVEL);
        List<DictItem> caseLevelList = DictUtil.getDictList(dictItem);

        //人员类型
        dictItem.setDictTypeCode(Constants.PERSON_TYPE);
        List<DictItem> memberTypeList = DictUtil.getDictList(dictItem);

        //性别
        dictItem.setDictTypeCode(Constants.GENDER);
        List<DictItem> genderList = DictUtil.getDictList(dictItem);

        //检材类型
        dictItem.setDictTypeCode(Constants.SAMPLE_TYPE);
        List<DictItem> sampleTypeList = DictUtil.getDictList(dictItem);

        //包装方法PACK_METHOD
        dictItem.setDictTypeCode(Constants.PACK_METHOD);
        List<DictItem> packMethodList = DictUtil.getDictList(dictItem);

        //案件状态CASE_STATUS
        dictItem.setDictTypeCode(Constants.CASE_STATUS);
        List<DictItem> caseStatusList = DictUtil.getDictList(dictItem);

        dictItem.setDictTypeCode(Constants.POSITION_LIST);
        List<DictItem> positionList = DictUtil.getDictList(dictItem);

        //人员类型
        dictItem.setDictTypeCode(Constants.PERSON_TYPE);
        List<DictItem> personTypeList = DictUtil.getDictList(dictItem);

        //亲缘关系
        dictItem.setDictTypeCode(Constants.RELATION_TYPE);
        List<DictItem> relationTypeList = DictUtil.getDictList(dictItem);

        //提取方法
        dictItem.setDictTypeCode(Constants.EXTRACT_METHOD);
        List<DictItem> extractMethodList = DictUtil.getDictList(dictItem);

        //载体
        dictItem.setDictTypeCode(Constants.SAMPLE_CARRIER);
        List<DictItem> sampleCarrierList = DictUtil.getDictList(dictItem);

        modelAndView.addObject("caseTypeList", caseTypeList);
        modelAndView.addObject("casePropertyList", casePropertyList);
        modelAndView.addObject("caseLevelList", caseLevelList);
        modelAndView.addObject("memberTypeList", memberTypeList);
        modelAndView.addObject("sampleTypeList", sampleTypeList);
        modelAndView.addObject("genderList", genderList);
        modelAndView.addObject("packMethodList", packMethodList);
        modelAndView.addObject("caseStatusList", caseStatusList);
        modelAndView.addObject("positionList", positionList);
        modelAndView.addObject("personTypeList",personTypeList);
        modelAndView.addObject("relationTypeList",relationTypeList);
        modelAndView.addObject("extractMethodList",extractMethodList);
        modelAndView.addObject("sampleCarrierList",sampleCarrierList);


        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            modelAndView.setViewName("redirect:/login.html?timeoutFlag=1");
            return  modelAndView;
        }

        String orgId = StringUtils.isBlank(operateUser.getSubOrgId()) ? operateUser.getOrgId() : operateUser.getSubOrgId();
        //查询委托单位
        OrgInfo subOrgInfo = orgInfoService.selectByPrimaryKey(orgId);
        modelAndView.addObject("subOrgInfo",subOrgInfo);

        //获取委托人管理数据
        if(operateUser.getOrgId().contains("110023")){
            List<AmPersonalInfo> personalInfoList =amPersonalInfoService.queryAmPersonalInfoLIst("");
            System.out.println(personalInfoList);
            modelAndView.addObject("personalInfoList",personalInfoList);
        }else{
            List<AmPersonalInfo> personalInfoList =amPersonalInfoService.queryAmPersonalInfoLIst(operateUser.getOrgId());
            System.out.println(personalInfoList);
            modelAndView.addObject("personalInfoList",personalInfoList);
        }

        //分局
        List<OrgInfo> orgInfoList = null;
        if(StringUtils.isNotBlank(operateUser.getQueryFlag()) && operateUser.getQueryFlag().equals(Constants.QUERY_FLAG_1)){
            orgInfoList = orgInfoService.selectAll();
        }else{
            orgInfoList = orgInfoService.selectDelegateByParentsId(operateUser.getOrgId());
        }
        modelAndView.addObject("orgInfoList",orgInfoList);

        //查询分局单位
        OrgInfo orgInfo1 = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        modelAndView.addObject("orgInfo",orgInfo1);
        //查询法医中心
        OrgInfo forensicCenterorg = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        modelAndView.addObject("forensicCenterorg",forensicCenterorg);
        //创建orgInfoList点击鉴定中心的选择，可以多选
        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo1.getOrgQualification())){
            orgInfos.add(orgInfo1);
            orgInfos.add(forensicCenterorg);
        }
        modelAndView.addObject("orgInfos",orgInfos);

        return modelAndView;
    }
}
