package com.bazl.alims.service.impl;

import com.bazl.alims.common.Constants;
import com.bazl.alims.dao.*;
import com.bazl.alims.model.LoaUserInfo;
import com.bazl.alims.model.PageInfo;
import com.bazl.alims.model.bo.DelegateDataModel;
import com.bazl.alims.model.po.*;
import com.bazl.alims.model.vo.LimsCaseInfoVo;
import com.bazl.alims.service.DictItemService;
import com.bazl.alims.service.LimsCaseInfoService;
import com.bazl.alims.service.SeqNoGenerateService;
import com.bazl.alims.utils.DateUtils;
import com.bazl.alims.utils.ImgUtils;
import com.bazl.alims.utils.UplodFtpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * Created by hj on 2018/12/20.
 */
@Service
public class LimsCaseInfoServiceImpl extends BaseService implements LimsCaseInfoService {

    @Autowired
    LimsCaseInfoMapper limsCaseInfoMapper;

    @Autowired
    DictItemService dictItemService;

    @Autowired
    SeqNoGenerateService seqNoGenerateService;

    @Autowired
    LimsConsignmentInfoMapper limsConsignmentInfoMapper;

    @Autowired
    LimsPersonInfoMapper limsPersonInfoMapper;

    @Autowired
    LimsPerosnRelationMapper limsPerosnRelationMapper;

    @Autowired
    LimsSampleInfoDnaMapper limsSampleInfoDnaMapper;

    @Autowired
    OrgInfoMapper orgInfoMapper;

    @Value("${personImg}")
    private String personImg;
    @Value("${sampleImg}")
    private String sampleImg;
    @Value("${ftpIp}")
    private String ftpIp;
    @Value("${ftpPort}")
    private String ftpPort;
    @Value("${ftpPersonImg}")
    private String ftpPersonImg;
    @Value("${ftpSampleImg}")
    private String ftpSampleImg;
    @Value("${ftpUser}")
    private String ftpUser;
    @Value("${ftpPassword}")
    private String ftpPassword;

    /**
     * 根据案件id查询案件信息
     * @param caseId
     * @return
     */
    @Override
    public LimsCaseInfo selectByCaseId(String caseId) {
        LimsCaseInfo caseInfoDna = limsCaseInfoMapper.selectByCaseId(caseId);
        return caseInfoDna;
    }

    /**
     * 根据案件id查询案件信息
     * @param caseXkNo
     * @return
     */
    @Override
    public List<LimsCaseInfo> selectByCaseXkNo(String caseXkNo, String acceptOrgId) {
        try {
            return limsCaseInfoMapper.selectByCaseXkNo(caseXkNo, acceptOrgId);
        }catch(Exception ex){
            logger.error("根据现勘K号获取案件信息错误！", ex);
            return null;
        }
    }

    /**
     * 查询案件数量
     */
    @Override
    public int selectCountByCaseStatus(String status, String delegateOrgCode) {
        return limsCaseInfoMapper.selectCountByCaseStatus(status, delegateOrgCode);
    }

    /**
     * 根据年份获取各个月份的案件数
     */
    @Override
    public HashMap selectMonthCountByYear(String year, String delegateOrgCode) {
        return limsCaseInfoMapper.selectMonthCountByYear(year, delegateOrgCode);
    }

    /**
     * 查询与补送查询list
     * @param query
     * @param pageInfo
     * @return
     */
    @Override
    public List<LimsCaseInfoVo> selectCaseInfoList(LimsCaseInfoVo query, PageInfo pageInfo) {
        List<LimsCaseInfoVo> caseInfoVOList = null;
        int pageNo;
        int pageSize;
        try {
            pageNo = pageInfo.getPage();
            pageSize = pageInfo.getEvePageRecordCnt();
            query.setOffset((pageNo - 1) * pageSize);
            query.setRows(query.getOffset() + pageSize);

            caseInfoVOList = limsCaseInfoMapper.selectVOPaginationList(query);
        } catch(Exception ex) {
            logger.info("查询与补送报错："+ex);
            return null;
        }

        return caseInfoVOList;
    }

    /**
     * 查询与补送查询count
     * @param consignationInfoVo
     * @return
     */
    @Override
    public int selectVOCount(LimsCaseInfoVo consignationInfoVo) {
        return limsCaseInfoMapper.selectVOCount(consignationInfoVo);
    }

