package com.bazl.alims.common;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Sun on 2016/12/28.
 */
public class Constants {

    //用户无效状态
    public static final String user_active_flase = "1";
    //用户有效效状态
    public static final String user_active_true = "0";
    //菜单顶级节点
    public static final String permission_root_flase = "1";
    //委托人添加委托人职务
    public static final String POSITION_LIST="POSITION_LIST";

    //案件登记字典项----------------
    //案件性质
    public static final String CASE_PROPERTY = "CASE_PROPERTY";
    //人员类型
    public static final String PERSON_TYPE = "PERSON_TYPE";
    //在逃人员
    public static final String PERSON_TYPE_07 = "07";
    //在逃人员亲属
    public static final String PERSON_TYPE_08 = "08";
    //性别
    public static final String GENDER = "GENDER";
    //男
    public static final String GENDER_01 = "01";
    //女
    public static final String GENDER_02 = "02";
    //未知
    public static final String GENDER_03 = "03";
    //男
    public static final String GENDER_MAN = "男";
    //女
    public static final String GENDER_WOMAN = "女";
    //未知
    public static final String GENDER_UNKNOWN = "未知";
    //检材类型
    public static final String SAMPLE_TYPE = "SAMPLE_TYPE";
    //包装方法
    public static final String PACK_METHOD = "PACK_METHOD";
    //案件级别
    public static final String CASE_LEVEL = "CASE_LEVEL";
    //案件状态
    public static final String CASE_STATUS = "CASE_STATUS";
    //案件状态_未受理
    public static final String CASE_STATUS_01 = "01";
    //案件状态_已完成
    public static final String CASE_STATUS_03 = "03";
    //案件状态_在检验
    public static final String CASE_STATUS_02 = "02";
    //案件状态_已退案
    public static final String CASE_STATUS_04 = "04";

    //案件类型
    public static final String CASE_TYPE = "CASE_TYPE";
    //人员关系类型
    public static final String RELATION_TYPE = "RELATION_TYPE";
    //提取方法
    public static final String EXTRACT_METHOD = "EXTRACT_METHOD";

    //检材载体
    public static final String SAMPLE_CARRIER = "SAMPLE_CARRIER";

    //流水号类型
    public static final String TYPE_CODE_CONSIGNMENT_NO = "CONSIGNMENT_NO";

    //法医中心id  forensicCenter
    public static final String FORENSIC_CENTER_ORG_ID = "110230000000";


    //市局
    public static final String ORG_LEVEL_TOP = "1";
    //分局
    public static final String ORG_LEVEL_FENJU = "2";
    //派出所
    public static final String ORG_LEVEL_PAICHUSUO = "3";

    /**
     * 生成流水号
     * @param serialNumber
     * @return
     */
    public static final String serialNumber(String serialNumber) {
        if(Integer.parseInt(serialNumber) == 99999){
            serialNumber = "00000";
        }
        String  number = (Integer.parseInt(serialNumber)+1)+"" ;
        int num = 5 - number.length() ;
        for (int i = 0; i < num; i++) {
            number = "0"+number;
        }
        return number;
    }

    public static final String selectOrgNameSp(String orgCode) {
        String orgNameSp = "";
        if(StringUtils.isNotBlank(orgCode) && ("110101000000").equals(orgCode)){
            orgNameSp = "DC";//东城分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110102000000").equals(orgCode)){
            orgNameSp = "XC";//西城分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110105000000").equals(orgCode)){
            orgNameSp = "CY";//朝阳分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110106000000").equals(orgCode)){
            orgNameSp = "FT";//丰台分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110107000000").equals(orgCode)){
            orgNameSp = "SJS";//石景山分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110108000000").equals(orgCode)){
            orgNameSp = "HD";//海淀分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110109000000").equals(orgCode)){
            orgNameSp = "MTG";//门头沟分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110111000000").equals(orgCode)){
            orgNameSp = "FS";//房山分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110112000000").equals(orgCode)){
            orgNameSp = "TZ";//通州分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110113000000").equals(orgCode)){
            orgNameSp = "SY";//顺义分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110114000000").equals(orgCode)){
            orgNameSp = "CP";//昌平分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110115000000").equals(orgCode)){
            orgNameSp = "DX";//大兴分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110116000000").equals(orgCode)){
            orgNameSp = "HR";//怀柔分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110117000000").equals(orgCode)){
            orgNameSp = "PG";//平谷分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110228000000").equals(orgCode)){
            orgNameSp = "MY";//密云分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110229000000").equals(orgCode)){
            orgNameSp = "YQ";//延庆分局
        }else if(StringUtils.isNotBlank(orgCode) && ("110230000000").equals(orgCode)){
            orgNameSp = "FY";//法医中心
        }else if(StringUtils.isNotBlank(orgCode) && ("110301000000").equals(orgCode)){
            orgNameSp = "XZ1";//刑侦总队一支队
        }else if(StringUtils.isNotBlank(orgCode) && ("110302000000").equals(orgCode)){
            orgNameSp = "XZ2";//刑侦总队二支队
        }else if(StringUtils.isNotBlank(orgCode) && ("110303000000").equals(orgCode)){
            orgNameSp = "XZ3";//刑侦总队三支队
        }else if(StringUtils.isNotBlank(orgCode) && ("110305000000").equals(orgCode)){
            orgNameSp = "XZ5";//刑侦总队五支队
        }else if(StringUtils.isNotBlank(orgCode) && ("110306000000").equals(orgCode)){
            orgNameSp = "XZ6";//刑侦总队六支队
        }else if(StringUtils.isNotBlank(orgCode) && ("110307000000").equals(orgCode)){
            orgNameSp = "XZ7";//刑侦总队七支队
        }
        return orgNameSp;
    }

    //委托状态_未受理
    public static final String STATUS_01 = "01";
    //委托状态_已受理
    public static final String STATUS_02 = "02";

    //未删除
    public static final String DELETE_FLAG_0 = "0";
    //已删除
    public static final String DELETE_FLAG_1 = "1";

    public static final String USER_TYPE_1 = "1";
    public static final String USER_TYPE_2 = "2";
    public static final String USER_TYPE_5 = "5";

    public static final String QUERY_FLAG_1 = "1";

    //事主样本信息
    public  static  final String CORE_VICTIM_STATS_TRUE = "1"; //入库中为事主样本信息
    public  static  final String CORE_VICTIM_STATS_FLASE ="0"; //入库中不为事主样本信息
    public  static  final String CORE_VICTIM_STATS_TEST  ="2"; //人员类型为受害人时前台展示的事主样本信息

    public static final String IDENTIFY_ITEM_FUGITIVES = "在逃人员委托";

}
