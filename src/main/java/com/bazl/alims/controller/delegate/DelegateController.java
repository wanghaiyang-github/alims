package com.bazl.alims.controller.delegate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bazl.alims.HttpClient.GetXkCaseService;
import com.bazl.alims.common.Constants;
import com.bazl.alims.controller.BaseController;
import com.bazl.alims.dao.LogRecordMapper;
import com.bazl.alims.dao.MobileNewsMapper;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.HjrkPersonInfo;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.bo.DelegateDataModel;
import com.bazl.alims.model.po.*;
import com.bazl.alims.model.vo.LimsCaseInfoVo;
import com.bazl.alims.service.*;
import com.bazl.alims.utils.*;
import com.bazl.alims.webservices.HjrkPersonInfoWebService;
import com.bazl.alims.webservices.XckyWebService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/delegate")
public class DelegateController extends BaseController {

    @Autowired
    AmPersonalInfoService amPersonalInfoService;

    @Autowired
    private DictItemService dictItemService;

    @Autowired
    private LimsConsignmentInfoService limsConsignmentInfoService;

    @Autowired
    private OrgInfoService orgInfoService;

    @Autowired
    private LimsCaseInfoService limsCaseInfoService;

    @Autowired
    private HjrkPersonInfoWebService hjrkPersonInfoWebService;

    @Autowired
    private GetXkCaseService getXkCaseService;

    @Autowired
    XckyAddressInfoService xckyAddressInfoService;
    @Autowired
    XckyWebService xckyWebService;
    @Autowired
    SeqNoGenerateService seqNoGenerateService;
    @Autowired
    MobileNewsMapper mobileNewsMapper;

    @Autowired
    LogRecordMapper logRecordMapper;
    @Autowired
    DelegateCenterConfigService delegateCenterConfigService;
    @Autowired
    LimsPersonInfoService   limsPersonInfoService;

    @Value("${Is_App_Inform}")
    private int isAppInform;
    @Value("${Is_App_Url}")
    private String AppUrl;


    /**
     * 案件委托
     * @return
     */
    @RequestMapping("/caseReg")
    public ModelAndView caseReg(){
        ModelAndView view = new ModelAndView();
        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        if(operateUser == null){
            view.addObject("date", "redirect:/login.html?timeoutFlag=1");
            return  view;
        }
        //查询分局单位
//        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //创建orgInfoList点击鉴定中心的选择，可以多选
        /*List<OrgInfo> orgInfos = null;
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            orgInfos = new ArrayList<>();
            OrgInfo forensicCenterorg = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
            view.addObject("forensicCenterorg",forensicCenterorg);
            orgInfos.add(orgInfo);
            orgInfos.add(forensicCenterorg);
        }
        if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfos = new ArrayList<>();
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
            OrgInfo forensicCenterorg = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
            view.addObject("forensicCenterorg",forensicCenterorg);
            orgInfos.add(orgInfo);
            orgInfos.add(forensicCenterorg);
        }*/
        String orgId = operateUser.getOrgId();
        String substring = orgId.substring(0, 6);
        List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);