    /**
     * 添加委托补送信息
     * @param delegateDataModel
     * @param operateUser
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> submitReplacement(DelegateDataModel delegateDataModel, LoaUserInfo operateUser, String evaluationCenterId){
        Map<String, String> result = new HashMap<>();
        try {

            //获取案件信息
            LimsCaseInfo limsCaseInfo = delegateDataModel.getCaseInfoDna();
            //修改案件补送标记为1  0：否  1：是
            limsCaseInfo.setHasAppendFlag("1");
            //数据推送修改crete_person(数据推送标识)
            this.datePushUpdateCreatePerson(limsCaseInfo);
            limsCaseInfoMapper.updateHasAppendFlagByCaseId(limsCaseInfo);

            //添加委托人信息
            LimsConsignmentInfo consignatioInfo = delegateDataModel.getConsignatioInfo();
            String consignmentId = UUID.randomUUID().toString();
            consignatioInfo.setConsignmentId(consignmentId);
            consignatioInfo.setCaseId(limsCaseInfo.getCaseId());
            //  consignatioInfo.setConsignmentNo(seqNoGenerateService.getNextNoVal(DateUtils.getCurrentYear(), Constants.TYPE_CODE_CONSIGNMENT_NO, Constants.selectOrgNameSp(consignatioInfo.getDelegateOrgCode()),evaluationCenterId));

            if (consignatioInfo.getConsignmentNo().equals("委托书编号自动生成") ) {
                //获取并生成流水号
                String code = seqNoGenerateService.getNextVal(DateUtils.getCurrentYear(), Constants.TYPE_CODE_CONSIGNMENT_NO, Constants.selectOrgNameSp(evaluationCenterId), evaluationCenterId);
                consignatioInfo.setConsignmentNo(code);
                try {
                    seqNoGenerateService.updateTypeVal(DateUtils.getCurrentYear(), Constants.TYPE_CODE_CONSIGNMENT_NO, Constants.selectOrgNameSp(evaluationCenterId), evaluationCenterId);
                } catch (Exception ex) {
                    logger.info("更新流水号失败：" + ex);
                }
            } else if(consignatioInfo.getConsignmentNo().equals("")) {
                consignatioInfo.setConsignmentNo(null);
            }else{
                consignatioInfo.setConsignmentNo(consignatioInfo.getConsignmentNo());
            }

            //consignatioInfo.setConsignmentNo(consignatioInfo.getConsignmentNo());

            consignatioInfo.setDelegateDatetime(new Date());
            int reidentifyCount = 0;
            consignatioInfo.setReidentifyCount((short)reidentifyCount);
            consignatioInfo.setCreateDatetime(new Date());
            consignatioInfo.setCreatePerson(operateUser.getLoginName());
            consignatioInfo.setDeleteFlag("0");
            consignatioInfo.setAppendFlag("1");
            consignatioInfo.setAcceptOrgId(evaluationCenterId);
            consignatioInfo.setStatus("01");
            limsConsignmentInfoMapper.insertConsignatioInfo(consignatioInfo);


            //添加被鉴定人信息
            List<LimsPersonInfo> limsPersonInfoList = delegateDataModel.getLimsPersonInfoList();
            if(null != limsPersonInfoList && limsPersonInfoList.size() > 0){
                for(LimsPersonInfo limsPersonInfo:limsPersonInfoList){

                    //对人员照片进行上传
                    if(StringUtils.isNotBlank(limsPersonInfo.getPersonFrontPicture())){
                        String personFrontPicturePath = ImgUtils.generateImage(limsPersonInfo.getPersonFrontPicture(), personImg);
                        String personFrontPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpPersonImg, personFrontPicturePath);
                        limsPersonInfo.setPersonFrontPicturePath(personFrontPicturePathFtp == null ? "" : personFrontPicturePathFtp);
                    }

                    PersonDetail personDetail = new PersonDetail();
                    LimsPersonInfo limsPersonInfo1 = new LimsPersonInfo();
                    //人员详细表
                    personDetail.setPersonDetailId(UUID.randomUUID().toString());
                    personDetail.setPersonName(limsPersonInfo.getPersonName());
                    personDetail.setPersonType(limsPersonInfo.getPersonType());
                    personDetail.setPersonGender(limsPersonInfo.getPersonGender());
                    personDetail.setPerosnAge(limsPersonInfo.getPerosnAge());
                    personDetail.setPersonHeight(limsPersonInfo.getPersonHeight());
                    personDetail.setPersonWeight(limsPersonInfo.getPersonWeight());
                    personDetail.setIdCardFlag(limsPersonInfo.getIdCardFlag());
                    personDetail.setPersonIdCard(limsPersonInfo.getPersonIdCard());
                    personDetail.setNoIdCardDesc(limsPersonInfo.getNoIdCardDesc());
                    personDetail.setPersonCurrentAddress(limsPersonInfo.getPersonCurrentAddress());
                    personDetail.setCreateDatetime(new Date());
                    personDetail.setCreatePerson(operateUser.getLoginName());
                    personDetail.setDeleteFlag("0");
                    if(StringUtils.isNotBlank(limsPersonInfo.getPersonFrontPicture())){
                        personDetail.setPersonFrontPicture(limsPersonInfo.getPersonFrontPicture());
                        personDetail.setPersonFrontPicturePath(limsPersonInfo.getPersonFrontPicturePath());
                    }
                    limsPersonInfoMapper.insertPersonDetail(personDetail);

                    //被鉴定人信息
                    String personId = UUID.randomUUID().toString();
                    limsPersonInfo1.setPersonId(personId);
                    limsPersonInfo1.setCaseId(limsCaseInfo.getCaseId());
                    limsPersonInfo1.setConsignmentId(consignatioInfo.getConsignmentId());
                    limsPersonInfo1.setPersonName(limsPersonInfo.getPersonName());
                    limsPersonInfo1.setPersonType(limsPersonInfo.getPersonType());
                    limsPersonInfo1.setCreateDatetime(new Date());
                    limsPersonInfo1.setCreatePerson(operateUser.getLoginName());
                    limsPersonInfo1.setDeleteFlag("0");
                    limsPersonInfo1.setPersonDetailId(personDetail.getPersonDetailId());
                    limsPersonInfoMapper.insertPersonInfo(limsPersonInfo1);

                    //判断亲缘关系是否为空
                    LimsPerosnRelation limsPerosnRelation = new LimsPerosnRelation();
                    limsPerosnRelation.setRelationId(UUID.randomUUID().toString());
                    limsPerosnRelation.setSourcePersonId(limsPersonInfo1.getPersonId());
                    limsPerosnRelation.setRelationType(limsPersonInfo.getRelationType());
                    limsPerosnRelation.setCreateDatetime(new Date());
                    limsPerosnRelation.setCreatePerson(operateUser.getLoginName());
                    limsPerosnRelation.setDeleteFlag("0");
                    limsPerosnRelationMapper.insertPersonRelation(limsPerosnRelation);


                    if(StringUtils.isNotBlank(limsPersonInfo.getSampleDnaPicture())){
                        //对人员样本照片进行上传
                        String sampleDnaPicturePath = ImgUtils.generateImage(limsPersonInfo.getSampleDnaPicture(), personImg);
                        String sampleDnaPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpSampleImg, sampleDnaPicturePath);
                        limsPersonInfo.setSampleDnaPicturePath(sampleDnaPicturePathFtp == null ? "" : sampleDnaPicturePathFtp);
                    }

                    //样本信息
                    List<LimsSampleInfoDna> limsSampleInfoDnaList = limsPersonInfo.getSampleInfoDnaList();
                    if(null != limsSampleInfoDnaList && limsSampleInfoDnaList.size() > 0){
                        for(LimsSampleInfoDna limsSampleInfoDna:limsSampleInfoDnaList){
                            //对人员样本照片进行上传
                            if(StringUtils.isNotBlank(limsSampleInfoDna.getSampleDnaPicture())){
                                String sampleDnaPicturePath = ImgUtils.generateImage(limsSampleInfoDna.getSampleDnaPicture(), personImg);
                                String sampleDnaPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpSampleImg, sampleDnaPicturePath);
                                limsSampleInfoDna.setSampleDnaPicturePath(sampleDnaPicturePathFtp == null ? "" : sampleDnaPicturePathFtp);
                            }
                            if(StringUtils.isBlank(limsSampleInfoDna.getSampleId()) && StringUtils.isNotBlank(limsSampleInfoDna.getSampleType())){
                                LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
                                String sampleId = UUID.randomUUID().toString();
                                sampleInfoDna.setSampleId(sampleId);
                                sampleInfoDna.setConsignmentId(consignatioInfo.getConsignmentId());
                                sampleInfoDna.setCaseId(limsCaseInfo.getCaseId());
                                sampleInfoDna.setSampleName(limsSampleInfoDna.getSampleName());
                                sampleInfoDna.setSampleType(limsSampleInfoDna.getSampleType());
                                sampleInfoDna.setSampleDesc(limsSampleInfoDna.getSampleDesc());
                                sampleInfoDna.setSamplePacking(limsSampleInfoDna.getSamplePacking());
                                sampleInfoDna.setExtractDatetime(limsSampleInfoDna.getExtractDatetime());
                                sampleInfoDna.setExtractMethod(limsSampleInfoDna.getExtractMethod());
                                sampleInfoDna.setExtractPerson(operateUser.getLoginName());
                                sampleInfoDna.setSampleFlag("1");
                                sampleInfoDna.setLinkId(limsPersonInfo1.getPersonId());
                                sampleInfoDna.setSamplePurpose(limsSampleInfoDna.getSamplePurpose());
                                sampleInfoDna.setSampleStatus("01");
                                sampleInfoDna.setInstoredFlag("0");
                                sampleInfoDna.setCreateDatetime(new Date());
                                sampleInfoDna.setCreatePerson(operateUser.getLoginName());
                                sampleInfoDna.setDeleteFlag("0");
                                //判断是否为中心提取
                                if (StringUtils.isNotBlank(limsSampleInfoDna.getCoreTakenStats())){
                                    sampleInfoDna.setCoreTakenStats(limsSampleInfoDna.getCoreTakenStats());
                                }
                                //判断是否为事主样本信息
                                if (StringUtils.isNotBlank(limsSampleInfoDna.getCoreVictimStats())){
                                    sampleInfoDna.setCoreVictimStats(limsSampleInfoDna.getCoreVictimStats());
                                }
                                if(StringUtils.isNotBlank(limsSampleInfoDna.getSampleDnaPicture())){
                                    sampleInfoDna.setSampleDnaPicture(limsSampleInfoDna.getSampleDnaPicture());
                                    sampleInfoDna.setSampleDnaPicturePath(limsSampleInfoDna.getSampleDnaPicturePath());
                                }
                                sampleInfoDna.setSampleCarrier(limsSampleInfoDna.getSampleCarrier());
                                limsSampleInfoDnaMapper.insertSampleInfoDna(sampleInfoDna);
                            }
                        }
                    }
                }
            }

            //添加检材信息
            List<LimsSampleInfoDna> sampleInfoDnaList = delegateDataModel.getSampleInfoDnaList();
            if(null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
                for(LimsSampleInfoDna sampleInfoDna:sampleInfoDnaList){

                    //对物证检材照片进行上传
                    if(StringUtils.isNotBlank(sampleInfoDna.getSampleMaterialPicture())){
                        String sampleMaterialPicturePath = ImgUtils.generateImage(sampleInfoDna.getSampleMaterialPicture(), personImg);
                        String sampleMaterialPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpSampleImg, sampleMaterialPicturePath);
                        sampleInfoDna.setSampleMaterialPicturePath(sampleMaterialPicturePathFtp == null ? "" : sampleMaterialPicturePathFtp);
                    }

                    sampleInfoDna.setSampleId(UUID.randomUUID().toString());
                    sampleInfoDna.setConsignmentId(consignatioInfo.getConsignmentId());
                    sampleInfoDna.setCaseId(limsCaseInfo.getCaseId());
                    sampleInfoDna.setEvidenceNo(sampleInfoDna.getEvidenceNo());
                    sampleInfoDna.setSampleName(sampleInfoDna.getSampleName());
                    sampleInfoDna.setSampleType(sampleInfoDna.getSampleType());
                    sampleInfoDna.setSampleDesc(sampleInfoDna.getSampleDesc());
                    sampleInfoDna.setSamplePacking(sampleInfoDna.getSamplePacking());
                    sampleInfoDna.setExtractDatetime(sampleInfoDna.getExtractDatetime());
                    sampleInfoDna.setExtractMethod(sampleInfoDna.getExtractMethod());
                    sampleInfoDna.setExtractPerson(operateUser.getLoginName());
                    sampleInfoDna.setSampleFlag("0");
                    //物证id不知道
                    sampleInfoDna.setSamplePurpose(sampleInfoDna.getSamplePurpose());
                    sampleInfoDna.setSampleStatus("01");
                    sampleInfoDna.setInstoredFlag("0");
                    sampleInfoDna.setCreateDatetime(new Date());
                    sampleInfoDna.setCreatePerson(operateUser.getLoginName());
                    sampleInfoDna.setDeleteFlag("0");
                    sampleInfoDna.setSampleCarrier(sampleInfoDna.getSampleCarrier());
                    //物证检材照片
                    sampleInfoDna.setSampleMaterialPicture(sampleInfoDna.getSampleMaterialPicture());
                    //物证检材照片路径
                    sampleInfoDna.setSampleMaterialPicturePath(sampleInfoDna.getSampleMaterialPicturePath());
                    //判断是否为中心提取
                    if (StringUtils.isNotBlank(sampleInfoDna.getCoreTakenStats())){
                        sampleInfoDna.setCoreTakenStats(sampleInfoDna.getCoreTakenStats());
                    }
                    limsSampleInfoDnaMapper.insertSampleInfoDna(sampleInfoDna);
                }
            }

            //记录该案件是第几次补送
            int replacementNum = 0;
            //查询之前是否补送
            LimsConsignmentInfo limsConsignmentInfo = limsConsignmentInfoMapper.selectMaxReplacementNum(limsCaseInfo.getCaseId());
            if(null != limsConsignmentInfo){
                if(null != limsConsignmentInfo.getReplacementNum()){
                    Integer replacementNum1 = limsConsignmentInfo.getReplacementNum();
                    replacementNum = replacementNum1+1;
                    //更新补送第几次数
                    LimsConsignmentInfo limsConsignmentInfo1 = new LimsConsignmentInfo();
                    limsConsignmentInfo1.setReplacementNum(replacementNum);
                    limsConsignmentInfo1.setConsignmentId(consignatioInfo.getConsignmentId());
                    limsConsignmentInfo1.setCaseId(limsCaseInfo.getCaseId());
                    limsConsignmentInfoMapper.updateReplacementNum(limsConsignmentInfo1);
                }
            }else{
                //更新补送第几次数
                LimsConsignmentInfo limsConsignmentInfo1 = new LimsConsignmentInfo();
                limsConsignmentInfo1.setReplacementNum(1);
                limsConsignmentInfo1.setConsignmentId(consignatioInfo.getConsignmentId());
                limsConsignmentInfo1.setCaseId(limsCaseInfo.getCaseId());
                limsConsignmentInfoMapper.updateReplacementNum(limsConsignmentInfo1);
            }

            result.put("caseId", limsCaseInfo.getCaseId());
            result.put("consignmentId", consignatioInfo.getConsignmentId());

        } catch (Exception ex) {
            logger.info("添加委托补送信息报错："+ex);
            throw ex;
        }

        return result;
    }

    /**
     * 查询补送记录
     * @param limsCaseInfoVo
     * @return
     */
    @Override
    public List<LimsCaseInfoVo> selectReplacementRecord(LimsCaseInfoVo limsCaseInfoVo) {
        return limsCaseInfoMapper.selectReplacementRecord(limsCaseInfoVo);
    }


