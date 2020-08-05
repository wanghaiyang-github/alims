package com.bazl.alims.webservices;

import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.po.LimsCaseInfo;
import com.bazl.alims.model.po.LimsPersonInfo;
import com.bazl.alims.model.po.LimsSampleInfoDna;
import com.bazl.alims.model.po.XckyAddressInfo;
import com.bazl.alims.service.DictItemService;
import com.bazl.alims.utils.DateUtils;
import javax.xml.namespace.QName;
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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2019/1/27.
 */
@Service
public class XckyWebServiceImpl implements XckyWebService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DictItemService dictItemService;

    /**
     * 根据指定K号获取现场勘验系统中的相关信息
     *
     * @param xckyAddressInfo       调用的现勘地址信息
     * @param kno                     现勘案件K号
     * @return
     */
    @Override
    public Map<String, Object> getXckyInfoByKno(XckyAddressInfo xckyAddressInfo, String kno) {
        String params = this.getWSParam(new String[]{xckyAddressInfo.getInvokerIpaddr(), "lims", kno});

        String invokeUrl = "http://" + xckyAddressInfo.getXckyAddress() + "/service/sceneDnaManager?wsdl";
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(invokeUrl);

        HTTPConduit http = (HTTPConduit)client.getConduit();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(10000L);
        httpClientPolicy.setAllowChunking(false);
        httpClientPolicy.setReceiveTimeout(30000L);
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
        /*
        String resultStr ="<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT><AUTHORADDRESS>14.112.37.69:9080/xckyservice</AUTHORADDRESS>" +
                "<AUTHORCODE>xcky</AUTHORCODE><CASE><CASE_NO></CASE_NO><K_NO>K1101050505002019010817</K_NO>" +
                "<J_NO></J_NO><AJBM></AJBM><CASE_TYPE>050230</CASE_TYPE>" +
                "<CASE_NAME> 盗窃保险柜案</CASE_NAME><SCENE_REGIONALISM>110105730000</SCENE_REGIONALISM>" +
                "<SCENE_PLACE><![CDATA[北京市朝阳区十八里店乡吕家营村京伟超市]]></SCENE_PLACE><OCCURRENCE_DATE>2019-01-28 23:00:00</OCCURRENCE_DATE>" +
                "<CASE_SUMMARY><![CDATA[北京市公安局朝阳分局刑侦支队技术队接到刑侦支队值班室值班员指派<指派方式>： 在该所管界内北京市朝阳区十八里店乡吕家营村京伟超市发生一起盗窃保险柜案，请速派人勘查现场。]]></CASE_SUMMARY>" +
                "<RESERVE1>XX市勘验系统</RESERVE1><EXTERNAL_CASE_NO>K1101050505002019010817</EXTERNAL_CASE_NO><CASE_PROPERTY>1</CASE_PROPERTY>" +
                "<ASSIGN_CONTENT>北京市公安局朝阳分局刑侦支队技术队接到刑侦支队值班室值班员指派<指派方式>： 在该所管界内北京市朝阳区十八里店乡吕家营村京伟超市发生一起盗窃保险柜案，请速派人勘查现场。</ASSIGN_CONTENT><WTBH></WTBH></CASE>" +
                "<BIO_EVIDENCE_LIST>" +
                "<BIO_EVIDENCE>" +
                "   <W_NO>W11010505050020190108172014001</W_NO>" +
                "   <SERIAL_NO>1</SERIAL_NO>" +
                "   <DESCRIPTION>北侧摄像头擦拭物，棉签擦拭</DESCRIPTION>" +
                "   <COLLECT_BY>闫志鹏、张涛</COLLECT_BY>" +
                "   <COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE>" +
                "   <EVIDENCE_NAME>北侧摄像头擦拭物</EVIDENCE_NAME>" +
                "   <SAMPLE_TYPE>2014</SAMPLE_TYPE>" +
                "   <COLLECT_POS>北侧摄像头</COLLECT_POS>" +
                "   <IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC>" +
                "   <TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG>" +
                "   <TYPE>0</TYPE><PERSON_ID></PERSON_ID>" +
                "</BIO_EVIDENCE>" +
                "<BIO_EVIDENCE>" +
                "   <W_NO>W11010505050020190108172014002</W_NO>" +
                "   <SERIAL_NO>2</SERIAL_NO>" +
                "   <DESCRIPTION>南侧摄像头擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>南侧摄像头擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>南侧摄像头</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010505050020190108172014003</W_NO><SERIAL_NO>3</SERIAL_NO><DESCRIPTION>金属杆擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>金属杆擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>金属杆</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010505050020190108172014004</W_NO><SERIAL_NO>4</SERIAL_NO><DESCRIPTION>南扇门外侧擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>南扇门外侧擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>南扇门外侧</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010505050020190108172014005</W_NO><SERIAL_NO>5</SERIAL_NO><DESCRIPTION>钥匙擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>钥匙擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>钥匙</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010505050020190108172014006</W_NO><SERIAL_NO>6</SERIAL_NO><DESCRIPTION>绿色挎包粘取物，粘取器粘取</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>绿色挎包粘取物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>绿色挎包</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO>W11010505050020190108172014007</W_NO><SERIAL_NO>7</SERIAL_NO><DESCRIPTION>梯子擦拭物，棉签擦拭</DESCRIPTION><COLLECT_BY>闫志鹏、张涛</COLLECT_BY><COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE><EVIDENCE_NAME>梯子擦拭物</EVIDENCE_NAME><SAMPLE_TYPE>2014</SAMPLE_TYPE><COLLECT_POS>梯子</COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>0</TYPE><PERSON_ID></PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO></W_NO><DESCRIPTION></DESCRIPTION><COLLECT_BY></COLLECT_BY><COLLECT_DATE>2019-01-29 13:22:53</COLLECT_DATE><EVIDENCE_NAME>姜天伟</EVIDENCE_NAME><SAMPLE_TYPE>2002</SAMPLE_TYPE><COLLECT_POS></COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>1</TYPE><PERSON_ID>8ef0a5c568409adb0168980f69950f32</PERSON_ID></BIO_EVIDENCE><BIO_EVIDENCE><W_NO></W_NO><DESCRIPTION></DESCRIPTION><COLLECT_BY></COLLECT_BY><COLLECT_DATE>2019-01-29 13:23:31</COLLECT_DATE><EVIDENCE_NAME>周石全</EVIDENCE_NAME><SAMPLE_TYPE>2002</SAMPLE_TYPE><COLLECT_POS></COLLECT_POS><IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC><TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG><TYPE>1</TYPE><PERSON_ID>8ef0a5c568409adb0168980ffc600f33</PERSON_ID></BIO_EVIDENCE>\t\t  <BIO_EVIDENCE>\n" +
                "            <W_NO></W_NO>\n" +
                "            <DESCRIPTION></DESCRIPTION>\n" +
                "            <COLLECT_BY></COLLECT_BY>\n" +
                "            <COLLECT_DATE>2019-01-29 13:22:53</COLLECT_DATE>\n" +
                "            <EVIDENCE_NAME>姜天伟2</EVIDENCE_NAME>\n" +
                "            <SAMPLE_TYPE>2002</SAMPLE_TYPE>\n" +
                "            <COLLECT_POS></COLLECT_POS>\n" +
                "            <IF_SJ>0</IF_SJ>\n" +
                "            <SAMPLE_DESC></SAMPLE_DESC>\n" +
                "            <TEST_DESC>无</TEST_DESC>\n" +
                "            <WARN_MSG>DNA</WARN_MSG>\n" +
                "            <TYPE>1</TYPE>\n" +
                "            <PERSON_ID>8ef0a5c568409adb0168980f69950f321</PERSON_ID>\n" +
                "        </BIO_EVIDENCE></BIO_EVIDENCE_LIST><PERSON_LIST><PERSON><ID>8ef0a5c568409adb0168980f69950f32</ID><NAME>姜天伟</NAME><SEX>男</SEX><AGE></AGE><IDCARD>420619197404074974</IDCARD><ADDRESS>被害人</ADDRESS><PERSON_TYPE>04</PERSON_TYPE></PERSON><PERSON><ID>8ef0a5c568409adb0168980ffc600f33</ID><NAME>周石全</NAME><SEX>男</SEX><AGE></AGE><IDCARD>510622197209084819</IDCARD><ADDRESS>被害人</ADDRESS><PERSON_TYPE>04</PERSON_TYPE></PERSON><PERSON>\n" +
                "            <ID>8ef0a5c568409adb0168980f69950f321</ID>\n" +
                "            <NAME>姜天伟2</NAME>\n" +
                "            <SEX>男</SEX>\n" +
                "            <AGE></AGE>\n" +
                "            <IDCARD>420619197404074974</IDCARD>\n" +
                "            <ADDRESS>的手机</ADDRESS>\n" +
                "            <PERSON_TYPE>05</PERSON_TYPE>\n" +
                "        </PERSON></PERSON_LIST></ROOT>";
        */

        //对<ASSIGN_CONTENT></ASSIGN_CONTENT>中的内容添加<![CDATA[ ]]处理
        if(resultStr.contains("<ASSIGN_CONTENT>") && resultStr.contains("</ASSIGN_CONTENT>")){
            String[] resultArr1 = resultStr.split("<ASSIGN_CONTENT>");
            resultStr = resultArr1[0]+"<ASSIGN_CONTENT><![CDATA["+resultArr1[1];
            String[] resultArr2 = resultStr.split("</ASSIGN_CONTENT>");
            resultStr = resultArr2[0]+"]]></ASSIGN_CONTENT>"+resultArr2[1];
        }

        try {
            logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&现勘编号[" + kno + "] 接口返回数据为： " + resultStr);

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
        //案件性质
        Element caseTypeAttr = caseElement.element("CASE_TYPE");
        if (caseTypeAttr != null) {
            if(StringUtils.isNotBlank(xkCaseTypeToLimsType(caseTypeAttr.getTextTrim()))){
                limsCaseInfo.setCaseProperty(xkCaseTypeToLimsType(caseTypeAttr.getTextTrim()));
            }
        }
        //案件名称
        Element caseNameAttr = caseElement.element("CASE_NAME");
        if (caseNameAttr != null) {
            if(StringUtils.isNotBlank(caseNameAttr.getTextTrim())){
                limsCaseInfo.setCaseName(caseNameAttr.getTextTrim());
            }
        }
        //案发地点行政编号
        Element sceneRegionalismAttr = caseElement.element("SCENE_REGIONALISM");
        if (sceneRegionalismAttr != null) {
            if(StringUtils.isNotBlank(sceneRegionalismAttr.getTextTrim())){
                limsCaseInfo.setCaseLocation(sceneRegionalismAttr.getTextTrim());
            }
        }
        //案发地点
        Element scenePlaceAttr = caseElement.element("SCENE_PLACE");
        if (scenePlaceAttr != null) {
            if(StringUtils.isNotBlank(scenePlaceAttr.getTextTrim())){
                limsCaseInfo.setCaseLocation(scenePlaceAttr.getTextTrim());
            }
        }

        //案发时间
        Element occurrenceDateAttr = caseElement.element("OCCURRENCE_DATE");
        if (occurrenceDateAttr != null) {
            if(StringUtils.isNotBlank(occurrenceDateAttr.getTextTrim())){
                String occurrenceDateStr = occurrenceDateAttr.getTextTrim();
                Date occurrenceDate = DateUtils.stringToDate(occurrenceDateStr, "yyyy-MM-dd");
                limsCaseInfo.setCaseDatetime(occurrenceDate);
            }
        }
        //简要案情
        Element caseSummaryAttr = caseElement.element("ASSIGN_CONTENT");
        if (caseSummaryAttr != null) {
            if(StringUtils.isNotBlank(caseSummaryAttr.getTextTrim())){
                limsCaseInfo.setCaseBrief(caseSummaryAttr.getTextTrim());
            }
        }

        Element reserve1Attr = caseElement.element("RESERVE1");
        Element externalCaseNoAttr = caseElement.element("EXTERNAL_CASE_NO");

/*
            Element casePropertyAttr = caseElement.element("CASE_PROPERTY");
            if (casePropertyAttr != null) {
                limsCaseInfo.setCaseProperty(casePropertyAttr.getTextTrim());
            }
*/
        //现堪委托编号
        Element wtbhAttr = caseElement.element("WTBH");
        if (wtbhAttr != null) {
            if(Strings.isNotBlank(wtbhAttr.getTextTrim())){
                limsCaseInfo.setConsignationXkNo(wtbhAttr.getTextTrim());
            }else {
                limsCaseInfo.setConsignationXkNo("无");
            }
        }

        //检材
        /**
         * "<BIO_EVIDENCE>" +
         "   <W_NO>W11010505050020190108172014001</W_NO>" +
         "   <SERIAL_NO>1</SERIAL_NO>" +
         "   <DESCRIPTION>北侧摄像头擦拭物，棉签擦拭</DESCRIPTION>" +
         "   <COLLECT_BY>闫志鹏、张涛</COLLECT_BY>" +
         "   <COLLECT_DATE>2019-01-29 00:00:00</COLLECT_DATE>" +
         "   <EVIDENCE_NAME>北侧摄像头擦拭物</EVIDENCE_NAME>" +
         "   <SAMPLE_TYPE>2014</SAMPLE_TYPE>" +
         "   <COLLECT_POS>北侧摄像头</COLLECT_POS>" +
         "   <IF_SJ>0</IF_SJ><SAMPLE_DESC></SAMPLE_DESC>" +
         "   <TEST_DESC>无</TEST_DESC><WARN_MSG>DNA</WARN_MSG>" +
         "   <TYPE>0</TYPE><PERSON_ID></PERSON_ID>" +
         "</BIO_EVIDENCE>"
         */

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

                Element serialNoAttr = bioEvidenceElement.element("SERIAL_NO");
                if (serialNoAttr != null) {
                    if(StringUtils.isNotBlank(serialNoAttr.getTextTrim())){
                        try {
                            sampleInfoDna.setEvidenceSerialNo(Integer.parseInt(serialNoAttr.getTextTrim()));
                        }catch(Exception ex){
                            sampleInfoDna.setEvidenceSerialNo(null);
                        }
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


        Element memberElement = root.element("PERSON_LIST");
        List<LimsPersonInfo> limsPersonInfoList = new ArrayList();
        if (memberElement != null) {
            List memberElementList = memberElement.elements("PERSON");
            if(null != memberElementList && memberElementList.size() > 0){
                for (int i = 0; i < memberElementList.size(); i++) {
                    Element element = (Element)memberElementList.get(i);
                    LimsPersonInfo limsPersonInfo = new LimsPersonInfo();


                    Element idAttr = element.element("ID");
                    if (idAttr != null) {
                        limsPersonInfo.setPersonId(idAttr.getTextTrim());
                    }

                    Element nameAttr = element.element("NAME");
                    if (nameAttr != null) {
                        if(StringUtils.isNotBlank(nameAttr.getTextTrim())){
                            limsPersonInfo.setPersonName(nameAttr.getTextTrim());
                        }
                    }

                    Element sexAttr = element.element("SEX");
                    if (sexAttr != null) {
                        if(StringUtils.isNotBlank(sexAttr.getTextTrim())){
                            if (("男").equals(sexAttr.getTextTrim())) {
                                limsPersonInfo.setPersonGender("01");
                            }else if (("女").equals(sexAttr.getTextTrim())) {
                                limsPersonInfo.setPersonGender("02");
                            }else {
                                limsPersonInfo.setPersonGender("03");
                            }
                            limsPersonInfo.setPersonGenderName(sexAttr.getTextTrim());
                        }

                    }

                    Element ageAttr = element.element("AGE");
                    if (ageAttr != null) {
                        if(StringUtils.isNotBlank(ageAttr.getTextTrim())){
                            limsPersonInfo.setPerosnAge(Short.parseShort(ageAttr.getTextTrim()));
                        }
                    }

                    Element idCardAttr = element.element("IDCARD");
                    if (idCardAttr != null) {
                        if(StringUtils.isNotBlank(idCardAttr.getTextTrim())){
                            limsPersonInfo.setPersonIdCard(idCardAttr.getTextTrim());
                        }
                    }

                    Element addressAttr = element.element("ADDRESS");
                    if (addressAttr != null) {
                        if(StringUtils.isNotBlank(addressAttr.getTextTrim())){
                            limsPersonInfo.setPersonCurrentAddress(addressAttr.getTextTrim());
                        }
                    }

                    Element personTypeAttr = element.element("PERSON_TYPE");
                    if (personTypeAttr != null) {
                        if(StringUtils.isNotBlank(personTypeAttr.getTextTrim())){
                            String type = personTypeAttr.getTextTrim();
                            limsPersonInfo.setPersonType(xkPersonTypeToLimsType(type));
                            limsPersonInfo.setPersonTypeName(xkPersonTypeToLimsTypeName(type));
                            for (LimsSampleInfoDna sampleInfoDna : limsPersonSampleInfoList) {
                                if(StringUtils.isBlank(sampleInfoDna.getSampleDesc())){
                                    if(limsPersonInfo.getPersonId().equals(sampleInfoDna.getLinkId())){
                                        sampleInfoDna.setSampleDesc(xkPersonTypeToLimsTypeName(type)+limsPersonInfo.getPersonName()+"血样");
                                    }
                                }
                            }
                        }
                    }
                    limsPersonInfoList.add(limsPersonInfo);
                }
            }
        }

        try {
            limsSampleInfoList = limsSampleInfoList.stream().sorted(
                    Comparator.comparing(LimsSampleInfoDna::getEvidenceSerialNo, Comparator.nullsFirst(Integer::compareTo))
            ).collect(Collectors.toList());
        }catch(Exception ex){
            logger.error("对现场检材进行排序时出错。", ex);
        }

        returnMap.put("limsCaseInfo", limsCaseInfo);
        returnMap.put("limsSampleInfoList", limsSampleInfoList);
        returnMap.put("limsPersonSampleInfoList", limsPersonSampleInfoList);
        returnMap.put("limsPersonInfoList", limsPersonInfoList);

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

    public String xkPersonTypeToLimsType(String personType) {
        if (personType == "") {
            return "99";
        }

        switch (personType) {
            case "02":
                return "01";
            case "04":
                return "03";
            case "05":
                return "06";
        } return "99";
    }

    public String xkPersonTypeToLimsTypeName(String personType) {
        if (personType == "") {
            return "其它人员";
        }

        switch (personType) {
            case "02":
                return "嫌疑人";
            case "04":
                return "受害人";
            case "05":
                return "失踪人员亲属";
        } return "其它人员";
    }

}
