package com.bazl.alims.HttpClient;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Sun on 2019/1/22.
 */
@Repository
public interface GetXkCaseService {

    /**
     * 根据身份证号获取人员信息(毕节)
     * @return
     */
    public Map<String, Object> getCaseByXkNo(String xkNo);
}