    /**
     * 添加非案件委托补送信息
     * @param delegateDataModel
     * @param operateUser
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> submitNonCaseReplacement(DelegateDataModel delegateDataModel, LoaUserInfo operateUser, String evaluationCenterId){
        Map<String, String> result = new HashMap<>();
        try {

            //获取案件信息
            LimsCaseInfo limsCaseInfo = delegateDataModel.getCaseInfoDna();
            //修改案件补送标记为1  0：否  1：是
            limsCaseInfo.setHasAppendFlag("1");
            //数据推送修改crete_person(数据推送标识)
            this.datePushUpdateCreatePerson(limsCaseInfo);
            limsCaseInfoMapper.updateHasAppendFlagByCaseId(limsCaseInfo);

            //添加委托人信息
            LimsConsignmentInfo consignatioInfo = delegateDataModel.getConsignatioInfo();
            String consignmentId = UUID.randomUUID().toString();
            consignatioInfo.setConsignmentId(consignmentId);
            consignatioInfo.setCaseId(limsCaseInfo.getCaseId());
//            consignatioInfo.setConsignmentNo(seqNoGenerateService.getNextNoVal(DateUtils.getCurrentYear(), Constants.TYPE_CODE_CONSIGNMENT_NO, Constants.selectOrgNameSp(consignatioInfo.getDelegateOrgCode()),evaluationCenterId));
//            consignatioInfo.setConsignmentNo(consignatioInfo.getConsignmentNo());
            if (consignatioInfo.getConsignmentNo().equals("委托书编号自动生成") ) {
                //获取并生成流水号
                String code = seqNoGenerateService.getNextVal(DateUtils.getCurrentYear(), Constants.TYPE_CODE_CONSIGNMENT_NO, Constants.selectOrgNameSp(evaluationCenterId), evaluationCenterId);
                consignatioInfo.setConsignmentNo(code);
                try {
                    seqNoGenerateService.updateTypeVal(DateUtils.getCurrentYear(), Constants.TYPE_CODE_CONSIGNMENT_NO, Constants.selectOrgNameSp(evaluationCenterId), evaluationCenterId);
                } catch (Exception ex) {
                    logger.info("更新流水号失败：" + ex);
                }
            } else if(consignatioInfo.getConsignmentNo().equals("")) {
                consignatioInfo.setConsignmentNo(null);
            }else{
                consignatioInfo.setConsignmentNo(consignatioInfo.getConsignmentNo());
            }
            consignatioInfo.setDelegateDatetime(new Date());
            int reidentifyCount = 0;
            consignatioInfo.setReidentifyCount((short)reidentifyCount);
            consignatioInfo.setCreateDatetime(new Date());
            consignatioInfo.setCreatePerson(operateUser.getLoginName());
            consignatioInfo.setDeleteFlag("0");
            consignatioInfo.setAppendFlag("1");
            consignatioInfo.setAcceptOrgId(evaluationCenterId);
            consignatioInfo.setStatus("01");
            limsConsignmentInfoMapper.insertConsignatioInfo(consignatioInfo);


            //添加被鉴定人信息
            List<LimsPersonInfo> limsPersonInfoList = delegateDataModel.getLimsPersonInfoList();
            if(null != limsPersonInfoList && limsPersonInfoList.size() > 0){
                for(LimsPersonInfo limsPersonInfo:limsPersonInfoList){

                    //对人员照片进行上传
                    if(StringUtils.isNotBlank(limsPersonInfo.getPersonFrontPicture())){
                        String personFrontPicturePath = ImgUtils.generateImage(limsPersonInfo.getPersonFrontPicture(), personImg);
                        String personFrontPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpPersonImg, personFrontPicturePath);
                        limsPersonInfo.setPersonFrontPicturePath(personFrontPicturePathFtp == null ? "" : personFrontPicturePathFtp);
                    }

                    PersonDetail personDetail = new PersonDetail();
                    LimsPersonInfo limsPersonInfo1 = new LimsPersonInfo();
                    //人员详细表
                    personDetail.setPersonDetailId(UUID.randomUUID().toString());
                    personDetail.setPersonName(limsPersonInfo.getPersonName());
                    personDetail.setPersonNo(limsPersonInfo.getPersonNo());
                    personDetail.setPersonType(limsPersonInfo.getPersonType());
                    personDetail.setPersonGender(limsPersonInfo.getPersonGender());
                    personDetail.setPerosnAge(limsPersonInfo.getPerosnAge());
                    personDetail.setPersonHeight(limsPersonInfo.getPersonHeight());
                    personDetail.setPersonWeight(limsPersonInfo.getPersonWeight());
                    personDetail.setIdCardFlag(limsPersonInfo.getIdCardFlag());
                    personDetail.setPersonIdCard(limsPersonInfo.getPersonIdCard());
                    personDetail.setNoIdCardDesc(limsPersonInfo.getNoIdCardDesc());
                    personDetail.setPersonCurrentAddress(limsPersonInfo.getPersonCurrentAddress());
                    personDetail.setCreateDatetime(new Date());
                    personDetail.setCreatePerson(operateUser.getLoginName());
                    personDetail.setDeleteFlag("0");
                    if(StringUtils.isNotBlank(limsPersonInfo.getPersonFrontPicture())){
                        personDetail.setPersonFrontPicture(limsPersonInfo.getPersonFrontPicture());
                        personDetail.setPersonFrontPicturePath(limsPersonInfo.getPersonFrontPicturePath());
                    }
                    limsPersonInfoMapper.insertPersonDetail(personDetail);

                    //被鉴定人信息
                    String personId = UUID.randomUUID().toString();
                    limsPersonInfo1.setPersonId(personId);
                    limsPersonInfo1.setCaseId(limsCaseInfo.getCaseId());
                    limsPersonInfo1.setConsignmentId(consignatioInfo.getConsignmentId());
                    limsPersonInfo1.setPersonName(limsPersonInfo.getPersonName());
                    limsPersonInfo1.setPersonNo(limsPersonInfo.getPersonNo());
                    limsPersonInfo1.setPersonType(limsPersonInfo.getPersonType());
                    limsPersonInfo1.setCreateDatetime(new Date());
                    limsPersonInfo1.setCreatePerson(operateUser.getLoginName());
                    limsPersonInfo1.setDeleteFlag("0");
                    limsPersonInfo1.setPersonDetailId(personDetail.getPersonDetailId());
                    limsPersonInfoMapper.insertPersonInfo(limsPersonInfo1);

                    //判断亲缘关系是否为空
                    LimsPerosnRelation limsPerosnRelation = new LimsPerosnRelation();
                    limsPerosnRelation.setRelationId(UUID.randomUUID().toString());
                    limsPerosnRelation.setSourcePersonId(limsPersonInfo1.getPersonId());
                    limsPerosnRelation.setRelationType(limsPersonInfo.getRelationType());
                    limsPerosnRelation.setCreateDatetime(new Date());
                    limsPerosnRelation.setCreatePerson(operateUser.getLoginName());
                    limsPerosnRelation.setDeleteFlag("0");
                    limsPerosnRelationMapper.insertPersonRelation(limsPerosnRelation);


                    if(StringUtils.isNotBlank(limsPersonInfo.getSampleDnaPicture())){
                        //对人员样本照片进行上传
                        String sampleDnaPicturePath = ImgUtils.generateImage(limsPersonInfo.getSampleDnaPicture(), personImg);
                        String sampleDnaPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpSampleImg, sampleDnaPicturePath);
                        limsPersonInfo.setSampleDnaPicturePath(sampleDnaPicturePathFtp == null ? "" : sampleDnaPicturePathFtp);
                    }

                    //样本信息
                    List<LimsSampleInfoDna> limsSampleInfoDnaList = limsPersonInfo.getSampleInfoDnaList();
                    if(null != limsSampleInfoDnaList && limsSampleInfoDnaList.size() > 0){
                        for(LimsSampleInfoDna limsSampleInfoDna:limsSampleInfoDnaList){

                            //对人员样本照片进行上传
                            if(StringUtils.isNotBlank(limsSampleInfoDna.getSampleDnaPicture())){
                                String sampleDnaPicturePath = ImgUtils.generateImage(limsSampleInfoDna.getSampleDnaPicture(), personImg);
                                String sampleDnaPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpSampleImg, sampleDnaPicturePath);
                                limsSampleInfoDna.setSampleDnaPicturePath(sampleDnaPicturePathFtp == null ? "" : sampleDnaPicturePathFtp);
                            }

                            if(StringUtils.isBlank(limsSampleInfoDna.getSampleId()) && StringUtils.isNotBlank(limsSampleInfoDna.getSampleType())){
                                LimsSampleInfoDna sampleInfoDna = new LimsSampleInfoDna();
                                String sampleId = UUID.randomUUID().toString();
                                sampleInfoDna.setSampleId(sampleId);
                                sampleInfoDna.setConsignmentId(consignatioInfo.getConsignmentId());
                                sampleInfoDna.setCaseId(limsCaseInfo.getCaseId());
                                sampleInfoDna.setSampleName(limsSampleInfoDna.getSampleName());
                                sampleInfoDna.setSampleType(limsSampleInfoDna.getSampleType());
                                sampleInfoDna.setSampleDesc(limsSampleInfoDna.getSampleDesc());
                                sampleInfoDna.setSamplePacking(limsSampleInfoDna.getSamplePacking());
                                sampleInfoDna.setExtractDatetime(limsSampleInfoDna.getExtractDatetime());
                                sampleInfoDna.setExtractMethod(limsSampleInfoDna.getExtractMethod());
                                sampleInfoDna.setExtractPerson(operateUser.getLoginName());
                                sampleInfoDna.setSampleFlag("1");
                                sampleInfoDna.setLinkId(limsPersonInfo1.getPersonId());
                                sampleInfoDna.setSamplePurpose(limsSampleInfoDna.getSamplePurpose());
                                sampleInfoDna.setSampleStatus("01");
                                sampleInfoDna.setInstoredFlag("0");
                                sampleInfoDna.setCreateDatetime(new Date());
                                sampleInfoDna.setCreatePerson(operateUser.getLoginName());
                                sampleInfoDna.setDeleteFlag("0");
                                if(StringUtils.isNotBlank(limsSampleInfoDna.getSampleDnaPicture())){
                                    sampleInfoDna.setSampleDnaPicture(limsSampleInfoDna.getSampleDnaPicture());
                                    sampleInfoDna.setSampleDnaPicturePath(limsSampleInfoDna.getSampleDnaPicturePath());
                                }
                                sampleInfoDna.setSampleCarrier(limsSampleInfoDna.getSampleCarrier());
                                //是否为中心送检
                                if(StringUtils.isNotBlank(limsSampleInfoDna.getCoreTakenStats())){
                                    sampleInfoDna.setCoreTakenStats(limsSampleInfoDna.getCoreTakenStats());
                                }
                                //是否是事主样本信息
                                if (StringUtils.isNotBlank(limsSampleInfoDna.getCoreVictimStats())){
                                    sampleInfoDna.setCoreVictimStats(limsSampleInfoDna.getCoreVictimStats());
                                }
                                limsSampleInfoDnaMapper.insertSampleInfoDna(sampleInfoDna);
                            }
                        }
                    }
                }
            }

            //添加检材信息
            List<LimsSampleInfoDna> sampleInfoDnaList = delegateDataModel.getSampleInfoDnaList();
            if(null != sampleInfoDnaList && sampleInfoDnaList.size() > 0){
                for(LimsSampleInfoDna sampleInfoDna:sampleInfoDnaList){

                    //对物证检材照片进行上传
                    if(StringUtils.isNotBlank(sampleInfoDna.getSampleMaterialPicture())){
                        String sampleMaterialPicturePath = ImgUtils.generateImage(sampleInfoDna.getSampleMaterialPicture(), personImg);
                        String sampleMaterialPicturePathFtp = UplodFtpUtils.uploadFtpFile(ftpIp, ftpPort, ftpUser, ftpPassword, ftpSampleImg, sampleMaterialPicturePath);
                        sampleInfoDna.setSampleMaterialPicturePath(sampleMaterialPicturePathFtp == null ? "" : sampleMaterialPicturePathFtp);
                    }

                    sampleInfoDna.setSampleId(UUID.randomUUID().toString());
                    sampleInfoDna.setConsignmentId(consignatioInfo.getConsignmentId());
                    sampleInfoDna.setCaseId(limsCaseInfo.getCaseId());
                    sampleInfoDna.setEvidenceNo(sampleInfoDna.getEvidenceNo());
                    sampleInfoDna.setSampleName(sampleInfoDna.getSampleName());
                    sampleInfoDna.setSampleType(sampleInfoDna.getSampleType());
                    sampleInfoDna.setSampleDesc(sampleInfoDna.getSampleDesc());
                    sampleInfoDna.setSamplePacking(sampleInfoDna.getSamplePacking());
                    sampleInfoDna.setExtractDatetime(sampleInfoDna.getExtractDatetime());
                    sampleInfoDna.setExtractMethod(sampleInfoDna.getExtractMethod());
                    sampleInfoDna.setExtractPerson(operateUser.getLoginName());
                    sampleInfoDna.setSampleFlag("0");
                    //物证id不知道
                    sampleInfoDna.setSamplePurpose(sampleInfoDna.getSamplePurpose());
                    sampleInfoDna.setSampleStatus("01");
                    sampleInfoDna.setInstoredFlag("0");
                    sampleInfoDna.setCreateDatetime(new Date());
                    sampleInfoDna.setCreatePerson(operateUser.getLoginName());
                    sampleInfoDna.setDeleteFlag("0");
                    sampleInfoDna.setSampleCarrier(sampleInfoDna.getSampleCarrier());
                    //物证检材照片
                    sampleInfoDna.setSampleMaterialPicture(sampleInfoDna.getSampleMaterialPicture());
                    //物证检材照片路径
                    sampleInfoDna.setSampleMaterialPicturePath(sampleInfoDna.getSampleMaterialPicturePath());
                    //是否为事主
                    if (StringUtils.isNotBlank(sampleInfoDna.getCoreVictimStats())){
                        sampleInfoDna.setCoreVictimStats(sampleInfoDna.getCoreVictimStats());
                    }
                    //是否为中心送检
                    if (StringUtils.isNotBlank(sampleInfoDna.getCoreTakenStats())){
                        sampleInfoDna.setCoreTakenStats(sampleInfoDna.getCoreTakenStats());
                    }
                    limsSampleInfoDnaMapper.insertSampleInfoDna(sampleInfoDna);
                }
            }

            //记录该案件是第几次补送
            int replacementNum = 0;
            //查询之前是否补送
            LimsConsignmentInfo limsConsignmentInfo = limsConsignmentInfoMapper.selectMaxReplacementNum(limsCaseInfo.getCaseId());
            if(null != limsConsignmentInfo){
                if(null != limsConsignmentInfo.getReplacementNum()){
                    Integer replacementNum1 = limsConsignmentInfo.getReplacementNum();
                    replacementNum = replacementNum1+1;
                    //更新补送第几次数
                    LimsConsignmentInfo limsConsignmentInfo1 = new LimsConsignmentInfo();
                    limsConsignmentInfo1.setReplacementNum(replacementNum);
                    limsConsignmentInfo1.setConsignmentId(consignatioInfo.getConsignmentId());
                    limsConsignmentInfo1.setCaseId(limsCaseInfo.getCaseId());
                    limsConsignmentInfoMapper.updateReplacementNum(limsConsignmentInfo1);
                }
            }else{
                //更新补送第几次数
                LimsConsignmentInfo limsConsignmentInfo1 = new LimsConsignmentInfo();
                limsConsignmentInfo1.setReplacementNum(1);
                limsConsignmentInfo1.setConsignmentId(consignatioInfo.getConsignmentId());
                limsConsignmentInfo1.setCaseId(limsCaseInfo.getCaseId());
                limsConsignmentInfoMapper.updateReplacementNum(limsConsignmentInfo1);
            }

            result.put("caseId", limsCaseInfo.getCaseId());
            result.put("consignmentId", consignatioInfo.getConsignmentId());

        } catch (Exception ex) {
            logger.info("添加非案件委托补送信息报错："+ex);
            throw ex;
        }

        return result;
    }

    /**
     * 数据推送修改crete_person(数据推送标识)  不需要推送数据到老系统时可以注掉
     *
     * @param limsCaseInfo
     */
    private void datePushUpdateCreatePerson(LimsCaseInfo limsCaseInfo) {
        if (StringUtils.isNotEmpty(limsCaseInfo.getCaseId())) {
            LimsCaseInfo limsCaseInfo1 = this.selectByCaseId(limsCaseInfo.getCaseId());
            if(limsCaseInfo1!=null){
                String createPerson = limsCaseInfo1.getCreatePerson();
                if(StringUtils.isNotEmpty(createPerson)){
                    if(createPerson.indexOf("dbPush")!=-1){
                        //去除dbPush_
                        limsCaseInfo.setCreatePerson(createPerson.replace("dbPush_", ""));
                    }else{
                        limsCaseInfo.setCreatePerson(createPerson);
                    }
                }

            }
        }

    }


