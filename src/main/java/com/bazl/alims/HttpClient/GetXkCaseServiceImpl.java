package com.bazl.alims.HttpClient;

import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.po.LimsSampleInfoDna;
import com.bazl.alims.service.DictItemService;
import com.bazl.alims.service.impl.BaseService;
import com.bazl.alims.utils.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Sun on 2019/1/22.
 */
@Service
public class GetXkCaseServiceImpl extends BaseService implements GetXkCaseService {

    @Autowired
    DictItemService dictItemService;

    @Value("${xkAdress}")
    private String xkAdress;

    /**
     * 根据现勘编号查询现勘数据
     * @param xkNo
     * @return
     */
    @Override
    public Map<String, Object> getCaseByXkNo(String xkNo) {
        //返回map
        Map<String, Object> returnMap = new HashMap<>();
        //案件信息
        LimsCaseInfo limsCaseInfo = new LimsCaseInfo();
        //物证检材信息
        List<LimsSampleInfoDna> limsSampleInfoList = new ArrayList();

        String result = "";
        try {
            HttpClient client = HttpClients.createDefault();
            // 要调用的接口方法
            String url = xkAdress+xkNo;
            HttpPost post = new HttpPost(url);
            try {
                StringEntity s = new StringEntity("");
                s.setContentEncoding("UTF-8");
                s.setContentType("application/json");
                post.setEntity(s);
                post.addHeader("content-type", "text/xml");
                HttpResponse res = client.execute(post);
                result = EntityUtils.toString(res.getEntity());
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Document document = DocumentHelper.parseText(result);
            Element root = document.getRootElement();
            Element caseElement = root.element("CASE");

            //委托现堪编号
            Element caseNoAttr = caseElement.element("WTBH");
            if (caseNoAttr != null) {
                limsCaseInfo.setConsignationXkNo(caseNoAttr.getTextTrim());
            }

            //现堪A号
            Element xkAnoAttr = caseElement.element("CASE_NO");
            if (xkAnoAttr != null) {
                limsCaseInfo.setXkAno(xkAnoAttr.getTextTrim());
            }


            //现堪编号
            Element kNoAttr = caseElement.element("K_NO");
            if (kNoAttr != null) {
                limsCaseInfo.setCaseXkNo(kNoAttr.getTextTrim());
            }
            //案件性质
            Element caseTypeAttr = caseElement.element("CASE_TYPE");
            if (caseTypeAttr != null) {
                limsCaseInfo.setCaseProperty(xkCaseTypeToLimsType(caseTypeAttr.getTextTrim()));
            }
            //案件名称
            Element caseNameAttr = caseElement.element("CASE_NAME");
            if (caseNameAttr != null) {
                limsCaseInfo.setCaseName(caseNameAttr.getTextTrim());
            }
            //案发地点行政编号
            Element sceneRegionalismAttr = caseElement.element("SCENE_REGIONALISM");
            if (sceneRegionalismAttr != null) {
                limsCaseInfo.setCaseLocation(sceneRegionalismAttr.getTextTrim());
            }
            //案发地点
            Element scenePlaceAttr = caseElement.element("SCENE_PLACE");
            if (scenePlaceAttr != null) {
                limsCaseInfo.setCaseLocation(scenePlaceAttr.getTextTrim());
            }

            //案发时间
            Element occurrenceDateAttr = caseElement.element("OCCURRENCE_DATE");
            if (occurrenceDateAttr != null) {
                String occurrenceDateStr = occurrenceDateAttr.getTextTrim();
                Date occurrenceDate = DateUtils.stringToDate(occurrenceDateStr, "yyyy-MM-dd");
                limsCaseInfo.setCaseDatetime(occurrenceDate);
            }
            //简要案情
            Element caseSummaryAttr = caseElement.element("CASE_SUMMARY");
            if (caseSummaryAttr != null) {
                limsCaseInfo.setCaseBrief(caseSummaryAttr.getTextTrim());
            }

            Element reserve1Attr = caseElement.element("RESERVE1");
            Element externalCaseNoAttr = caseElement.element("EXTERNAL_CASE_NO");

/*
            Element casePropertyAttr = caseElement.element("CASE_PROPERTY");
            if (casePropertyAttr != null) {
                limsCaseInfo.setCaseProperty(casePropertyAttr.getTextTrim());
            }
*/
            Element wtbhAttr = caseElement.element("WTBH");

            //检材
            Element bioEvidenceListElement = root.element("BIO_EVIDENCE_LIST");
            List bioEvidenceElementList = bioEvidenceListElement.elements("BIO_EVIDENCE");
            for (int i = 0; i < bioEvidenceElementList.size(); i++) {
                LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
                Element bioEvidenceElement = (Element)bioEvidenceElementList.get(i);

                Element wnoAttr = bioEvidenceElement.element("W_NO");
                if(wnoAttr != null){
                    sampleInfoDna.setEvidenceNo(wnoAttr.getTextTrim());
                }
                //检材描述
                Element descriptionAttr = bioEvidenceElement.element("DESCRIPTION");
                if(descriptionAttr != null){
                    sampleInfoDna.setSampleDesc(descriptionAttr.getTextTrim());
                }
                //提取人
                Element collectByAttr = bioEvidenceElement.element("COLLECT_BY");
                if (collectByAttr != null) {
                    sampleInfoDna.setExtractPerson(collectByAttr.getTextTrim());
                }
                //提取时间
                Element collectDateAttr = bioEvidenceElement.element("COLLECT_DATE");
                if (collectDateAttr != null) {
                    String collectDateStr = collectDateAttr.getTextTrim();
                    Date collectDate = DateUtils.stringToDate(collectDateStr, "yyyy-MM-dd");
                    sampleInfoDna.setExtractDatetime(collectDate);
                }
                //检材名称
                Element evidenceNameAttr = bioEvidenceElement.element("EVIDENCE_NAME");
                if (evidenceNameAttr != null) {
                    sampleInfoDna.setSampleName(evidenceNameAttr.getTextTrim());
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
                    } else {
                        sampleInfoDna.setSampleFlag("0");
                    }
                }
                Element personIdAttr = bioEvidenceElement.element("PERSON_ID");
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

                limsSampleInfoList.add(sampleInfoDna);

            }
            returnMap.put("limsCaseInfo", limsCaseInfo);
            returnMap.put("limsSampleInfoList", limsSampleInfoList);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("目前系统没有对应的勘验信息"+e);
//            throw new IllegalArgumentException("目标系统没有对应的勘验信息！");
            return null;
        }

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


    public String xkCaseTypeToLimsType(String caseType) {
        if (caseType == "") {
            return "09";
        }

        switch (caseType) {
            case "020703":
                return "10";
            case "040101":
                return "01";
            case "040103":
                return "02";
            case "040105":
                return "05";
            case "040113":
                return "11";
            case "050225":
                return "15";
            case "050248":
                return "03";
            case "900000":
                return "08";
        } return "09";
    }
}
