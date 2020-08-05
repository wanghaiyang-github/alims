package com.bazl.alims.service;

/**
 * Created by Administrator on 2017/1/7.
 */
public interface SeqNoGenerateService {

    public String getNextNoVal(String year, String code, String text,String orgId);

    /**
     * 获取流水号
     * @param year
     * @param code
     * @param orgId
     * @return
     */
    public String getNextVal(String year,String typeCode, String code,String orgId);

    /**
     * 修改流水号
     * @param year
     * @param typeCode
     * @param code
     * @param orgId
     * @return
     */
    public int updateTypeVal(String year,String typeCode, String code,String orgId);

}
