package com.bazl.alims.webservices;

import com.bazl.alims.model.po.XckyAddressInfo;

import java.util.Map;

/**
 * Created by Administrator on 2019/1/27.
 */
public interface XckyWebService {


    /**
     * 根据指定K号获取现场勘验系统中的相关信息
     *
     * @param xckyAddressInfo       调用的现勘地址信息
     * @param kno                     现勘案件K号
     * @return
     */
    public Map<String, Object> getXckyInfoByKno(XckyAddressInfo xckyAddressInfo, String kno);

}