        view.addObject("orgInfos",delegateCenterConfigs);
        view.setViewName("delegationReg/caseReg");
        return view;
    }

    /**
     * dna委托
     * @return
     */
    @RequestMapping(value = "/dnaReg.html", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView dnaReg(String xkNo, String orgId, HttpSession session){
        ModelAndView view = initModelAndView();
        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        operateUser.setStatusId("1");

        LimsConsignmentInfo limsConsignmentInfo = new LimsConsignmentInfo();
        //根据现勘编号查询现勘数据
        /*
            1、判断该现勘号是否已经获取；
            2、根据当前登录人所在单位获取对应的现勘接口信息；
            3、根据[2]中获取的现勘信息以及待调取信息的K号，获取数据；
            4、如果指定K号在现勘系统中不存在，则跳转到错误提示页面：提示现勘号不存在，让用户重新输入现勘号。
         */

        /**
         * TODO 根据现勘号判断当前单位是否已经进行过委托登记, 如果已经登记过则跳转到错误提示页面：该案件已登记，如有新增检材请进行补送登记。
         */
        List<LimsCaseInfo> regedCaseInfoList = limsCaseInfoService.selectByCaseXkNo(xkNo, orgId);
        if(ListUtils.isNotNullAndEmptyList(regedCaseInfoList)){
            //提示用户该现勘号已经委托，请进行补送登记。
            view.addObject("errMsg", xkNo + "对应案件已登记，如有新增检材请进行补送登记！");
            view.setViewName("delegationReg/dnaXkErr");
            return view;
        }


        OrgInfo xcOrg = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //如果登录人时派出所级别，则获取对应分局
//        if(orgInfo.getOrgLevel().equals(Constants.ORG_LEVEL_PAICHUSUO)){
//            xcOrg = orgInfoService.selectByPrimaryKey(orgInfo.getParentId());
//        }

        XckyAddressInfo xckyAddressInfo = xckyAddressInfoService.selectByOrgId(xcOrg.getOrgId());
        if(xckyAddressInfo == null){
            xckyAddressInfo = xckyAddressInfoService.selectDefault();
        }

        Map<String, Object> caseSampleInfoList = null;
        try {
            caseSampleInfoList = xckyWebService.getXckyInfoByKno(xckyAddressInfo, xkNo);
        }catch(Exception ex){
            logger.error("根据现勘号调取案件信息时出现错误！", ex);
        }

        if (caseSampleInfoList == null) {
            // 跳转到错误提示页面：提示现勘号不存在，让用户重新输入现勘号。
            view.addObject("errMsg", "现勘号" + xkNo + "不存在，请核实后重新获取！");
            view.setViewName("delegationReg/dnaXkErr");
            return view;
        }

        LimsCaseInfo limsCaseInfo = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
        List<LimsSampleInfoDna> limsSampleInfoList = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
        List<LimsSampleInfoDna> sampleInfoDnaList = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsPersonSampleInfoList");
        List<LimsPersonInfo> limsPersonInfoList = (List<LimsPersonInfo>) caseSampleInfoList.get("limsPersonInfoList");

        /*
         * edit by lizhihua  在调用现勘接口的函数中已进行排序
        //对物证检材进行排序
        if(null != limsSampleInfoList && limsSampleInfoList.size() > 0){
            System.out.println(limsSampleInfoList.size());
            Collections.sort(limsSampleInfoList , new Comparator<LimsSampleInfoDna>() {

                @Override
                public int compare(LimsSampleInfoDna l1, LimsSampleInfoDna l2) {
                    if(StringUtils.isNotBlank(l1.getEvidenceNo()) && StringUtils.isNotBlank(l2.getEvidenceNo())){
                        String evidenceNo1 = l1.getEvidenceNo().substring(8);
                        System.out.println(evidenceNo1);
                        String evidenceNo2 = l2.getEvidenceNo().substring(8);
                        System.out.println(evidenceNo2);
                        BigDecimal no1 = new BigDecimal(evidenceNo1);
                        BigDecimal no2 = new BigDecimal(evidenceNo2);
                        //相减
                        BigDecimal diff = no1.subtract(no2);
                        if (diff.intValue() > 0) {
                            return 1;
                        }else if (diff.intValue() < 0) {
                            return -1;
                        }

                    }
                    return 0; //相等为0
                }
            });
        }
        */

        //如果人员不为空，并且样本信息不为空时，如果人员的样本类型03 则为事主样本信息,否则为否
        if(null != limsPersonInfoList && limsPersonInfoList.size() > 0 && null != sampleInfoDnaList && sampleInfoDnaList.size() > 0) {
            for (LimsPersonInfo limsPersonInfo1 : limsPersonInfoList) {
                for (LimsSampleInfoDna sampleInfoDna2 : sampleInfoDnaList) {
                    if (limsPersonInfo1.getPersonName().equals(sampleInfoDna2.getPersonName())) {
                        if (limsPersonInfo1 != null && limsPersonInfo1.getPersonType().equals("03")) {
                            sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_TRUE);
                        } else {
                            sampleInfoDna2.setCoreVictimStats(Constants.CORE_VICTIM_STATS_FLASE);
                        }
                    }
                }
            }
        }

        if(limsConsignmentInfo.getConsignmentNo()==null){
            //view.addObject("consignmentNo","委托书编号自动生成");
            limsConsignmentInfo.setConsignmentNo("委托书编号自动生成");
            view.addObject("consignatioInfo",limsConsignmentInfo);
        }

        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //创建orgInfoList点击鉴定中心的选择，可以多选
//        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            //查询鉴定中心
            List<LimsCaseInfo>  caseInfoList1 = limsCaseInfoService.selectByCaseXkNo(xkNo, orgInfo.getOrgId());
            /*if (ListUtils.isNullOrEmptyList(caseInfoList1)){
                orgInfos.add(orgInfo);
            }*/
            List<LimsCaseInfo>  caseInfoList2 = limsCaseInfoService.selectByCaseXkNo(xkNo, Constants.FORENSIC_CENTER_ORG_ID);
            if(ListUtils.isNullOrEmptyList(caseInfoList2)){
                orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//                orgInfos.add(orgInfo);
            }
        }
        else if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
