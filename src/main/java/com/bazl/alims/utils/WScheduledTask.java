package com.bazl.alims.utils;


import com.bazl.alims.dao.LimsCaseInfoMapper;
import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.po.LimsSampleInfoDna;
import com.bazl.alims.model.po.XckyAddressInfo;
import com.bazl.alims.model.vo.LimsCaseInfoVo;
import com.bazl.alims.service.*;
import com.bazl.alims.webservices.XckyWebService;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.logging.log4j.util.Strings;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.xml.namespace.QName;
import java.util.*;

/**
 * Created by hejia on 2019/4/15.
 */

@Component
public class WScheduledTask {
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

    @Autowired
    DictItemService dictItemService;

    @Autowired
    LimsSampleInfoDnaService limsSampleInfoDnaService;



    /**
     * 每间隔15分钟输出时间
     */
//    @Scheduled(fixedRate = 100 * 1000)
    public void logTime() {


        logger.info("------ 更新W号开始 --------");
        boolean flag = true;
        Integer offset = 0;
        Integer rows = 10;
        while (flag) {
            logger.info("更新W号开始 offset:{}", offset);
            flag = xckyUpdateWno(offset, rows);
            offset++;
        }
        logger.info("更新W号结束 offset:{}", offset);

    }


    //更新W号方法
    public boolean xckyUpdateWno(Integer offset, Integer rows) {

        LimsCaseInfoVo query = new LimsCaseInfoVo();

        List<LimsCaseInfoVo> limsCaseInfo = limsCaseInfoService.selectAllConsignmentIdw(offset, rows);

        Map<String, Object> caseSampleInfoList = null;
        String flagTrue = null;

        if(limsCaseInfo.size() > 0){
            for(int i =0; i< limsCaseInfo.size(); i++){
                List<LimsSampleInfoDna> limsSampleInfoDnaList = limsSampleInfoDnaService.selectByCaseId(limsCaseInfo.get(i).getEntity().getCaseId());
                if(limsSampleInfoDnaList.size() > 0){
                    for(LimsSampleInfoDna limsSampleInfoDnaEntity : limsSampleInfoDnaList){
                        if(limsSampleInfoDnaEntity != null){
                            String evidenceNo = limsSampleInfoDnaEntity.getEvidenceNo();
                            if(StringUtils.isBlank(evidenceNo) || "".equals(evidenceNo)){
                                flagTrue = "1";
                                break;
                            }
                        }
                    }
                }
                if("1".equals(flagTrue)){
                    if(StringUtils.isNotBlank(limsCaseInfo.get(i).getDelegateOrgCode())){
                        if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110101") !=-1){
                            //东城分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoDch = xckyAddressInfoService.selectByOrgId("110101000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoDch, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("----定时任务，东城分局调取现勘数据失败！-----", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try{
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.info("-----东城分局获取现勘物证检材失败或无物证检材数据!---");
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        System.out.println("定时任务，东城分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("-----定时任务，东城分局更新现勘W号失败---");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110102") !=-1){
                            //西城分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoXch = xckyAddressInfoService.selectByOrgId("110102000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoXch, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("----定时任务，西城分局调取现勘数据失败！-----", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("----定时任务，西城分局解析物证检材数据失败或无物证检材数据！-----", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        System.out.println("定时任务，西城分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.error("----定时任务，西城分局更新现勘W号失败！-----", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110105") !=-1){
                            //朝阳分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoChy = xckyAddressInfoService.selectByOrgId("110105000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoChy, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("定时任务，朝阳分局调取现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                //更新物证检材编号 W号
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，朝阳分局解析现勘物证检材数据失败或无物证检材数据！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        System.out.println("--定时任务，朝阳分局更新现勘W号---" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("------定时任务，朝阳分局更新现勘W号失败-----");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110106") !=-1){
                            //丰台分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoFt = xckyAddressInfoService.selectByOrgId("110106000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoFt, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，丰台分局解析现勘数据失败！");
                                logger.error("定时任务，丰台分局解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，丰台分局获取物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，丰台分局更新现勘W号成功！" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("定时任务，丰台分局更新现勘W号失败!");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110107") !=-1){
                            //石景山分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoSjs = xckyAddressInfoService.selectByOrgId("110107000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoSjs, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，解析现勘数据失败！");
                                logger.error("定时任务，解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    System.out.println("定时任务，获取现勘物证检材数据失败！");
                                    logger.error("定时任务，获取现勘物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                    }catch(Exception ex){
                                                        System.out.println("-----定时任务，石景山分局更新现勘W号失败！----");
                                                        logger.info("-----定时任务，石景山分局更新现勘W号失败----");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110108") !=-1){
                            //海淀分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoHd = xckyAddressInfoService.selectByOrgId("110108000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoHd, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("----定时任务，海淀分局现解析海淀现勘数据失败！------", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("----定时任务，海淀分局获取现勘物证检材数据失败！------", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        System.out.println("定时任务，海淀分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                        logger.info("定时任务，海淀分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        System.out.println("定时任务，海淀分局更新现勘W号失败！" + evidenceNo + sampleNameXK + caseId);
                                                        logger.info("定时任务，海淀分局更新现勘W号失败！" + evidenceNo + sampleNameXK + caseId);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110109") !=-1){
                            //门头沟分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoMtg = xckyAddressInfoService.selectByOrgId("110109000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoMtg, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("-----定时任务，门头沟分局解析现勘数据失败！----", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("-----定时任务，门头沟分局获取物证检材数据失败！----", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        System.out.println("定时任务，门头沟分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                        logger.info("定时任务，门头沟分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("定时任务，门头沟分局更新现勘W号失败：" + evidenceNo + sampleNameXK + caseId);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110111") !=-1){
                            //房山分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoFs = xckyAddressInfoService.selectByOrgId("110111000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoFs, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("----定时任务，房山分局解析现勘数据失败！----");
                                logger.error("-----定时任务，房山分局解析现勘数据失败！--", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    System.out.println("----定时任务，房山分局获取物证检材数据失败！----");
                                    logger.error("-----定时任务，房山分局获取物证检材数据失败！--", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                    }catch(Exception ex){
                                                        System.out.println("----定时任务，房山分局更新W号失败！----");
                                                        logger.error("-----定时任务，房山分局更新W号失败！--", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110112") !=-1){
                            //通州分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoTzh = xckyAddressInfoService.selectByOrgId("110112000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoTzh, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，通州分局解析现勘数据失败！");
                                logger.error("定时任务，通州分局解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    System.out.println("定时任务，通州分局获取物证检材数据失败！");
                                    logger.error("定时任务，通州分局获取物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        System.out.println("定时任务，通州分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                        logger.info("定时任务，通州分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("定时任务，通州分局更新现勘W号失败：" + evidenceNo + sampleNameXK + caseId);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110113") !=-1){
                            //顺义分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoSy = xckyAddressInfoService.selectByOrgId("110113000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoSy, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，顺义分局解析现勘数据失败！");
                                logger.error("定时任务，顺义分局解析现勘数据失败！", ex);
                            }
                                if(caseSampleInfoList != null){
                                    //更新物证检材编号 W号
                                    List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                    try {
                                        limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                        logger.error("定时任务，顺义分局解析现勘物证数据！");
                                    }catch(Exception ex){
                                        System.out.println("定时任务，顺义分局解析现勘物证数据失败！");
                                        logger.error("定时任务，顺义分局解析现勘物证数据失败！", ex);
                                    }
                                    if(limsSampleInfoListXk.size()>0){
                                        for(int j=0;j < limsSampleInfoListXk.size();j++){
                                            if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                                LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                                String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                                String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                                String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                                limsSampleInfoDna.setSampleName(sampleNameXK);
                                                limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                                limsSampleInfoDna.setCaseId(caseId);
                                                LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                                if(limsSampleInfoDnaNew != null){
                                                    if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                        try {
                                                            limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                            logger.error("定时任务，顺义分局更新现勘W号！");
                                                        }catch(Exception ex){
                                                            logger.error("定时任务，顺义分局解析现勘物证数据失败！", ex);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110114") !=-1){
                            //昌平分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoChp = xckyAddressInfoService.selectByOrgId("110114000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoChp, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，昌平分局解析现勘数据失败！");
                                logger.error("定时任务，昌平分局解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，昌平分局获取物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，昌平分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.error("定时任务，昌平分局更新现勘W号失败！", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110115") !=-1){
                            //大兴分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoDx = xckyAddressInfoService.selectByOrgId("110115000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoDx, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("定时任务，大兴分局解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，大兴分局获取现勘物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，大兴分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.error("定时任务，大兴分局更新现勘W号失败！", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110116") !=-1){
                            //怀柔分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoHr = xckyAddressInfoService.selectByOrgId("110116000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoHr, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                logger.error("定时任务，怀柔分局解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，怀柔分局解析现勘数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，怀柔分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.error("定时任务，怀柔分局更新现勘数据失败！", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110117") !=-1){
                            //平谷分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoPg = xckyAddressInfoService.selectByOrgId("110117000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoPg, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，平谷分局现解析现勘数据失败！");
                                logger.error("定时任务，平谷分局现解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，平谷分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.error("定时任务，平谷分局更新现勘W号失败！", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110228") !=-1){
                            //密云分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoMy = xckyAddressInfoService.selectByOrgId("110228000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoMy, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，密云分局现更新现勘W号失败！");
                                logger.error("定时任务，密云分局现更新现勘W号失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号

                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();

                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，密云分局获取现勘物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，密云分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.error(evidenceNo + sampleNameXK + caseId);
                                                        logger.error("定时任务，密云分局更新现勘W号失败！", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else if(limsCaseInfo.get(i).getDelegateOrgCode().indexOf("110229") !=-1){
                            //延庆分局现场勘验系统
                            XckyAddressInfo xckyAddressInfoYq = xckyAddressInfoService.selectByOrgId("110229000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoYq, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，延庆分局解析现勘数据失败！");
                                logger.error("定时任务，延庆分局解析现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    logger.error("定时任务，延庆分局获取物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，延庆分局更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("定时任务，延庆分局更新现勘W号失败：" + evidenceNo + sampleNameXK + caseId);
                                                        logger.error("----定时任务，延庆分局获取物证检材数据失败！---", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }else{
                            //北京市公安局现场勘验系统
                            XckyAddressInfo xckyAddressInfoFy = xckyAddressInfoService.selectByOrgId("110230000000");
                            try {
                                caseSampleInfoList = getXckyUpdateAno(xckyAddressInfoFy, limsCaseInfo.get(i).getEntity().getCaseXkNo());
                            }catch(Exception ex){
                                System.out.println("定时任务，法医中心获取现勘数据失败！");
                                logger.error("定时任务，法医中心获取现勘数据失败！", ex);
                            }
                            if(caseSampleInfoList != null){
                                //更新物证检材编号 W号
                                List<LimsSampleInfoDna> limsSampleInfoListXk = new ArrayList<>();
                                try {
                                    limsSampleInfoListXk = (List<LimsSampleInfoDna>) caseSampleInfoList.get("limsSampleInfoList");
                                }catch(Exception ex){
                                    System.out.println("定时任务，法医中心解析现勘物证检材数据失败！");
                                    logger.error("定时任务，法医中心解析现勘物证检材数据失败！", ex);
                                }
                                if(limsSampleInfoListXk.size()>0){
                                    for(int j=0;j < limsSampleInfoListXk.size();j++){
                                        if(StringUtils.isNotEmpty(limsSampleInfoListXk.get(j).getEvidenceNo())){
                                            LimsSampleInfoDna limsSampleInfoDna = new LimsSampleInfoDna();
                                            String sampleNameXK = limsSampleInfoListXk.get(j).getSampleName();//现勘物证检材名车
                                            String evidenceNo = limsSampleInfoListXk.get(j).getEvidenceNo();//现勘物证检材编号W号
                                            String caseId = limsCaseInfo.get(i).getEntity().getCaseId();//案件id
                                            limsSampleInfoDna.setSampleName(sampleNameXK);
                                            limsSampleInfoDna.setEvidenceNo(evidenceNo);
                                            limsSampleInfoDna.setCaseId(caseId);
                                            LimsSampleInfoDna limsSampleInfoDnaNew = limsSampleInfoDnaService.selectBySampleName(caseId,sampleNameXK);
                                            if(limsSampleInfoDnaNew != null){
                                                if(StringUtils.isBlank(limsSampleInfoDnaNew.getEvidenceNo()) || " ".equals(limsSampleInfoDnaNew.getEvidenceNo())){
                                                    try {
                                                        limsSampleInfoDnaService.updateWno(limsSampleInfoDna);
                                                        logger.info("定时任务，法医中心更新现勘W号：" + evidenceNo + sampleNameXK + caseId);
                                                    }catch(Exception ex){
                                                        logger.info("定时任务，法医中心更新现勘W号失败！：" + evidenceNo + sampleNameXK + caseId);
                                                        logger.error("定时任务，法医中心解析现勘物证检材数据失败！", ex);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }

                }
            }
            return true;

        }else{
            return false;
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

//        String resultStr ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT><AUTHORADDRESS>10.11.234.201:9080/xckyservice</AUTHORADDRESS><AUTHORCODE>xcky</AUTHORCODE><CASE><CASE_NO></CASE_NO><K_NO>K1101080000002019060201</K_NO><J_NO></J_NO><AJBM></AJBM><CASE_TYPE>900000</CASE_TYPE><CASE_NAME> 其他类别案件</CASE_NAME><SCENE_REGIONALISM>110108490000</SCENE_REGIONALISM><SCENE_PLACE><![CDATA[北京市海淀区复兴路30号楼中门5层楼道间]]></SCENE_PLACE><OCCURRENCE_DATE>2019-06-15 08:00:00</OCCURRENCE_DATE><CASE_SUMMARY><![CDATA[2019年6月15日20时40分北京市海淀区复兴路30号楼中门5层楼道间发生一起其他类别案件，要求技术队勘查现场。]]></CASE_SUMMARY><RESERVE1>北京市海淀分局勘验系统</RESERVE1><EXTERNAL_CASE_NO>K1101080000002019060201</EXTERNAL_CASE_NO><CASE_PROPERTY>0</CASE_PROPERTY><ASSIGN_CONTENT>2019年6月15日20时40分北京市海淀区复兴路30号楼中门5层楼道间发生一起其他类别案件，要求技术队勘查现场。</ASSIGN_CONTENT><WTBH>WT1101080000002019060212</WTBH></CASE><BIO_EVIDENCE_LIST><BIO_EVIDENCE><W_NO>W11010800000020190602012013001</W_NO><SERIAL_NO>1</SERIAL_NO><DESCRIPTION>嫌疑人触摸</DESCRIPTION><COLLECT_BY>赵亮、宋春光、三班</COLLECT_BY><COLLECT_DATE>2019-06-15 00:00:00</COLLECT_DATE><EVIDENCE_NAME>木板正面上端粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2013</SAMPLE_TYPE><COLLECT_POS>木板正面上端</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010800000020190602012013002</W_NO><SERIAL_NO>2</SERIAL_NO><DESCRIPTION>嫌疑人触摸</DESCRIPTION><COLLECT_BY>赵亮、宋春光、三班</COLLECT_BY><COLLECT_DATE>2019-06-15 00:00:00</COLLECT_DATE><EVIDENCE_NAME>木板正面中端粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2013</SAMPLE_TYPE><COLLECT_POS>木板正面中端</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010800000020190602012013003</W_NO><SERIAL_NO>3</SERIAL_NO><DESCRIPTION>嫌疑人触摸</DESCRIPTION><COLLECT_BY>赵亮、宋春光、三班</COLLECT_BY><COLLECT_DATE>2019-06-15 00:00:00</COLLECT_DATE><EVIDENCE_NAME>木板正面下端粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2013</SAMPLE_TYPE><COLLECT_POS>木板正面下端</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010800000020190602012013004</W_NO><SERIAL_NO>4</SERIAL_NO><DESCRIPTION>嫌疑人触摸</DESCRIPTION><COLLECT_BY>赵亮、宋春光、三班</COLLECT_BY><COLLECT_DATE>2019-06-15 00:00:00</COLLECT_DATE><EVIDENCE_NAME>木板背面上端粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2013</SAMPLE_TYPE><COLLECT_POS>木板背面上端</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010800000020190602012013005</W_NO><SERIAL_NO>5</SERIAL_NO><DESCRIPTION>嫌疑人触摸</DESCRIPTION><COLLECT_BY>赵亮、宋春光、三班</COLLECT_BY><COLLECT_DATE>2019-06-15 00:00:00</COLLECT_DATE><EVIDENCE_NAME>木板背面中端粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2013</SAMPLE_TYPE><COLLECT_POS>木板背面中端</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO></W_NO><DESCRIPTION></DESCRIPTION><COLLECT_BY></COLLECT_BY><COLLECT_DATE>2019-06-16 15:02:49</COLLECT_DATE><EVIDENCE_NAME>马萌</EVIDENCE_NAME><SAMPLE_TYPE>2002</SAMPLE_TYPE><COLLECT_POS></COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG></WARN_MSG><TYPE>1</TYPE><PERSON_ID>8a8b6a496b46ae6e016b5f188180163a</PERSON_ID></BIO_EVIDENCE></BIO_EVIDENCE_LIST><PERSON_LIST><PERSON><ID>8a8b6a496b46ae6e016b5f188180163a</ID><NAME>马萌</NAME><SEX>男</SEX><AGE></AGE><IDCARD>110108198203053718</IDCARD><ADDRESS></ADDRESS><PERSON_TYPE>04</PERSON_TYPE></PERSON></PERSON_LIST></ROOT>";

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

        Element bioEvidenceListElement = root.element("BIO_EVIDENCE_LIST");
        List bioEvidenceElementList = bioEvidenceListElement.elements("BIO_EVIDENCE");
        if(null != bioEvidenceElementList && bioEvidenceElementList.size()>0){
            for (int i = 0; i < bioEvidenceElementList.size(); i++) {
                LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
                Element bioEvidenceElement = (Element) bioEvidenceElementList.get(i);

                Element wnoAttr = bioEvidenceElement.element("W_NO");
                if (wnoAttr != null) {
                    if(StringUtils.isNotBlank(wnoAttr.getTextTrim())){
                        sampleInfoDna.setEvidenceNo(wnoAttr.getTextTrim());
                    }
                }
                //检材描述
                Element descriptionAttr = bioEvidenceElement.element("DESCRIPTION");
                if (descriptionAttr != null) {
                    if(StringUtils.isNotBlank(descriptionAttr.getTextTrim())){
                        sampleInfoDna.setSampleDesc(descriptionAttr.getTextTrim());
                    }
                }
                //提取人
                Element collectByAttr = bioEvidenceElement.element("COLLECT_BY");
                if (collectByAttr != null) {
                    if(StringUtils.isNotBlank(collectByAttr.getTextTrim())){
                        sampleInfoDna.setExtractPerson(collectByAttr.getTextTrim());
                    }
                }
                //提取时间
                Element collectDateAttr = bioEvidenceElement.element("COLLECT_DATE");
                if (collectDateAttr != null) {
                    if(StringUtils.isNotBlank(collectDateAttr.getTextTrim())){
                        String collectDateStr = collectDateAttr.getTextTrim();
                        Date collectDate = DateUtils.stringToDate(collectDateStr, "yyyy-MM-dd");
                        sampleInfoDna.setExtractDatetime(collectDate);
                    }
                }
                //检材名称
                Element evidenceNameAttr = bioEvidenceElement.element("EVIDENCE_NAME");
                if (evidenceNameAttr != null) {
                    if(StringUtils.isNotBlank(evidenceNameAttr.getTextTrim())){
                        sampleInfoDna.setSampleName(evidenceNameAttr.getTextTrim());
                    }
                }
                //检材类型
                Element sampleTypeAttr = bioEvidenceElement.element("SAMPLE_TYPE");
                if ((sampleTypeAttr != null) && (!sampleTypeAttr.equals(""))) {
                    sampleInfoDna.setSampleType(xkTypeToLimsType(sampleTypeAttr.getTextTrim()));
                    List<DictItem> sampleTypeList = dictItemService.selectListByDictTypeCode("SAMPLE_TYPE");
                    for (DictItem dictItem : sampleTypeList) {
                        if (sampleInfoDna.getSampleType().equals(dictItem.getDictCode())) {
                            sampleInfoDna.setSampleTypeName(dictItem.getDictName());
                        }
                    }
                }
                Element collectPosAttr = bioEvidenceElement.element("COLLECT_POS");
                System.out.print(collectPosAttr);
                Element testDescAttr = bioEvidenceElement.element("TEST_DESC");
                System.out.print(testDescAttr);
                Element warnAttr = bioEvidenceElement.element("WARN_MSG");
                System.out.print(warnAttr);
                //检材类别
                Element flagAttr = bioEvidenceElement.element("TYPE");
                if (flagAttr != null) {
                    String flag = flagAttr.getTextTrim();
                    if (flag.equals("1")) {
                        sampleInfoDna.setSampleFlag("1");
                        if (evidenceNameAttr != null) {
                            sampleInfoDna.setPersonName(evidenceNameAttr.getTextTrim());
                        }
                    } else {
                        sampleInfoDna.setSampleFlag("0");
                    }
                }
                Element personIdAttr = bioEvidenceElement.element("PERSON_ID");
                if (personIdAttr != null) {
                    if(StringUtils.isNotBlank(personIdAttr.getTextTrim())){
                        sampleInfoDna.setLinkId(personIdAttr.getTextTrim());
                    }
                }
                Element relationAttr;
                if (personIdAttr != null) {
                    relationAttr = bioEvidenceElement.element("SAMPLE_RELATION");
                }

                Element acceptStatus = bioEvidenceElement.element("IF_SJ");
                Element sceneAddressAttr = root.element("AUTHORADDRESS");

                //以下为默认
                sampleInfoDna.setExtractMethod("01");
                sampleInfoDna.setExtractMethodName("擦");
                sampleInfoDna.setSamplePacking("01");
                sampleInfoDna.setSamplePackingName("纸袋");
                sampleInfoDna.setSamplePurpose("DNA检验");
                sampleInfoDna.setSampleCarrier("01");
                if (flagAttr != null) {
                    if(StringUtils.isNotBlank(flagAttr.getTextTrim())){
                        if (flagAttr.getTextTrim().equals("1")) {
                            sampleInfoDna.setSampleFlag("1");
                            sampleInfoDna.setSampleName(sampleInfoDna.getSampleName()+"血样");
                            limsPersonSampleInfoList.add(sampleInfoDna);
                        } else {
                            sampleInfoDna.setSampleFlag("0");
                            limsSampleInfoList.add(sampleInfoDna);
                        }
                    }
                }
                else {
                    sampleInfoDna.setSampleFlag("0");
                    limsSampleInfoList.add(sampleInfoDna);
                }
            }
        }




        returnMap.put("limsCaseInfo", limsCaseInfo);
        returnMap.put("limsSampleInfoList", limsSampleInfoList);
        returnMap.put("limsPersonSampleInfoList", limsPersonSampleInfoList);

        return returnMap;
    }




    public String xkTypeToLimsType(String bioEvidenceType) {
        if (bioEvidenceType == "") {
            return "99";
        }
        int bioEvidenctTypeInt = 0;
        try {
            bioEvidenctTypeInt = Integer.parseInt(bioEvidenceType);
        } catch (Exception e) {
            return "99";
        }
        switch (bioEvidenctTypeInt) {
            case 2002:
                return "01";
            case 2003:
                return "09";
            case 2004:
                return "02";
            case 2005:
                return "04";
            case 2011:
                return "07";
            case 2013:
                return "03";
            case 2014:
                return "03";
            case 2006:
            case 2007:
            case 2008:
            case 2009:
            case 2010:
            case 2012: } return "99";
    }

}
