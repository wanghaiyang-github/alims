package com.bazl.alims.utils;


import com.bazl.alims.dao.LimsCaseInfoMapper;
import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.po.LimsSampleInfoDna;
import com.bazl.alims.model.po.XckyAddressInfo;
import com.bazl.alims.service.LimsCaseInfoService;
import com.bazl.alims.service.OrgInfoService;
import com.bazl.alims.service.XckyAddressInfoService;
import com.bazl.alims.webservices.XckyWebService;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.logging.log4j.util.Strings;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejia on 2019/4/15.
 */

//@Component
public class ScheduledTask {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LimsCaseInfoMapper limsCaseInfoMapper;

    @Autowired
    LimsCaseInfoService limsCaseInfoService;

    @Autowired
    private OrgInfoService orgInfoService;

    @Autowired
    XckyAddressInfoService xckyAddressInfoService;

    @Autowired
    XckyWebService xckyWebService;

    /**
     * 每间隔2小时输出时间
     */
//    @Scheduled(fixedRate = 7200 * 1000)
    public void logTime() {


//        List<LimsCaseInfo> limsCaseInfo = limsCaseInfoMapper.selectAllConsignmentId();
        List<LimsCaseInfo> limsCaseInfo = null;

        Map<String, Object> caseSampleInfoList = null;

        if(limsCaseInfo.size()>0){
            for(int i =0; i< limsCaseInfo.size(); i++){
                    if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfo.get(i).getDelegateOrgCode())){
                        if (limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110230")!=-1){
                            //北京市公安局现场勘验系统
                            XckyAddressInfo xckyAddressInfoFy = xckyAddressInfoService.selectByOrgId("110230000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoFy, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，法医中心更新现勘A号失败！");
                                logger.error("定时任务，法医中心更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110101") !=-1){
                            //东城分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoDch = xckyAddressInfoService.selectByOrgId("110101000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoDch, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，东城分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，东城分局更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，东城分局更新现勘A号失败！");
                                logger.error("定时任务，东城分局更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110102") !=-1){
                            //西城分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoXch = xckyAddressInfoService.selectByOrgId("110102000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoXch, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，西城分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString()+ "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，西城分局更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，西城分局更新现勘A号失败！");
                                logger.error("定时任务，西城分局更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110105") !=-1){
                            //朝阳分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoChy = xckyAddressInfoService.selectByOrgId("110105000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoChy, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，朝阳分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，朝阳分局更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，朝阳分局更新现勘A号失败！");
                                logger.error("定时任务，朝阳分局更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110106") !=-1){
                            //丰台分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoFt = xckyAddressInfoService.selectByOrgId("110106000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoFt, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，丰台分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，丰台分局更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，丰台分局更新现勘A号失败！");
                                logger.error("定时任务，丰台分局更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110107") !=-1){
                            //石景山分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoSjs = xckyAddressInfoService.selectByOrgId("110107000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoSjs, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，石景山分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，石景山分局更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，石景山分局更新现勘A号失败！");
                                logger.error("定时任务，石景山分局更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110108") !=-1){
                            //海淀分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoHd = xckyAddressInfoService.selectByOrgId("110108000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoHd, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，海淀分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，海淀分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，海淀分局现更新现勘A号失败！");
                                logger.error("定时任务，海淀分局现更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110109") !=-1){
                            //门头沟分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoMtg = xckyAddressInfoService.selectByOrgId("110109000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoMtg, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，门头沟分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，门头沟分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，门头沟分局现更新现勘A号失败！");
                                logger.error("定时任务，门头沟分局现更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110111") !=-1){
                            //房山分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoFs = xckyAddressInfoService.selectByOrgId("110111000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoFs, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，房山分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，房山分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，房山分局现更新现勘A号失败！");
                                logger.error("定时任务，房山分局现更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110112") !=-1){
                            //通州分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoTzh = xckyAddressInfoService.selectByOrgId("110112000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoTzh, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，通州分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，通州分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，通州分局现更新现勘A号失败！");
                                logger.error("定时任务，通州分局现更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110113") !=-1){
                            //顺义分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoSy = xckyAddressInfoService.selectByOrgId("110113000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoSy, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，顺义分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，顺义分局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，顺义分局现更新现勘A号失败！");
                                logger.error("定时任务，顺义分局现更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110114") !=-1){
                            //昌平分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoChp = xckyAddressInfoService.selectByOrgId("110114000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoChp, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，昌平分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，昌平分局局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，昌平分局现更新现勘A号失败！");
                                logger.error("定时任务，昌平分局现更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110115") !=-1){
                            //大兴分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoDx = xckyAddressInfoService.selectByOrgId("110115000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoDx, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，大兴分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，大兴分局局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，大兴分局现更新现勘A号失败！");
                                logger.error("定时任务，大兴分局现更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110116") !=-1){
                            //怀柔分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoHr = xckyAddressInfoService.selectByOrgId("110116000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoHr, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，怀柔分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，怀柔分局局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，怀柔分局现更新现勘A号失败！");
                                logger.error("定时任务，怀柔分局现更新现勘A号失败！", ex);
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110117") !=-1){
                            //平谷分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoPg = xckyAddressInfoService.selectByOrgId("110117000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoPg, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，平谷分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，平谷分局局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，平谷分局现更新现勘A号失败！");
                                logger.error("定时任务，平谷分局现更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110228") !=-1){
                            //密云分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoMy = xckyAddressInfoService.selectByOrgId("110228000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoMy, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，密云分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，密云分局局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，密云分局现更新现勘A号失败！");
                                logger.error("定时任务，密云分局现更新现勘A号失败！", ex);
                            }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110229") !=-1){
                            //延庆分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoYq = xckyAddressInfoService.selectByOrgId("110229000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoYq, limsCaseInfo.get(i).getCaseXkNo());
                                if(caseSampleInfoList != null){
                                    LimsCaseInfo limsCaseInfoEntity = (LimsCaseInfo) caseSampleInfoList.get("limsCaseInfo");
                                    if(limsCaseInfoEntity != null){
                                        if(org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getCaseXkNo()) && org.apache.commons.lang3.StringUtils.isNotBlank(limsCaseInfoEntity.getXkAno())){
                                            limsCaseInfoService.updateCaseXAno(limsCaseInfoEntity.getCaseXkNo(),limsCaseInfoEntity.getXkAno(),limsCaseInfoEntity.getConsignationXkNo());
                                            System.out.println("定时任务，延庆分局更新现勘A号：" + limsCaseInfoEntity.getXkAno().toString() + "WT号:" + limsCaseInfoEntity.getConsignationXkNo().toString());
                                            logger.info("定时任务，延庆分局局现更新现勘A号：" + limsCaseInfoEntity.getXkAno());
                                        }
                                    }
                                }
                            }catch(Exception ex){
                                System.out.println("定时任务，延庆分局现更新现勘A号失败！");
                                logger.error("定时任务，延庆分局现更新现勘A号失败！", ex);
                            }
                        }
                    }
                }
            }
    }


    /**
     * 根据指定K号获取现场勘验系统中的相关信息
     *
     * @param xckyAddressInfo       调用的现勘地址信息
     * @param kno                     现勘案件K号
     * @return
     */
    public Map<String, Object> getXckyUpdateAno(XckyAddressInfo xckyAddressInfo, String kno) {
        String params = this.getWSParam(new String[]{xckyAddressInfo.getInvokerIpaddr(), "lims", kno});

        String invokeUrl = "http://" + xckyAddressInfo.getXckyAddress() + "/service/sceneDnaManager?wsdl";
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(invokeUrl);


        /**
        I*配置超时时间
         */
       //设置超时单位为毫秒
        HTTPConduit http = (HTTPConduit)client.getConduit();
        HTTPClientPolicy httpClientPolicy =  new  HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(10000);  //连接超时 10秒
        httpClientPolicy.setAllowChunking(false);    //取消块编码
        httpClientPolicy.setReceiveTimeout(30000);     //响应超时 30秒
        http.setClient(httpClientPolicy);

        Object[] objects = null;
        String resultStr = null;
        try {
            //命名空间http://service对应现勘wsdl接口中wsdl:definitions中的targetNamespace的值
            objects = client.invoke(new QName("http://service", "getSceneInvestigation"), params);
            resultStr = objects[0].toString();
        } catch (Exception ex) {
            logger.error("调用现勘接口异常！", ex);
            return null;
        }

//        String resultStr ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT><AUTHORADDRESS>14.112.37.69:9080/xckyservice</AUTHORADDRESS><AUTHORCODE>xcky</AUTHORCODE><CASE><CASE_NO>A1101057300002019010234</CASE_NO><K_NO>K1101050505002019010817</K_NO><J_NO></J_NO><AJBM></AJBM><CASE_TYPE>050230</CASE_TYPE><CASE_NAME> 盗窃保险柜案</CASE_NAME><SCENE_REGIONALISM>110105730000</SCENE_REGIONALISM><SCENE_PLACE><![CDATA[北京市朝阳区十八里店乡吕家营村京伟超市]]></SCENE_PLACE><OCCURRENCE_DATE>2019-01-28 23:00:00</OCCURRENCE_DATE><CASE_SUMMARY><![CDATA[北京市公安局朝阳分局刑侦支队技术队接到刑侦支队值班室值班员指派<指派方式>： 在该所管界内北京市朝阳区十八里店乡吕家营村京伟超市发生一起盗窃保险柜案，请速派人勘查现场。]]></CASE_SUMMARY><RESERVE1>XX市勘验系统</RESERVE1><EXTERNAL_CASE_NO>K1101050505002019010817</EXTERNAL_CASE_NO><CASE_PROPERTY>1</CASE_PROPERTY><ASSIGN_CONTENT>北京市公安局朝阳分局刑侦支队技术队接到刑侦支队值班室值班员指派<指派方式>： 在该所管界内北京市朝阳区十八里店乡吕家营村京伟超市发生一起盗窃保险柜案，请速派人勘查现场。</ASSIGN_CONTENT><WTBH>WT1101050505002019011164</WTBH></CASE><BIO_EVIDENCE_LIST><BIO_EVIDENCE><SERIAL_NO>1</SERIAL_NO><DESCRIPTION>北侧摄像头擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>北侧摄像头擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>北侧摄像头</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><SERIAL_NO>2</SERIAL_NO><DESCRIPTION>南侧摄像头擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>南侧摄像头擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>南侧摄像头</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><SERIAL_NO>3</SERIAL_NO><DESCRIPTION>金属杆擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>金属杆擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>金属杆</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><SERIAL_NO>4</SERIAL_NO><DESCRIPTION>南扇门外侧擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>南扇门外侧擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>南扇门外侧</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><SERIAL_NO>5</SERIAL_NO><DESCRIPTION>钥匙擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>钥匙擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>钥匙</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><SERIAL_NO>6</SERIAL_NO><DESCRIPTION>绿色挎包粘取物，粘取器粘取</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>绿色挎包粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>绿色挎包</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><SERIAL_NO>7</SERIAL_NO><DESCRIPTION>梯子擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>梯子擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>梯子</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><DESCRIPTION></DESCRIPTION><COLLECT_BY></COLLECT_BY><COLLECT_DATE>2019-01-29 13:22:53</COLLECT_DATE><EVIDENCE_NAME>姜天伟</EVIDENCE_NAME><SAMPLE_TYPE>2002</SAMPLE_TYPE><COLLECT_POS></COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>1</TYPE><PERSON_ID>8ef0a5c568409adb0168980f69950f32</PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><DESCRIPTION></DESCRIPTION><COLLECT_BY></COLLECT_BY><COLLECT_DATE>2019-01-29 13:23:31</COLLECT_DATE><EVIDENCE_NAME>周石全</EVIDENCE_NAME><SAMPLE_TYPE>2002</SAMPLE_TYPE><COLLECT_POS></COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>1</TYPE><PERSON_ID>8ef0a5c568409adb0168980ffc600f33</PERSON_ID></BIO_EVIDENCE></BIO_EVIDENCE_LIST><PERSON_LIST><PERSON><ID>8ef0a5c568409adb0168980f69950f32</ID><NAME>姜天伟</NAME><SEX>男</SEX><AGE></AGE><IDCARD>420619197404074974</IDCARD><ADDRESS>被害人</ADDRESS><PERSON_TYPE>04</PERSON_TYPE></PERSON><PERSON><ID>8ef0a5c568409adb0168980ffc600f33</ID><NAME>周石全</NAME><SEX>男</SEX><AGE></AGE><IDCARD>510622197209084819</IDCARD><ADDRESS>被害人</ADDRESS><PERSON_TYPE>04</PERSON_TYPE></PERSON></PERSON_LIST></ROOT>";

        //对<ASSIGN_CONTENT></ASSIGN_CONTENT>中的内容添加<![CDATA[ ]]处理
        if(resultStr.contains("<ASSIGN_CONTENT>") && resultStr.contains("</ASSIGN_CONTENT>")){
            String[] resultArr1 = resultStr.split("<ASSIGN_CONTENT>");
            resultStr = resultArr1[0]+"<ASSIGN_CONTENT><![CDATA["+resultArr1[1];
            String[] resultArr2 = resultStr.split("</ASSIGN_CONTENT>");
            resultStr = resultArr2[0]+"]]></ASSIGN_CONTENT>"+resultArr2[1];
        }

        try {
            Map<String, Object> caseSampleInfoList = parseStringToMap(resultStr);
            return caseSampleInfoList;
        }catch (Exception ex){
            logger.error("解析现勘接口返回数据异常！", ex);
            return null;
        }
    }

    public String getWSParam(String... par) {
        StringBuffer param = new StringBuffer("");
        param.append("<?xml  version=\"1.0\" encoding=\"UTF-8\"?>");
        param.append("<ROOT>");
        param.append("<AUTHORADDRESS>").append(par[0]).append("</AUTHORADDRESS>");
        param.append("<AUTHORCODE>").append(par[1]).append("</AUTHORCODE>");
        param.append("<K_NO>").append(par[2]).append("</K_NO>");
        param.append("</ROOT>");
        return param.toString();
    }

    private Map<String, Object> parseStringToMap(String result) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        //案件信息
        LimsCaseInfo limsCaseInfo = new LimsCaseInfo();
        //物证检材信息
        List<LimsSampleInfoDna> limsSampleInfoList = new ArrayList();

        //人员样本信息
        List<LimsSampleInfoDna> limsPersonSampleInfoList = new ArrayList();

        Document document = DocumentHelper.parseText(result);
        Element root = document.getRootElement();
        Element caseElement = root.element("CASE");
        //案件三版本编号
        Element caseNoAttr = caseElement.element("CASE_NO");
        if (caseNoAttr != null) {
            if(Strings.isNotBlank(caseNoAttr.getTextTrim())){
                limsCaseInfo.setXkAno(caseNoAttr.getTextTrim());
            }else {
                limsCaseInfo.setXkAno("无");
            }
        }

        //现堪编号
        Element kNoAttr = caseElement.element("K_NO");
        if (kNoAttr != null) {
            if(StringUtils.isNotBlank(kNoAttr.getTextTrim())){
                limsCaseInfo.setCaseXkNo(kNoAttr.getTextTrim());
            }
        }

        //现堪委托编号
        Element wtbhAttr = caseElement.element("WTBH");
        if (wtbhAttr != null) {
            if(Strings.isNotBlank(wtbhAttr.getTextTrim())){
                limsCaseInfo.setConsignationXkNo(wtbhAttr.getTextTrim());
            }else {
                limsCaseInfo.setConsignationXkNo("无");
            }
        }

        returnMap.put("limsCaseInfo", limsCaseInfo);
        returnMap.put("limsSampleInfoList", limsSampleInfoList);
        returnMap.put("limsPersonSampleInfoList", limsPersonSampleInfoList);

        return returnMap;
    }

}