    /**
     * 通用查询分页list
     * @param query
     * @param pageInfo
     * @return
     */
    @Override
    public List<LimsCaseInfoVo> selectCaseQueryInfoList(LimsCaseInfoVo query, PageInfo pageInfo) {
        List<LimsCaseInfoVo> caseInfoVOList = null;
        int pageNo;
        int pageSize;
        try {
            pageNo = pageInfo.getPage();
            pageSize = pageInfo.getEvePageRecordCnt();
            query.setOffset((pageNo - 1) * pageSize);
            query.setRows(query.getOffset() + pageSize);

            caseInfoVOList = limsCaseInfoMapper.selectCaseQueryInfoList(query);
        } catch(Exception ex) {
            logger.info("通用查询报错："+ex);
            return null;
        }

        return caseInfoVOList;
    }

    /**
     * 通用查询count
     * @param consignationInfoVo
     * @return
     */
    @Override
    public int selectCaseQueryVOCount(LimsCaseInfoVo consignationInfoVo) {
        return limsCaseInfoMapper.selectCaseQueryVOCount(consignationInfoVo);
    }

    @Override
    public int queryXkNoCount(String caseXkNo) {
        return limsCaseInfoMapper.queryXkNoCount(caseXkNo);
    }


    @Override
    public void updateCaseXAno(String caseXkNo, String xkAno,String consignationXkNo) {
        limsCaseInfoMapper.updateCaseXAno(caseXkNo,xkAno,consignationXkNo);
    }

    @Override
    public List<LimsCaseInfoVo> selectAllConsignmentIdw(Integer offset, Integer rows) {
        LimsCaseInfoVo queryVo = new LimsCaseInfoVo();
        if(offset.equals(0)){
            queryVo.setOffset(0);
            queryVo.setRows(rows);
        }else{
            queryVo.setOffset(offset*rows);
            queryVo.setRows(offset*rows+rows);
        }
        return limsCaseInfoMapper.selectAllConsignmentId(queryVo);
    }

}