//            orgInfos.add(orgInfo);
            OrgInfo forensicCenterorg = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(forensicCenterorg);
        }
        else{
            //查询法医鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
//            orgInfos.add(orgInfo);
        }

        String orgId1 = operateUser.getOrgId();
        String substring = orgId1.substring(0, 6);
        List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);
        view.addObject("orgInfos",delegateCenterConfigs);

        view.addObject("checkedOrgId",orgId);
        view.addObject("orgInfo",orgInfo);
//        view.addObject("orgInfos",orgInfos);
        String caseXkNo = (String) session.getAttribute("caseXkNo");
        if (StringUtils.isBlank(xkNo) && StringUtils.isNotBlank(caseXkNo)) {
            xkNo = caseXkNo;
        }
        view.addObject("xkNo",xkNo);
        view.addObject("limsCaseInfo",limsCaseInfo);
        session.setAttribute("user", operateUser);
        view.addObject("limsSampleInfoList",limsSampleInfoList);
        view.addObject("sampleInfoDnaList",sampleInfoDnaList);
        view.addObject("limsPersonInfoList",limsPersonInfoList);
        view.addObject("orgId", orgId);

        view.setViewName("delegationReg/dnaReg");
        return view;
    }

    @RequestMapping(value="/validateXkNo", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> validateXkNo(HttpServletRequest request, HttpSession session,
                                            HttpServletResponse response, String caseXkNo, String orgId){
        Map<String, Object> result = new HashMap<String, Object>();
        //根据现勘编号查询现勘数据
        if(StringUtils.isBlank(orgId)){
            orgId = Constants.FORENSIC_CENTER_ORG_ID;
        }
        List<LimsCaseInfo>  caseInfoList = limsCaseInfoService.selectByCaseXkNo(caseXkNo, orgId);
        if(ListUtils.isNotNullAndEmptyList(caseInfoList)){
            result.put("success", false);
        }else{
            String msg= null;
            List<LimsConsignmentInfo>  limsConsignmentInfoLists =  limsConsignmentInfoService.selectByCaseXkNoTwo(caseXkNo);
            if(limsConsignmentInfoLists.size()>0){
                result.put("ifOrgId", limsConsignmentInfoLists.get(0).getAcceptOrgId());
            }
            result.put("success", true);
            session.setAttribute("caseXkNo", caseXkNo);
        }
        return result;
    }

    @RequestMapping(value="/refreshBtn", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String, Object> refreshBtn(HttpServletRequest request,
                                          HttpServletResponse response, String caseXkNo){
        Map<String, Object> result = new HashMap<String, Object>();

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
            caseSampleInfoList = xckyWebService.getXckyInfoByKno(xckyAddressInfo, caseXkNo);
        }catch(Exception ex){
            logger.error("根据现勘号调取案件信息时出现错误！", ex);
        }

        //Map<String, Object> caseSampleInfoList = getXkCaseService.getCaseByXkNo(caseXkNo);
        LimsCaseInfo limsCaseInfo = (LimsCaseInfo)caseSampleInfoList.get("limsCaseInfo");
        List<LimsSampleInfoDna> limsSampleInfoList = (List<LimsSampleInfoDna>)caseSampleInfoList.get("limsSampleInfoList");

        /*
         * edit by lizhihua  在调用现勘接口的函数中已进行排序
        //对物证检材进行排序
        if(null != limsSampleInfoList && limsSampleInfoList.size() > 0){
            System.out.println(limsSampleInfoList.size());
            Collections.sort(limsSampleInfoList , new Comparator<LimsSampleInfoDna>() {

                @Override
                public int compare(LimsSampleInfoDna l1, LimsSampleInfoDna l2) {
                    if(StringUtils.isNotBlank(l1.getEvidenceNo()) && StringUtils.isNotBlank(l2.getEvidenceNo())){
                        String evidenceNo1 = l1.getEvidenceNo().substring(8);
                        System.out.println(evidenceNo1);
                        String evidenceNo2 = l2.getEvidenceNo().substring(8);
                        System.out.println(evidenceNo2);
                        BigDecimal no1 = new BigDecimal(evidenceNo1);
                        BigDecimal no2 = new BigDecimal(evidenceNo2);
                        //相减
                        BigDecimal diff = no1.subtract(no2);
                        if (diff.intValue() > 0) {
                            return 1;
                        }else if (diff.intValue() < 0) {
                            return -1;
                        }

                    }
                    return 0; //相等为0
                }
            });
        }
        */

        result.put("success", limsSampleInfoList);
        result.put("date", limsSampleInfoList.size());
        return result;
    }

    /**
     * 专家会诊委托
     * @return
     */
    @RequestMapping("/consultationReg")
    public ModelAndView consultationReg(){
        ModelAndView view = new ModelAndView();
        view.setViewName("delegationReg/consultationReg");
        return view;
    }

    /**
     * 添加委托信息
     * @param paramsData
     * @return
     */
    @RequestMapping(value="/submitDelegate.html", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> submitDelegate(@RequestParam(value = "paramsData")String paramsData,HttpServletRequest request) {
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

            String orgname = "";
            String personIds = "";
            String sampleIdWzs = "";
            String sampleIds = "";
            String evaluationCenterId = "";

            //解析前台传来的数据
            if(str.containsKey("evaluationCenter")){
                evaluationCenterId = str.get("evaluationCenter").toString();
            }
            if(str.containsKey("personIds")) {
                personIds = str.get("personIds").toString();
            }
            if(str.containsKey("sampleIdWzs")) {
                sampleIdWzs = str.get("sampleIdWzs").toString();
            }
            if(str.containsKey("sampleIds")) {
                sampleIds = str.get("sampleIds").toString();
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
            if(str.containsKey("orgname")) {
                orgname = str.get("orgname").toString();
            }

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
            Map<String, String> result = limsConsignmentInfoService.submitDelegate(delegateDataModel, operateUser, personIds, sampleIdWzs, sampleIds, evaluationCenterId);
            map.put("success",true);
            map.put("caseId",result == null ? "" : (result.get("caseId")==null?"":result.get("caseId")));
            map.put("consignmentId",result == null ? "" : (result.get("consignmentId")==null?"":result.get("consignmentId")));

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
//                count.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
//                count.setState(0);
//                count.setCreateDatetime(date);
//                count.setUpdateDatetime(date);
//                count.setType(4);
//                count.setUserid(consignatioInfo.getDelegator1Id());
//                count.setUsername(consignatioInfo.getDelegator1Name());
//                count.setUserOrg(consignatioInfo.getDelegateOrgCode());
//                count.setCaseId(consignatioInfo.getCaseId());
//                count.setMessageType(1);
//                MobileNews count2 = new MobileNews();
//                count2.setId(UuidUtil.generateUUID());
//                count2.setTitle(caseInfoDna.getCaseName());
//                count2.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
//                count2.setState(0);
//                count2.setCreateDatetime(date);
//                count2.setUpdateDatetime(date);
//                count2.setType(4);
//                count2.setUserid(consignatioInfo.getDelegator2Id());
//                count2.setUsername(consignatioInfo.getDelegator2Name());
//                count2.setUserOrg(consignatioInfo.getDelegateOrgCode());
//                count2.setCaseId(consignatioInfo.getCaseId());
//                count2.setMessageType(1);
//                MobileNews count3 = new MobileNews();
//                count3.setId(UuidUtil.generateUUID());
//                count3.setTitle(caseInfoDna.getCaseName());
//                count3.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
//                count3.setState(0);
//                count3.setCreateDatetime(date);
//                count3.setUpdateDatetime(date);
//                count3.setType(4);
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
            logger.error("委托登记保存失败"+ex);
        }


        return map;
    }


    /**
     * 查询委托人信息
     * @param parentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryAmPersonalInfo.html")
    public AmPersonalInfo getCountyRegionListByParentIds(String parentId) {
        AmPersonalInfo amPersonalInfo = new AmPersonalInfo();
        try {
            if(StringUtils.isNotBlank(parentId)){
                //查询委托人信息
                amPersonalInfo = amPersonalInfoService.queryAmPersonalInfo(parentId);
            }
        }catch (Exception ex){
            logger.info("查询委托人信息失败"+ex);
        }
        return amPersonalInfo;
    }

    /**
     * 调取人口库信息
     * @param idcard
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryHjrkPerson")
    public Map<String, Object> queryHjrkPerson(String idcard) {
        Map<String, Object> result = new HashMap<>();
        try {
            if(StringUtils.isNotBlank(idcard)){
                //查询委托人信息
                List<HjrkPersonInfo> hjrkPersonInfos = hjrkPersonInfoWebService.selectListByPid2(idcard);
                result.put("hjrkPersonInfos", hjrkPersonInfos);
            }
        }catch (Exception ex){
            logger.info("查询委托人信息失败"+ex);
        }
        return result;
    }

    /**
     * dna非案件委托登记
     * @return
     */
    @RequestMapping(value = "/nonDnaReg", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ModelAndView nonDnaReg(HttpSession session){
        ModelAndView view = initModelAndView();

        DictItem dictItem = new DictItem();
        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        operateUser.setStatusId("1");
        //案件性质
        List<DictItem> casePropertyList1 = new LinkedList<>();
        dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
        List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);
        for(DictItem dictItem1:casePropertyList){
            if (operateUser.getLoginName().equals("jgj")){
                if (("11").equals(dictItem1.getDictCode())) {
                    casePropertyList1.add(dictItem1);
                }
            }else{
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
        }
        view.addObject("casePropertyList",casePropertyList1);

        LimsConsignmentInfo limsConsignmentInfo = new LimsConsignmentInfo();
        if(limsConsignmentInfo.getConsignmentNo()==null){
            limsConsignmentInfo.setConsignmentNo("委托书编号自动生成");
            view.addObject("consignatioInfo",limsConsignmentInfo);

        }

        String orgId = operateUser.getOrgId();
        String substring = orgId.substring(0, 6);
        List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);
        view.addObject("orgInfos",delegateCenterConfigs);
        view.addObject("checkedOrgId", orgId);

        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //创建orgInfoList点击鉴定中心的选择，可以多选
//        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            //查询鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        }
        else if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
            view.addObject("checkedOrgId", orgInfo.getOrgId());
//            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        }
        else{
            //查询法医鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        }
        view.addObject("orgInfo",orgInfo);
        session.setAttribute("user",operateUser);
        view.setViewName("delegationReg/nonDnaReg");
        return view;
    }


    /**
     * 在逃人员委托登记
     * @return
     */
    @RequestMapping(value = "/fugitivesReg")
    public ModelAndView fugitivesReg(HttpSession session){
        ModelAndView view = initModelAndView();

        DictItem dictItem = new DictItem();
        //获取当前登录人信息
        Subject subject = SecurityUtils.getSubject();
        LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
        operateUser.setStatusId("1");
        //案件性质
        dictItem.setDictTypeCode(Constants.CASE_PROPERTY);
        List<DictItem> casePropertyList = DictUtil.getDictList(dictItem);
        view.addObject("casePropertyList",casePropertyList);

        LimsConsignmentInfo limsConsignmentInfo = new LimsConsignmentInfo();
        if(limsConsignmentInfo.getConsignmentNo()==null){
            limsConsignmentInfo.setConsignmentNo("委托书编号自动生成");
            view.addObject("consignatioInfo",limsConsignmentInfo);

        }

        String orgId = operateUser.getOrgId();
        String substring = orgId.substring(0, 6);
        List<DelegateCenterConfig> delegateCenterConfigs = delegateCenterConfigService.selectQualification(substring);
        view.addObject("orgInfos",delegateCenterConfigs);
        view.addObject("checkedOrgId", orgId);

        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        //创建orgInfoList点击鉴定中心的选择，可以多选
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            //查询鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        }
        else if(orgInfo.getOrgId().indexOf("110024") != -1){
            orgInfo = orgInfoService.selectByPrimaryKey("110115000000");
            view.addObject("checkedOrgId", orgInfo.getOrgId());
        }
        else{
            //查询法医鉴定中心
            orgInfo = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        }
        view.addObject("orgInfo",orgInfo);
        session.setAttribute("user",operateUser);
        view.setViewName("delegationReg/fugitivesReg");
        return view;
    }


    /**
     * 添加非案件委托信息
     * @param paramsData
     * @return
     */
    @RequestMapping(value="/submitNonDelegate", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> submitNonDelegate(@RequestParam(value = "paramsData")String paramsData,HttpServletRequest request) {
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
            String orgname = "";
            String personIds = "";
            String sampleIds = "";
            String evaluationCenterId = "";
            String sampleIdWzs = "";
            //解析前台传来的数据
            if(str.containsKey("evaluationCenter")){
                evaluationCenterId = str.get("evaluationCenter").toString();
            }
            if(str.containsKey("personIds")) {
                personIds = str.get("personIds").toString();
            }
            if(str.containsKey("sampleIds")) {
                sampleIds = str.get("sampleIds").toString();
            }
            if(str.containsKey("sampleIdWzs")) {
                sampleIdWzs = str.get("sampleIdWzs").toString();
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
            if(str.containsKey("orgname")) {
                orgname = str.get("orgname").toString();
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
            Map<String, String> result = limsConsignmentInfoService.submitNonDelegate(delegateDataModel, operateUser, personIds, sampleIds, evaluationCenterId,sampleIdWzs);
            map.put("success",true);
            map.put("caseId",result == null ? "" : (result.get("caseId")==null?"":result.get("caseId")));
            map.put("consignmentId",result == null ? "" : (result.get("consignmentId")==null?"":result.get("consignmentId")));

            if (0 == isAppInform){ //1开启 0默认关闭
                String date = DateUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
                CallThirdpartyInfo bean = new CallThirdpartyInfo();
                //发起人信息
                bean.setUserid(operateUser.getUserId());
                bean.setUserName(operateUser.getLoginName());
                bean.setOrgid(operateUser.getOrgId());
                bean.setIp(IpAddressUtils.getIpAddr(request));
                //接收人
                List<String> phones = new ArrayList<>();
                if (null != consignatioInfo.getDelegator1PhoneNumber())
                    phones.add(consignatioInfo.getDelegator1PhoneNumber());
                if (null != consignatioInfo.getDelegator2PhoneNumber())
                    phones.add(consignatioInfo.getDelegator2PhoneNumber());
                bean.setPhones(phones);
                String countName = consignatioInfo.getDelegator1Name()+","+consignatioInfo.getDelegator2Name();
                ArrayList<MobileNews> mobileNewsList = new ArrayList<>();
                MobileNews count = new MobileNews();
                count.setId(UuidUtil.generateUUID());
                count.setTitle(caseInfoDna.getCaseName());
                count.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
                count.setState(0);
                count.setCreateDatetime(date);
                count.setUpdateDatetime(date);
                count.setType(4);
                count.setUserid(consignatioInfo.getDelegator1Id());
                count.setUsername(consignatioInfo.getDelegator1Name());
                MobileNews count2 = new MobileNews();
                count2.setId(UuidUtil.generateUUID());
                count2.setTitle(caseInfoDna.getCaseName());
                count2.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
                count2.setState(0);
                count2.setCreateDatetime(date);
                count2.setUpdateDatetime(date);
                count2.setType(4);
                count2.setUserid(consignatioInfo.getDelegator2Id());
                count2.setUsername(consignatioInfo.getDelegator2Name());
                mobileNewsList.add(count);
                mobileNewsList.add(count2);
                //信息存储到消息表
                for (MobileNews MobileNews: mobileNewsList) {
                    int insert = mobileNewsMapper.insert(MobileNews);
                    logger.info("===成功存储消息:"+insert+"条.");
                }
                bean.setContent(mobileNewsList);
                //第三方唤醒app
                LogRecordInfo logRecordInfo = CallThirdpartyTool.CallThirdpartyTool(AppUrl, bean);
                logger.info("===唤醒app结束开始存储日志");
                logRecordMapper.insert(logRecordInfo);

            }

        }catch(Exception ex){
            map.put("error",false);
            logger.error("非案件委托登记保存失败"+ex.getMessage());
        }
        return map;
    }

    /**
     * 添加在逃人员案件委托信息
     * @param paramsData
     * @param request
     * @return
     */
    @RequestMapping(value="/submitFugitivesDelegate", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> submitFugitivesDelegate(@RequestParam(value = "paramsData")String paramsData,HttpServletRequest request) {
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
            String orgname = "";
            String personIds = "";
            String sampleIds = "";
            String evaluationCenterId = "";
            String sampleIdWzs = "";
            //解析前台传来的数据
            if(str.containsKey("evaluationCenter")){
                evaluationCenterId = str.get("evaluationCenter").toString();
            }
            if(str.containsKey("personIds")) {
                personIds = str.get("personIds").toString();
            }
            if(str.containsKey("sampleIds")) {
                sampleIds = str.get("sampleIds").toString();
            }
            if(str.containsKey("sampleIdWzs")) {
                sampleIdWzs = str.get("sampleIdWzs").toString();
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
            if(str.containsKey("orgname")) {
                orgname = str.get("orgname").toString();
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
            Map<String, String> result = limsConsignmentInfoService.submitFugitivesDelegate(delegateDataModel, operateUser, personIds, sampleIds, evaluationCenterId,sampleIdWzs);
            map.put("success",true);
            map.put("caseId",result == null ? "" : (result.get("caseId")==null?"":result.get("caseId")));
            map.put("consignmentId",result == null ? "" : (result.get("consignmentId")==null?"":result.get("consignmentId")));

            /*if (0 == isAppInform){ //1开启 0默认关闭
                String date = DateUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss");
                CallThirdpartyInfo bean = new CallThirdpartyInfo();
                //发起人信息
                bean.setUserid(operateUser.getUserId());
                bean.setUserName(operateUser.getLoginName());
                bean.setOrgid(operateUser.getOrgId());
                bean.setIp(IpAddressUtils.getIpAddr(request));
                //接收人
                List<String> phones = new ArrayList<>();
                if (null != consignatioInfo.getDelegator1PhoneNumber())
                    phones.add(consignatioInfo.getDelegator1PhoneNumber());
                if (null != consignatioInfo.getDelegator2PhoneNumber())
                    phones.add(consignatioInfo.getDelegator2PhoneNumber());
                bean.setPhones(phones);
                String countName = consignatioInfo.getDelegator1Name()+","+consignatioInfo.getDelegator2Name();
                ArrayList<MobileNews> mobileNewsList = new ArrayList<>();
                MobileNews count = new MobileNews();
                count.setId(UuidUtil.generateUUID());
                count.setTitle(caseInfoDna.getCaseName());
                count.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
                count.setState(0);
                count.setCreateDatetime(date);
                count.setUpdateDatetime(date);
                count.setType(4);
                count.setUserid(consignatioInfo.getDelegator1Id());
                count.setUsername(consignatioInfo.getDelegator1Name());
                MobileNews count2 = new MobileNews();
                count2.setId(UuidUtil.generateUUID());
                count2.setTitle(caseInfoDna.getCaseName());
                count2.setContent(CallThirdpartyTool.Content(4,countName,consignatioInfo.getDelegateOrgName(),orgname));
                count2.setState(0);
                count2.setCreateDatetime(date);
                count2.setUpdateDatetime(date);
                count2.setType(4);
                count2.setUserid(consignatioInfo.getDelegator2Id());
                count2.setUsername(consignatioInfo.getDelegator2Name());
                mobileNewsList.add(count);
                mobileNewsList.add(count2);
                //信息存储到消息表
                for (MobileNews MobileNews: mobileNewsList) {
                    int insert = mobileNewsMapper.insert(MobileNews);
                    logger.info("===成功存储消息:"+insert+"条.");
                }
                bean.setContent(mobileNewsList);
                //第三方唤醒app
                LogRecordInfo logRecordInfo = CallThirdpartyTool.CallThirdpartyTool(AppUrl, bean);
                logger.info("===唤醒app结束开始存储日志");
                logRecordMapper.insert(logRecordInfo);

            }*/

        }catch(Exception ex){
            map.put("error",false);
            logger.error("在逃案件委托登记保存失败"+ex.getMessage());
        }
        return map;
    }

    /**
     * 添加非案件委托信息
     * @param consignmentNo
     * @return
     */
    @RequestMapping(value="/testConsignmentNo", method = RequestMethod.POST, produces="application/json; charset=utf-8")
    @ResponseBody
    public Map<String,Object> testConsignmentNo(String consignmentNo, String delegateOrgCode) {
        Map<String,Object> map=new HashMap();
        String url="";
        try {

            //获取当前登录人信息
            Subject subject = SecurityUtils.getSubject();
            LoaUserInfo operateUser = (LoaUserInfo) subject.getPrincipal();
            if(operateUser == null){
                map.put("success",false);
                map.put(url,"/login.html?timeoutFlag=1");
                return  map;
            }

            LimsConsignmentInfo limsConsignmentInfo = limsConsignmentInfoService.selectByConsignmentNo(consignmentNo, delegateOrgCode);
            if (limsConsignmentInfo != null){
                map.put("success",true);
            }else{
                map.put("success",false);
            }

        }catch(Exception ex){
            map.put("error",false);
            logger.error("判断委托编号重复失败"+ex.getMessage());
        }
        return map;
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

        String orgId = StringUtils.isBlank(operateUser.getSubOrgId()) ? operateUser.getOrgId() : operateUser.getSubOrgId();
        //查询委托单位
        OrgInfo subOrgInfo = orgInfoService.selectByPrimaryKey(orgId);
        modelAndView.addObject("subOrgInfo",subOrgInfo);
        //查询分局单位
        OrgInfo orgInfo = orgInfoService.selectByPrimaryKey(operateUser.getOrgId());
        modelAndView.addObject("orgInfo",orgInfo);
        //查询法医中心
        OrgInfo forensicCenterorg = orgInfoService.selectByPrimaryKey(Constants.FORENSIC_CENTER_ORG_ID);
        modelAndView.addObject("forensicCenterorg",forensicCenterorg);
        //创建orgInfoList点击鉴定中心的选择，可以多选
        List<OrgInfo> orgInfos = new ArrayList<>();
        if(StringUtils.isNotBlank(orgInfo.getOrgQualification())){
            orgInfos.add(orgInfo);
            orgInfos.add(forensicCenterorg);
        }
        modelAndView.addObject("orgInfos",orgInfos);
        //分局
        List<OrgInfo> orgInfoList = null;
        if(StringUtils.isNotBlank(operateUser.getQueryFlag()) && operateUser.getQueryFlag().equals(Constants.QUERY_FLAG_1)){
            orgInfoList = orgInfoService.selectAll();
        }else{
            orgInfoList = orgInfoService.selectDelegateByParentsId(operateUser.getOrgId());
        }
        modelAndView.addObject("orgInfoList",orgInfoList);

        //获取委托人管理数据
        List<AmPersonalInfo> personalInfoList =amPersonalInfoService.queryAmPersonalInfoLIst(operateUser.getOrgId());
        System.out.println(personalInfoList);
        modelAndView.addObject("personalInfoList",personalInfoList);

        return modelAndView;
    }
}
